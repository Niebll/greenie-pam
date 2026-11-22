-- =====================================================================
-- 002 — Favorit (bookmark) postingan jurnal
-- Jalankan SETELAH schema.sql: Supabase Dashboard → SQL Editor → Run.
-- Favorit bersifat pribadi: hanya pemiliknya yang bisa melihat.
-- =====================================================================

create table public.journal_favorites (
  journal_id  bigint not null references public.journals (id) on delete cascade,
  user_id     uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  created_at  timestamptz not null default now(),
  primary key (journal_id, user_id)
);

create index journal_favorites_user_id_idx on public.journal_favorites (user_id, created_at desc);

alter table public.journal_favorites enable row level security;

create policy "User melihat favoritnya sendiri"
  on public.journal_favorites for select to authenticated
  using ((select auth.uid()) = user_id);

create policy "User menambah favoritnya sendiri"
  on public.journal_favorites for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User menghapus favoritnya sendiri"
  on public.journal_favorites for delete to authenticated
  using ((select auth.uid()) = user_id);
