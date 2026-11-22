-- =====================================================================
-- 003 — Pustaka tanaman (katalog) + favorit tanaman
-- Jalankan SETELAH 002_favorites.sql: Supabase Dashboard → SQL Editor → Run.
-- Katalog bisa ditambah/diubah lewat Table Editor → plant_library.
-- =====================================================================

-- ---------------------------------------------------------------------
-- PLANT_LIBRARY: katalog jenis tanaman (sama untuk semua user)
-- ---------------------------------------------------------------------
create table public.plant_library (
  id               bigint generated always as identity primary key,
  name             text not null unique,
  latin_name       text not null default '',
  category         text not null check (category in ('Hias daun', 'Sukulen', 'Berbunga')),
  difficulty       text not null check (difficulty in ('Mudah', 'Sedang', 'Sulit')),
  light            text not null,            -- untuk kartu info, mis. "Terang, tidak langsung"
  light_tag        text not null,            -- versi singkat untuk tag, mis. "Teduh–terang"
  water_days       int  not null check (water_days > 0),
  water_text       text not null,            -- mis. "Tiap 5–7 hari"
  humidity         text not null check (humidity in ('Rendah', 'Sedang', 'Tinggi')),
  description      text not null default '',
  care_guide       text not null default '',
  color            text not null default 'GREEN' check (color in ('GREEN', 'LIME', 'GOLD', 'TEAL')),
  popularity       int  not null default 0,  -- makin besar makin atas di tab Populer
  is_recommended   boolean not null default false,
  created_at       timestamptz not null default now()
);

alter table public.plant_library enable row level security;

-- Hanya bisa dibaca dari aplikasi; isi katalog diubah lewat dashboard
create policy "Katalog bisa dibaca user yang login"
  on public.plant_library for select to authenticated
  using (true);

-- ---------------------------------------------------------------------
-- PLANTS: tandai tanaman koleksi yang berasal dari katalog
-- ---------------------------------------------------------------------
alter table public.plants
  add column library_id bigint references public.plant_library (id) on delete set null;

create index plants_library_id_idx on public.plants (library_id);

-- ---------------------------------------------------------------------
-- PLANT_LIBRARY_FAVORITES: tanaman katalog yang difavoritkan user (pribadi)
-- ---------------------------------------------------------------------
create table public.plant_library_favorites (
  library_id  bigint not null references public.plant_library (id) on delete cascade,
  user_id     uuid not null default auth.uid() references public.profiles (id) on delete cascade,
  created_at  timestamptz not null default now(),
  primary key (library_id, user_id)
);

create index plant_library_favorites_user_idx on public.plant_library_favorites (user_id, created_at desc);

alter table public.plant_library_favorites enable row level security;

create policy "User melihat tanaman favoritnya sendiri"
  on public.plant_library_favorites for select to authenticated
  using ((select auth.uid()) = user_id);

create policy "User menambah tanaman favoritnya sendiri"
  on public.plant_library_favorites for insert to authenticated
  with check ((select auth.uid()) = user_id);

create policy "User menghapus tanaman favoritnya sendiri"
  on public.plant_library_favorites for delete to authenticated
  using ((select auth.uid()) = user_id);

-- ---------------------------------------------------------------------
-- Isi awal katalog
-- ---------------------------------------------------------------------
insert into public.plant_library
  (name, latin_name, category, difficulty, light, light_tag, water_days, water_text, humidity, description, care_guide, color, popularity, is_recommended)
