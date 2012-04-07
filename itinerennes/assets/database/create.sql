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