package fr.itinerennes.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import fr.itinerennes.R;
import fr.itinerennes.commons.utils.StringUtils;

public class GtfsDataReader implements IDataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GtfsDataReader.class);

    private final String tableName;
    private final BufferedReader source;
    private final String[] columns;
    private final int lineCount;

    private String nextLine;

    public GtfsDataReader(final Context context, final int asset, final String targetTable) {
        this.tableName = targetTable;
        BufferedReader source = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(asset)));

        try {
            final String firstLine = source.readLine();
            if (null != firstLine) {
                columns = firstLine.split(",");
            } else {
                throw new RuntimeException("GTFS file is empty");
            }

            int count = 0;
            while (null != source.readLine()) {
                count++;
            }
            lineCount = count;

            // reopen file from begining and skip first line (headers)
            source.close();
            source = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(asset)));
            source.readLine();

            this.source = source;
            nextLine = source.readLine();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to initialize the GTFS reader", e);
        }
    }

    @Override
    public boolean hasNext() {
        return null != nextLine || !StringUtils.isBlank(nextLine);
    }

    @Override
    public String[] next() {
        final String[] current = nextLine.split(","); // (","|")
        try {
            nextLine = source.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read GTFS data", e);
        }
        return current;
    }

    @Override
    public void remove() {

        throw new UnsupportedOperationException("Can't remove data from initial dataset");
    }

    @Override
    public int getRowCount() {
        return lineCount;
    }

    @Override
    public String getTable() {
        return tableName;
    }

    @Override
    public String[] getColumns() {
        return columns;
    }

    public static GtfsDataReader agency(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_agency, "agency");
    }

    public static GtfsDataReader calendar(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_calendar, "calendar");
    }

    public static GtfsDataReader calendarDates(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_calendar_dates, "calendar_dates");
    }

    public static GtfsDataReader feedInfo(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_feed_info, "feed_info");
    }

    public static GtfsDataReader routes(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_routes, "routes");
    }

    public static GtfsDataReader stopTimes(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_stop_times, "stop_times");
    }

    public static GtfsDataReader stops(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_stops, "stops");
    }

    public static GtfsDataReader trips(final Context context) {
        return new GtfsDataReader(context, R.raw.gtfs_trips, "trips");
    }
}
