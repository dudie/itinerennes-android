package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.itinerennes.R;

/**
 * This activity uses the <code>bus_station.xml</code> layout and displays a window with
 * informations about a bus station.
 * 
 * @author Jérémie Huchet
 */
public class BusStationActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationActivity.class);

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_station);

        final TextView name = (TextView) findViewById(R.station.name);
        name.setText("Nom de l'arrêt de bus ");// super méga troniquement long");

        final ViewGroup lineList = (ViewGroup) findViewById(R.station.line_icon_list);
        for (int i = 0; i < 1; i++) {
            for (final int img : new int[] { R.drawable.tmp_lm1, R.drawable.tmp_lm08,
                    R.drawable.tmp_lm9 }) {
                final ImageView lineIcon = (ImageView) getLayoutInflater().inflate(
                        R.layout.line_icon, null);
                lineIcon.setImageDrawable(getBaseContext().getResources().getDrawable(img));
                lineList.addView(lineIcon);
            }
        }

        final LinearLayout listTimes = (LinearLayout) findViewById(R.station.list_bus);
        for (final String s : new String[] { "aaaaa", "bbbbb", "ccccc", "ddddd", "eeeee", "fffff",
                "ggggg", "hhhhh", "iiiii", "jjjjj", "kkkkk", "lllll", "mmmmm", "nnnnn", "ooooo" }) {
            final TextView time = (TextView) getLayoutInflater().inflate(R.layout.bus_time, null);
            time.setText(s);
            listTimes.addView(time);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }
}
