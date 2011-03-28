DROP TABLE IF EXISTS "markers";

DROP TABLE IF EXISTS "bike_stations";
DROP INDEX IF EXISTS "bike_station_id_idx";
DROP INDEX IF EXISTS "bike_station_lon_idx";
DROP INDEX IF EXISTS "bike_station_lat_idx";

DROP TABLE IF EXISTS "subway_stations";
DROP INDEX IF EXISTS "subway_station_id_idx";
DROP INDEX IF EXISTS "subway_station_lon_idx";
DROP INDEX IF EXISTS "subway_station_lat_idx";

DROP TABLE IF EXISTS "bookmarks";
DROP INDEX IF EXISTS "bookmarks_idx";

DROP TABLE IF EXISTS "accessibility";
DROP INDEX IF EXISTS "accessibility_idx";


-- suppression des anciennes tables
-- ce code devrait être supprimé plus tard
DROP TABLE IF EXISTS "bus_stations";
DROP INDEX IF EXISTS "bus_stations_id_idx";
DROP INDEX IF EXISTS "bus_stations_lon_idx";
DROP INDEX IF EXISTS "bus_stations_lat_idx";
DROP TABLE IF EXISTS "cache_metadata";
DROP INDEX IF EXISTS "index_unique_type_id";
DROP TABLE IF EXISTS "line_icons";
DROP TABLE IF EXISTS "index_unique_line_id";
DROP TABLE IF EXISTS "geo_explore";
DROP INDEX IF EXISTS "geo_explore_coords_idx";