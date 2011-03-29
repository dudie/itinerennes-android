CREATE TABLE "markers" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "id" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "label" TEXT NOT NULL,
    "lon" INTEGER NOT NULL,
    "lat" INTEGER NOT NULL
);
CREATE UNIQUE INDEX "markers_idx" on markers (type ASC, id ASC);
CREATE INDEX "markers_type_idx" on markers (type ASC);

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