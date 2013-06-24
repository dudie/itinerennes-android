select 
  st.arrival_time,
  st.departure_time,
  st.route_id,
  t.service_id,
  t.trip_id

from
  stop_times st

right outer join
  calendar c
  on st.service_id = c.service_id

right outer join
  trips t
  on st.trip_id = t.trip_id

where
      c.start_date < :now
  and c.end_date > :now
  and st.stop_id = :id
  and st.departure_time > :today
  and st.departure_time < :tomorrow