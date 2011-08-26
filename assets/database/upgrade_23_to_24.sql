DROP TABLE IF EXISTS bike_stations;
DROP INDEX IF EXISTS bike_station_id_idx;
DROP INDEX IF EXISTS bike_station_lon_idx;
DROP INDEX IF EXISTS bike_station_lat_idx;
DROP TABLE IF EXISTS bus_stations;
DROP INDEX IF EXISTS bus_station_id_idx;
DROP INDEX IF EXISTS bus_station_lon_idx;
DROP INDEX IF EXISTS bus_station_lat_idx;
DROP TABLE IF EXISTS subway_stations;
DROP INDEX IF EXISTS subway_station_id_idx;
DROP INDEX IF EXISTS subway_station_lon_idx;
DROP INDEX IF EXISTS subway_station_lat_idx;
DROP TABLE IF EXISTS bus_routes;
DROP INDEX IF EXISTS bus_route_id_idx;
DROP INDEX IF EXISTS bus_route_station_idx;
DROP TABLE IF EXISTS cache_metadata;
DROP INDEX IF EXISTS index_unique_type_id;
DROP TABLE IF EXISTS geo_explore;
DROP INDEX IF EXISTS geo_explore_coords_idx;
DROP TABLE IF EXISTS line_icons;
DROP INDEX IF EXISTS index_unique_line_id;
DROP TABLE IF EXISTS accessibility;
DROP INDEX IF EXISTS accessibility_idx;

CREATE TABLE "markers" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "label" TEXT NOT NULL,
    "lon" INTEGER NOT NULL,
    "lat" INTEGER NOT NULL,
    "search_label" TEXT NOT NULL
);
CREATE UNIQUE INDEX "markers_idx" on markers (type ASC, id ASC);
CREATE INDEX "markers_type_idx" on markers (type ASC);

CREATE TABLE "accessibility" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "wheelchair" BOOLEAN NULL
);
CREATE UNIQUE INDEX "accessibility_idx" on accessibility (id ASC);

UPDATE bookmarks SET TYPE='BUS' WHERE type='fr.itinerennes.model.BusStation';
UPDATE bookmarks SET TYPE='BIKE' WHERE type='fr.itinerennes.model.BikeStation';
UPDATE bookmarks SET TYPE='SUBWAY' WHERE type='fr.itinerennes.model.SubwayStation';