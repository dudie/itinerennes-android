CREATE TABLE "markers" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "label" TEXT NOT NULL,
    "lon" INTEGER NOT NULL,
    "lat" INTEGER NOT NULL,
    "search_label" TEXT NOT NULL,
    "city" TEXT
);
CREATE UNIQUE INDEX "markers_idx" on markers (type ASC, id ASC);
CREATE INDEX "markers_type_idx" on markers (type ASC);
CREATE INDEX "markers_search_label_idx" on markers (search_label);
CREATE INDEX "markers_label_idx" on markers (label);

CREATE TABLE "routesstops" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "route_id" TEXT NOT NULL,
    "stop_id" TEXT NOT NULL
);
CREATE UNIQUE INDEX "routesstops_idx" on routesstops (route_id ASC, stop_id ASC);
CREATE INDEX "routesstops_route_idx" on routesstops (route_id ASC);
CREATE INDEX "routesstops_stop_idx" on routesstops (stop_id ASC);

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



CREATE TABLE "agency" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "agency_id" TEXT NOT NULL,
    "agency_name" TEXT NOT NULL,
    "agency_url" TEXT NOT NULL,
    "agency_timezone" TEXT NOT NULL,
    "agency_lang" TEXT,
    "agency_phone" TEXT,
    "agency_fare_url" TEXT
);
CREATE TABLE "stops" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "stop_id" TEXT NOT NULL,
    "stop_code" TEXT,
    "stop_name" TEXT NOT NULL,
    "stop_desc" TEXT,
    "stop_lat" INTEGER NOT NULL,
    "stop_lon" INTEGER NOT NULL,
    "zone_id" TEXT,
    "stop_url" TEXT,
    "location_type" TEXT,
    "parent_station" TEXT,
    "stop_timezone" TEXT,
    "wheelchair_boarding" TEXT
);
CREATE TABLE "routes" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "route_id" TEXT NOT NULL,
    "agency_id" TEXT,
    "route_short_name" TEXT NOT NULL,
    "route_long_name" TEXT NOT NULL,
    "route_desc" TEXT,
    "route_type" INTEGER NOT NULL,
    "route_url" TEXT,
    "route_color" TEXT,
    "route_text_color" TEXT
);
CREATE TABLE "trips" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "route_id" TEXT NOT NULL,
    "service_id" TEXT NOT NULL,
    "trip_id" TEXT NOT NULL,
    "trip_headsign" TEXT,
    "trip_short_name" TEXT,
    "direction_id" INTEGER,
    "block_id" TEXT,
    "shape_id" TEXT,
    "wheelchair_accessible" INTEGER
);
CREATE TABLE "stop_times" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "trip_id" TEXT NOT NULL,
    "arrival_time" INTEGER NOT NULL,
    "departure_time" INTEGER NOT NULL,
    "stop_id" TEXT NOT NULL,
    "stop_sequence" INTEGER NOT NULL,
    "stop_headsign" TEXT,
    "pickup_type" INTEGER,
    "drop_off_type" INTEGER,
    "shape_dist_traveled" INTEGER
);
CREATE TABLE "calendar" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "service_id" TEXT NOT NULL,
    "monday" INTEGER NOT NULL,
    "tuesday" INTEGER NOT NULL,
    "wednesday" INTEGER NOT NULL,
    "thursday" INTEGER NOT NULL,
    "friday" INTEGER NOT NULL,
    "saturday" INTEGER NOT NULL,
    "sunday" INTEGER NOT NULL,
    "start_date" INTEGER NOT NULL,
    "end_date" INTEGER NOT NULL
);
CREATE TABLE "calendar_dates" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "service_id" TEXT NOT NULL,
    "date" INTEGER NOT NULL,
    "exception_type" INTEGER NOT NULL
);
CREATE TABLE "feed_info" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "feed_publisher_name" TEXT NOT NULL,
    "feed_publisher_url" TEXT NOT NULL,
    "feed_lang" TEXT NOT NULL,
    "feed_start_date" INTEGER,
    "feed_end_date" INTEGER,
    "feed_version" TEXT
);
