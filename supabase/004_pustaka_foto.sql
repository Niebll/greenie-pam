-- =====================================================================
-- 004 — Foto tanaman di Pustaka + katalog dirapikan jadi 10 tanaman
-- Jalankan SETELAH 003_pustaka.sql: Supabase Dashboard → SQL Editor → Run.
--
-- Foto dari Wikimedia Commons (lisensi bebas, CC BY-SA / public domain).
-- Untuk ganti foto: Table Editor → plant_library → kolom image_url.
-- =====================================================================

alter table public.plant_library add column image_url text;

-- Katalog dirapikan jadi 10 tanaman (Aglaonema dikeluarkan).
-- Tanaman koleksi yang berasal dari Aglaonema tetap ada; hanya tautannya ke katalog dilepas.
delete from public.plant_library where name = 'Aglaonema';

update public.plant_library as p
set image_url = v.image_url,
    latin_name = coalesce(v.latin_name, p.latin_name)
from (values
  ('Monstera',      null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/2/2e/Monstera_deliciosa2.jpg/960px-Monstera_deliciosa2.jpg'),
  ('Calathea',      null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/fa/Calathea_orbifolia_2.jpg/960px-Calathea_orbifolia_2.jpg'),
  ('Lidah mertua',  null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/fb/Snake_Plant_%28Sansevieria_trifasciata_%27Laurentii%27%29.jpg/960px-Snake_Plant_%28Sansevieria_trifasciata_%27Laurentii%27%29.jpg'),
  ('Pothos',        null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/6/62/Money_Plant_%28Epipremnum_aureum%29_4.jpg/960px-Money_Plant_%28Epipremnum_aureum%29_4.jpg'),
  ('Kaktus',        'Echinocactus grusonii',  'https://thumb.wikimedia.org/wikipedia/commons/thumb/2/25/Echinocactus_grusonii_%28aka%29.jpg/960px-Echinocactus_grusonii_%28aka%29.jpg'),
  ('Lidah buaya',   null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/07/Aloe_vera_plant_in_flower_pot.jpg/960px-Aloe_vera_plant_in_flower_pot.jpg'),
  ('Echeveria',     null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/5d/Echeveria_elegans_-_1.png/960px-Echeveria_elegans_-_1.png'),
  ('Anggrek bulan', null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/59/Phalaenopsis_amabilis_Orchi_198.jpg/960px-Phalaenopsis_amabilis_Orchi_198.jpg'),
  ('Anthurium',     null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/8/8b/AnthuriumAndraenum.jpg/960px-AnthuriumAndraenum.jpg'),
  ('Peace lily',    null,                     'https://thumb.wikimedia.org/wikipedia/commons/thumb/4/49/Peace_lily_-_1_-_cropped.jpg/960px-Peace_lily_-_1_-_cropped.jpg')
) as v(name, latin_name, image_url)
where p.name = v.name;

-- Rekomendasi: satu per kategori + Pothos, supaya tidak semuanya hias daun
update public.plant_library
set is_recommended = name in ('Lidah mertua', 'Pothos', 'Lidah buaya', 'Anggrek bulan');
