CREATE TABLE "markers" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "label" TEXT NOT NULL,
    "lon" INTEGER NOT NULL,
    "lat" INTEGER NOT NULL
);

CREATE TABLE "bike_stations" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "lon" INTEGER NOT NULL,
    "lat" INTEGER NOT NULL,
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

CREATE TABLE "subway_stations" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "lon" INTEGER NOT NULL,
    "lat" INTEGER NOT NULL,
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

CREATE TABLE "bookmarks" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "label" TEXT NOT NULL,
    "type" TEXT NOT NULL, 
    "id" TEXT NOT NULL
);
CREATE UNIQUE INDEX "bookmarks_idx" on bookmarks (type ASC, id ASC);

CREATE TABLE "accessibility" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "wheelchair" BOOLEAN NULL
);
CREATE UNIQUE INDEX "accessibility_idx" on accessibility (id ASC);