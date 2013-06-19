select distinct 
    stop_times.arrival_time,
    stop_times.departure_time,
    stop_times.trip_id,
    trips.trip_headsign,
    trips.service_id,
    routes.route_id,
    routes.agency_id,
    routes.route_short_name,
    routes.route_long_name,
    routes.route_desc,
    routes.route_type,
    routes.route_color,
    routes.route_text_color
from stop_times
left join trips on stop_times.trip_id = trips.trip_id
left join calendar on trips.service_id = calendar.service_id
left join calendar_dates on trips.service_id = calendar_dates.service_id
left join routes on trips.route_id = routes.route_id
where
    stop_times.stop_id= :stopId
    and 
    (
        (
            calendar.monday = 1
            and calendar.start_date <= :date
            and calendar.end_date >= :date
            and not (calendar_dates.exception_type = 2 and calendar_dates.date = :date)
        )
        or
        (
            calendar_dates.exception_type = 1 and calendar_dates.date = :date
        )
    );