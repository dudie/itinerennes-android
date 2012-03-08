DELETE FROM markers;
DELETE FROM accessibility;

DROP TABLE IF EXISTS markers;

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