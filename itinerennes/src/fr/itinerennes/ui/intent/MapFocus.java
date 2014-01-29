package fr.itinerennes.ui.intent;

import org.osmdroid.util.GeoPoint;

import fr.itinerennes.ui.views.MapViewController;
import android.os.Parcel;
import android.os.Parcelable;

public class MapFocus implements Parcelable {

    private final int zoom;
    private final int lon;
    private final int lat;

    public MapFocus(final int zoom, final int lon, final int lat) {
        this.zoom = zoom;
        this.lon = lon;
        this.lat = lat;
    }

    public void setFocus(MapViewController controller) {
        controller.setZoom(zoom);
        controller.setCenter(new GeoPoint(lat, lon));
    }

    @Override
    public int describeContents() {
        return 3;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(zoom);
        dest.writeInt(lon);
        dest.writeInt(lat);
    }

   public static final Creator<MapFocus> CREATOR = new Creator<MapFocus>() {

        @Override
        public MapFocus createFromParcel(final Parcel source) {
            final int zoom = source.readInt();
            final int lon = source.readInt();
            final int lat = source.readInt();
            return new MapFocus(zoom, lon, lat);
        }

        @Override
        public MapFocus[] newArray(int size) {
            return new MapFocus[size];
        }
    };
}
