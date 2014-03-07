package app.android.homeBase;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import static android.widget.Toast.LENGTH_LONG;

public class GPSservice extends Service implements LocationListener {
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                60000,   // 3 sec
                10, this);
    }


    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {

        String str = "Latitude: "+location.getLatitude()+"Longitude: "+location.getLongitude();

        Toast.makeText(getBaseContext(), str, LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

        /******** Called when User off Gps *********/

        Toast.makeText(getBaseContext(), "Gps turned off ", LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        /******** Called when User on Gps  *********/

        Toast.makeText(getBaseContext(), "Gps turned on ", LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
