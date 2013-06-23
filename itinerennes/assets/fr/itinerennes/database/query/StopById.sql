select
    stop_id,
    stop_code,
    stop_name,
    stop_lat,
    stop_lon
from markers
where 
        type = 'BUS'
    AND id = :stopId
