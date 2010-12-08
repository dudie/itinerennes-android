CREATE TABLE "bike_stations" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "lon" REAL NOT NULL,
    "lat" REAL NOT NULL,
    "street_name" TEXT NOT NULL,
    "is_active" INTEGER NOT NULL,
    "avail_slots" INTEGER NOT NULL,
    "avail_bikes" INTEGER NOT NULL,
    "is_pos" INTEGER NOT NULL,
    "district_name" TEXT NOT NULL,
    "last_update" INTEGER NOT NULL
);
CREATE UNIQUE INDEX "bike_station_id_idx" on bike_stations (id ASC);
CREATE INDEX "bike_station_lon_idx" on bike_stations (lon ASC);
CREATE INDEX "bike_station_lat_idx" on bike_stations (lat ASC);
CREATE TABLE "bus_stations" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "lon" REAL NOT NULL,
    "lat" REAL NOT NULL
);
CREATE UNIQUE INDEX "bus_station_id_idx" on bus_stations (id ASC);
CREATE INDEX "bus_station_lon_idx" on bus_stations (lon ASC);
CREATE INDEX "bus_station_lat_idx" on bus_stations (lat ASC);
CREATE TABLE "subway_stations" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "lon" REAL NOT NULL,
    "lat" REAL NOT NULL,
    "has_pf_dir_1" INTEGER NOT NULL,
    "has_pf_dir_2" INTEGER NOT NULL,
    "rank_pf_dir_1" INTEGER NOT NULL,
    "rank_pf_dir_2" INTEGER NOT NULL,
    "floors" INTEGER NOT NULL,
    "last_update" INTEGER NOT NULL,
    "status" INTEGER
);
CREATE UNIQUE INDEX "subway_station_id_idx" on subway_stations (id ASC);
CREATE INDEX "subway_station_lon_idx" on subway_stations (lon ASC);
CREATE INDEX "subway_station_lat_idx" on subway_stations (lat ASC);
CREATE TABLE "cache_metadata" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "type" TEXT NOT NULL,
    "id" TEXT NOT NULL,
    "last_update" INTEGER
);
CREATE UNIQUE INDEX "index_unique_type_id" on cache_metadata (type ASC, id ASC);
CREATE TABLE geo_explore (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "lon_west" INTEGER NOT NULL,
    "lat_north" INTEGER NOT NULL,
    "lon_east" INTEGER NOT NULL,
    "lat_south" INTEGER NOT NULL,
    "last_update" INTEGER NOT NULL,
    "type" TEXT NOT NULL
);
CREATE UNIQUE INDEX "geo_explore_coords_idx" on geo_explore (lon_west ASC, lat_north ASC, lon_east ASC, lat_south ASC);
