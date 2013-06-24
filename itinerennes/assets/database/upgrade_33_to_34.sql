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
    "trip_id" KEY NOT NULL,
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
