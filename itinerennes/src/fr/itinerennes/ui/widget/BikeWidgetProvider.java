package fr.itinerennes.ui.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import fr.dudie.keolis.model.BikeStation;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;

/**
 * Provider to update the bike widget.
 * 
 * @author Olivier Boudet
 */
public class BikeWidgetProvider extends AppWidgetProvider {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BikeWidgetProvider.class);

    /** Intent name for widget update. */
    public static final String WIDGET_UPDATE = "fr.itinerennes.intent.BIKE_WIDGET_UPDATE";

    /** Pending intent used by AlarmManager. */
    private static PendingIntent pendingIntent;

    /**
     * {@inheritDoc}
     * 
     * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context,
     *      android.appwidget.AppWidgetManager, int[])
     */
    @Override
    public final void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
            final int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < appWidgetIds.length; i++) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Updating bike widget. id = {}", appWidgetIds[i]);
            }

            final int appWidgetId = appWidgetIds[i];

            final Intent serviceIntent = new Intent(context, UpdateService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            context.startService(serviceIntent);

        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context,
     *      android.content.Intent)
     */
    @Override
    public final void onReceive(final Context context, final Intent intent) {

        super.onReceive(context, intent);

        if (WIDGET_UPDATE.equals(intent.getAction())) {

            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            final ComponentName bikeWidget = new ComponentName(context.getPackageName(),
                    BikeWidgetProvider.class.getName());
            final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(bikeWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.appwidget.AppWidgetProvider#onDeleted(android.content.Context, int[])
     */
    @Override
    public final void onDeleted(final Context context, final int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            BikeWidgetConfigurationWizardActivity.deletePref(context, appWidgetIds[i]);
        }
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.appwidget.AppWidgetProvider#onDisabled(android.content.Context)
     */
    @Override
    public final void onDisabled(final Context context) {

        super.onDisabled(context);

        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(pendingIntent);
    }

    /**
     * Saves the alarm manager pending intent to future use in
     * {@link BikeWidgetProvider#onDisabled(Context)}.
     * 
     * @param intent
     *            intent to save
     */
    public static void savePendingIntent(final PendingIntent intent) {

        // TOBO un meilleur moyen de faire ça ? actuellement ca sert pour pouvoir faire le cancel
        // dans onDisabled.
        pendingIntent = intent;
    }

    /**
     * Service used to fetch bike stations informations from network and update the widget views.
     * 
     * @author Olivier Boudet
     */
    public static final class UpdateService extends IntentService {

        /**
         * Constructor.
         */
        public UpdateService() {

            super(BikeWidgetProvider.class.getName());
        }

        /**
         * Constructor.
         * 
         * @param name
         *            the name the worker thread
         */
        public UpdateService(final String name) {

            super(BikeWidgetProvider.class.getName());
        }

        /**
         * {@inheritDoc}
         * 
         * @see android.app.IntentService#onHandleIntent(android.content.Intent)
         */
        @Override
        protected void onHandleIntent(final Intent intent) {

            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                final String stationIdsString = BikeWidgetConfigurationWizardActivity.loadPref(
                        this, appWidgetId);

                if (stationIdsString != null) {
                    final String[] stationIds = stationIdsString.split(";");

                    if (stationIds.length > 0) {
                        final List<BikeStation> stationsList = getBikeStations(this, stationIds);
                        final RemoteViews views = getUpdatedViews(this, stationsList);

                        // Tell the widget manager
                        AppWidgetManager.getInstance(this).updateAppWidget(appWidgetId, views);
                    }
                }

            }

        }

        /**
         * Load the list of bike stations from preferences and fetch updated informations from
         * network.
         * 
         * @param context
         *            the context
         * @param stationIds
         *            the list of bike stations to get
         * @return the list of updated BikeStation
         */
        private List<BikeStation> getBikeStations(final Context context, final String[] stationIds) {

            final List<BikeStation> bikeStations = new ArrayList<BikeStation>();

            // fetch information from keolis for each bike station saved in preferences
            for (int j = 0; j < stationIds.length; j++) {

                try {

                    // TOBO ici on fait autant de requêtes qu'on a de stations. Voir pour faire
                    // un appel a getAllBikeStations si nécessaire.
                    final BikeStation bikeStation = ((ItineRennesApplication) context
                            .getApplicationContext()).getKeolisClient().getBikeStation(
                            stationIds[j]);
                    if (bikeStation != null) {
                        bikeStations.add(bikeStation);
                    }

                } catch (final IOException e) {
                    LOGGER.error("onUpdate : can't get bike station from keolis. {}",
                            e.getMessage());
                }

            }

            return bikeStations;

        }

        /**
         * Builds an updated RemoteViews with given bike stations.
         * 
         * @param context
         *            the context
         * @param bikeStations
         *            the list of bike stations to show
         * @return an updated {@link RemoteViews}
         */
        private RemoteViews getUpdatedViews(final Context context,
                final List<BikeStation> bikeStations) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("buildUpdatedViews() for {} bike stations", bikeStations.size());
            }

            final RemoteViews allViews = new RemoteViews(context.getPackageName(),
                    R.layout.wgt_container);

            // removing all child view before adding new ones
            allViews.removeAllViews(R.widget.container);

            for (final BikeStation bikeStation : bikeStations) {

                final RemoteViews bikeView = new RemoteViews(context.getPackageName(),
                        R.layout.wgt_bike);

                bikeView.setTextViewText(R.widget_bike.title, bikeStation.getName());
                bikeView.setProgressBar(R.widget_bike.bike_station_gauge,
                        bikeStation.getAvailableBikes() + bikeStation.getAvailableSlots(),
                        bikeStation.getAvailableBikes(), false);
                bikeView.setTextViewText(R.widget_bike.available_bikes,
                        String.valueOf(bikeStation.getAvailableBikes()));
                bikeView.setTextViewText(R.widget_bike.available_slots,
                        String.valueOf(bikeStation.getAvailableSlots()));

                allViews.addView(R.widget.container, bikeView);

            }

            return allViews;
        }

    }

}