values
  ('Monstera', 'Monstera deliciosa', 'Hias daun', 'Mudah',
   'Terang, tidak langsung', 'Terang', 7, 'Tiap 5–7 hari', 'Sedang',
   'Monstera deliciosa dikenal juga sebagai janda bolong karena daunnya yang besar dengan celah alami. Tanaman tropis asal Amerika Tengah ini populer sebagai penghias ruangan, mudah dirawat untuk pemula, dan tumbuh subur di tempat terang tanpa sinar matahari langsung.',
   'Siram saat 3–5 cm media tanam bagian atas terasa kering. Letakkan dekat jendela, hindari sinar matahari langsung.',
   'GREEN', 100, false),

  ('Kaktus', 'Cactaceae', 'Sukulen', 'Mudah',
   'Terang, sinar langsung', 'Terang', 14, 'Tiap 2–3 minggu', 'Rendah',
   'Kaktus menyimpan air di batangnya sehingga tahan kering dan cocok untuk yang sering lupa menyiram. Bentuknya beragam, dari yang bulat mungil sampai tinggi berduri.',
   'Siram hanya saat media benar-benar kering. Pakai pot berlubang dan media porous (pasir + sekam) supaya akar tidak busuk.',
   'LIME', 90, false),

  ('Calathea', 'Calathea orbifolia', 'Hias daun', 'Sedang',
   'Teduh, tidak langsung', 'Teduh', 4, 'Tiap 3–5 hari', 'Tinggi',
   'Calathea punya daun lebar bermotif garis yang menutup di malam hari. Cantik untuk sudut ruangan yang teduh, tapi butuh udara lembap supaya ujung daunnya tidak mengering.',
   'Jaga media tetap lembap, jangan sampai becek. Semprot daun atau letakkan dekat wadah air untuk menambah kelembapan.',
   'GOLD', 85, false),

  ('Lidah mertua', 'Sansevieria trifasciata', 'Hias daun', 'Mudah',
   'Teduh hingga terang', 'Teduh–terang', 14, 'Tiap 2–3 minggu', 'Rendah',
   'Lidah mertua berdaun tegak dan kaku, terkenal sangat tahan banting. Bisa hidup di ruangan minim cahaya dan dikenal membantu menyegarkan udara di dalam rumah.',
   'Siram saat media benar-benar kering, lebih jarang saat musim hujan. Kelebihan air adalah penyebab utama tanaman ini mati.',
   'TEAL', 80, true),

  ('Pothos', 'Epipremnum aureum', 'Hias daun', 'Mudah',
   'Teduh, tidak langsung', 'Teduh', 5, 'Tiap 5–7 hari', 'Sedang',
   'Pothos atau sirih gading adalah tanaman merambat yang cepat tumbuh. Cocok digantung atau dirambatkan di rak, dan tetap segar walau cahaya ruangan terbatas.',
   'Siram saat 2–3 cm media atas kering. Pangkas sulur yang terlalu panjang supaya tumbuh lebih lebat.',
   'LIME', 75, true),

  ('Aglaonema', 'Aglaonema commutatum', 'Hias daun', 'Mudah',
   'Teduh hingga terang', 'Teduh–terang', 7, 'Tiap 7 hari', 'Sedang',
   'Aglaonema atau sri rejeki punya daun bercorak hijau, perak, sampai merah muda. Tanaman ini toleran terhadap cahaya rendah dan cocok untuk meja kerja.',
   'Siram saat media atas mulai kering. Hindari angin AC langsung dan bersihkan debu di daun dengan kain lembap.',
   'GREEN', 70, true),

  ('Lidah buaya', 'Aloe vera', 'Sukulen', 'Mudah',
   'Terang, sinar pagi', 'Terang', 14, 'Tiap 2 minggu', 'Rendah',
   'Lidah buaya adalah sukulen berdaun tebal berisi gel yang sering dipakai untuk perawatan kulit. Mudah diperbanyak lewat anakan yang tumbuh di sekitarnya.',
   'Letakkan di tempat yang kena sinar pagi. Siram saat media kering total dan pastikan pot punya lubang drainase.',
   'LIME', 65, false),

  ('Anggrek bulan', 'Phalaenopsis amabilis', 'Berbunga', 'Sedang',
   'Terang, tidak langsung', 'Terang', 7, 'Tiap 5–7 hari', 'Tinggi',
   'Anggrek bulan adalah puspa pesona Indonesia dengan bunga putih yang bisa bertahan berminggu-minggu. Akarnya menempel di media seperti kulit kayu atau moss, bukan tanah.',
   'Siram saat media mulai kering, jangan sampai air menggenang di pangkal daun. Letakkan di tempat terang tanpa sinar langsung.',
   'GOLD', 60, true),

  ('Peace lily', 'Spathiphyllum wallisii', 'Berbunga', 'Mudah',
   'Teduh, tidak langsung', 'Teduh', 5, 'Tiap 5–7 hari', 'Sedang',
   'Peace lily berbunga putih anggun dan daunnya hijau mengkilap. Tanaman ini memberi tanda saat haus: daunnya akan terkulai dan kembali segar setelah disiram.',
   'Siram saat daun mulai sedikit layu atau media atas kering. Cocok di ruangan teduh, jauh dari sinar matahari langsung.',
   'GREEN', 55, false),

  ('Echeveria', 'Echeveria elegans', 'Sukulen', 'Sedang',
   'Terang, sinar langsung', 'Terang', 10, 'Tiap 10–14 hari', 'Rendah',
   'Echeveria adalah sukulen berbentuk roset seperti bunga mawar. Warnanya bisa berubah lebih cerah saat mendapat cukup sinar matahari.',
   'Siram di media, bukan di atas roset, supaya daun tidak busuk. Butuh banyak cahaya agar bentuknya tetap rapat.',
   'TEAL', 50, false),

  ('Anthurium', 'Anthurium andraeanum', 'Berbunga', 'Sedang',
   'Terang, tidak langsung', 'Terang', 5, 'Tiap 4–6 hari', 'Tinggi',
   'Anthurium punya "bunga" merah mengkilap berbentuk hati yang sebenarnya adalah daun pelindung. Bisa berbunga sepanjang tahun kalau cahayanya cukup.',
   'Jaga media lembap tapi tidak becek. Beri pupuk ringan sebulan sekali agar rajin berbunga.',
   'GOLD', 45, false);
