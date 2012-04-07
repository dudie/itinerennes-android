CREATE TABLE "routesstops" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "route_id" TEXT NOT NULL,
    "stop_id" TEXT NOT NULL
);
CREATE UNIQUE INDEX "routesstops_idx" on routesstops (route_id ASC, stop_id ASC);
CREATE INDEX "routesstops_route_idx" on routesstops (route_id ASC);
CREATE INDEX "routesstops_stop_idx" on routesstops (stop_id ASC);