-- =====================================================================
-- 005 — Hapus fitur favorit tanaman Pustaka
-- Favorit hanya untuk postingan jurnal (tabel journal_favorites).
-- Jalankan SETELAH 004_pustaka_foto.sql: Supabase Dashboard → SQL Editor → Run.
-- =====================================================================

drop table if exists public.plant_library_favorites;
