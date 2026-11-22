-- =====================================================================
-- Greenie — skema database Supabase
-- Jalankan SEKALI di Supabase Dashboard → SQL Editor → New query → Run.
-- Semua tabel memakai RLS (Row Level Security).
-- =====================================================================

-- ---------------------------------------------------------------------
-- PROFILES: data profil user (1 baris per akun auth)
-- ---------------------------------------------------------------------
create table public.profiles (
  id          uuid primary key references auth.users (id) on delete cascade,
  name        text not null default '',
  username    text unique,
  bio         text not null default '',
  created_at  timestamptz not null default now()
);

alter table public.profiles enable row level security;

create policy "Profil bisa dibaca user yang login"
  on public.profiles for select to authenticated
  using (true);

create policy "User mengubah profilnya sendiri"
  on public.profiles for update to authenticated
  using ((select auth.uid()) = id)
  with check ((select auth.uid()) = id);

-- Otomatis buat baris profil saat user mendaftar
create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer
set search_path = ''
as $$
begin
  insert into public.profiles (id, name)
  values (
    new.id,
    coalesce(nullif(new.raw_user_meta_data ->> 'name', ''), split_part(new.email, '@', 1))
  );
  return new;
end;
$$;

create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();

-- ---------------------------------------------------------------------
-- PLANTS: koleksi tanaman milik user
-- ---------------------------------------------------------------------
create table public.plants (
  id             bigint generated always as identity primary key,
  user_id        uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  name           text not null check (char_length(name) between 1 and 60),
  gradient_type  text not null default 'GREEN' check (gradient_type in ('GREEN', 'GOLD', 'TEAL')),
  created_at     timestamptz not null default now()
);

create index plants_user_id_idx on public.plants (user_id);

alter table public.plants enable row level security;

create policy "User melihat tanamannya sendiri"
  on public.plants for select to authenticated
  using ((select auth.uid()) = user_id);

create policy "User menambah tanamannya sendiri"
  on public.plants for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User mengubah tanamannya sendiri"
  on public.plants for update to authenticated
  using ((select auth.uid()) = user_id)
  with check ((select auth.uid()) = user_id);

create policy "User menghapus tanamannya sendiri"
  on public.plants for delete to authenticated
  using ((select auth.uid()) = user_id);

-- ---------------------------------------------------------------------
-- JOURNALS: jurnal pertumbuhan (yang is_shared = true tampil di Beranda)
-- ---------------------------------------------------------------------
create table public.journals (
  id            bigint generated always as identity primary key,
  user_id       uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  plant_name    text not null,
  journal_date  text not null,
  height        text not null default '',
  notes         text not null default '',
  condition     text not null default '',
  is_shared     boolean not null default true,
  created_at    timestamptz not null default now()
);

create index journals_user_id_idx on public.journals (user_id);
create index journals_shared_created_idx on public.journals (is_shared, created_at desc);

alter table public.journals enable row level security;

create policy "Jurnal yang dibagikan atau milik sendiri bisa dibaca"
  on public.journals for select to authenticated
  using (is_shared or (select auth.uid()) = user_id);

create policy "User menulis jurnalnya sendiri"
  on public.journals for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User mengubah jurnalnya sendiri"
  on public.journals for update to authenticated
  using ((select auth.uid()) = user_id)
  with check ((select auth.uid()) = user_id);

create policy "User menghapus jurnalnya sendiri"
  on public.journals for delete to authenticated
  using ((select auth.uid()) = user_id);

-- ---------------------------------------------------------------------
-- JOURNAL_LIKES: suka pada postingan jurnal
-- ---------------------------------------------------------------------
create table public.journal_likes (
  journal_id  bigint not null references public.journals (id) on delete cascade,
  user_id     uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  created_at  timestamptz not null default now(),
  primary key (journal_id, user_id)
);

create index journal_likes_user_id_idx on public.journal_likes (user_id);

alter table public.journal_likes enable row level security;

create policy "Like bisa dibaca user yang login"
  on public.journal_likes for select to authenticated
  using (true);

create policy "User memberi like atas namanya sendiri"
  on public.journal_likes for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User membatalkan like-nya sendiri"
  on public.journal_likes for delete to authenticated
  using ((select auth.uid()) = user_id);

-- ---------------------------------------------------------------------
-- JOURNAL_COMMENTS: komentar pada postingan jurnal
-- ---------------------------------------------------------------------
create table public.journal_comments (
  id          bigint generated always as identity primary key,
  journal_id  bigint not null references public.journals (id) on delete cascade,
  user_id     uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  body        text not null check (char_length(body) between 1 and 500),
  created_at  timestamptz not null default now()
);

create index journal_comments_journal_id_idx on public.journal_comments (journal_id);
create index journal_comments_user_id_idx on public.journal_comments (user_id);

alter table public.journal_comments enable row level security;

create policy "Komentar bisa dibaca user yang login"
  on public.journal_comments for select to authenticated
  using (true);

create policy "User berkomentar atas namanya sendiri"
  on public.journal_comments for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User menghapus komentarnya sendiri"
  on public.journal_comments for delete to authenticated
  using ((select auth.uid()) = user_id);

-- ---------------------------------------------------------------------
-- CARE_TASKS: jadwal siram & pupuk
-- ---------------------------------------------------------------------
create table public.care_tasks (
  id           bigint generated always as identity primary key,
  user_id      uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  plant_name   text not null,
  action_name  text not null,
  task_time    text not null default '',
  task_date    date not null,
  is_done      boolean not null default false,
  created_at   timestamptz not null default now()
);

create index care_tasks_user_date_idx on public.care_tasks (user_id, task_date);

alter table public.care_tasks enable row level security;

create policy "User melihat jadwalnya sendiri"
  on public.care_tasks for select to authenticated
  using ((select auth.uid()) = user_id);

create policy "User menambah jadwalnya sendiri"
  on public.care_tasks for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User mengubah jadwalnya sendiri"
  on public.care_tasks for update to authenticated
  using ((select auth.uid()) = user_id)
  with check ((select auth.uid()) = user_id);

create policy "User menghapus jadwalnya sendiri"
  on public.care_tasks for delete to authenticated
  using ((select auth.uid()) = user_id);
