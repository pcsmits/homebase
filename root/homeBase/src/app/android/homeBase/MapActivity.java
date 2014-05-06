package app.android.homeBase;


import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FindHouseActivity extends HomeBaseActivity {
    public ParseBase parse;
    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findhouse);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_house, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSubmitHouseClick(View view)
    {
        // Get a list of all the edit texts
        List<EditText> checkList = new ArrayList<EditText>();

        EditText houseNameET = (EditText) findViewById(R.id.newhouse_name_etext);
        checkList.add(houseNameET);

        EditText houseAddressET = (EditText) findViewById(R.id.newhouse_address_etext);
        checkList.add(houseNameET);

        EditText houseCityET = (EditText) findViewById(R.id.newhouse_city_etext);
        checkList.add(houseNameET);

        EditText houseStateET = (EditText) findViewById(R.id.newhouse_state_etext);
        checkList.add(houseNameET);

        EditText houseZipET = (EditText) findViewById(R.id.newhouse_zipcode_etext);
        checkList.add(houseNameET);

        for(EditText item : checkList)
        {
            if(item.length() == 0)
            {
                Toast.makeText(FindHouseActivity.this, "Please fill out the " + item.getHint() + " field", Toast.LENGTH_LONG).show();
                return;
            }
        }

        try
        {
            String name = houseNameET.getText().toString();
            String address = houseAddressET.getText().toString();
            String city = houseCityET.getText().toString();
            String state = houseStateET.getText().toString();
            String zip = houseZipET.getText().toString();
            int zipcode = Integer.parseInt(zip);

            // Find latitude and longitude
            Geocoder gc = new Geocoder(FindHouseActivity.this, Locale.getDefault());

            ParseUser curr = ParseUser.getCurrentUser();
            if (gc.isPresent()) {
                List<Address> list = null;
                try {
                    list = gc.getFromLocationName(address + ", " + zip, 1);
                } catch (IOException E) {
                    Log.d("GeoCoder", "Not a proper address");
                }

                Address fullAddress = list.get(0);

                double lat = fullAddress.getLatitude();
                double lng = fullAddress.getLongitude();
                String gpsString = lat + " - " +lng;
                Log.d("GeoCoder", gpsString);

                parse.createHouse(name, address, city, state, zipcode, curr.getObjectId(), lat, lng, FindHouseActivity.this);
            } else {
                Log.d("GeoCoder", "Not present");
                parse.createHouse(name, address, city, state, zipcode, curr.getObjectId(), 43.0667, 89.4, FindHouseActivity.this);
                Log.d("GeoCoder", "House loctaion set to 0 0");
            }

        } catch (NullPointerException e){
            Toast.makeText(FindHouseActivity.this, "Please fill out all the forms", Toast.LENGTH_LONG).show();
        }

    }

    public void onJoinHouseClick(View view)
    {
        EditText houseCodeET = (EditText) findViewById(R.id.newhouse_joinhouse_etext);
        try
        {
            String code = houseCodeET.getText().toString();
            if(code.length() != 0)
            {
                parse.getHouse(code, FindHouseActivity.this);
            }
            else
            {
                Toast.makeText(FindHouseActivity.this, "Please enter the username of the house admin", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e)
        {
            Toast.makeText(FindHouseActivity.this, "Please enter the username of the house admin", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetHouseSuccess(HomeBaseHouse house)
    {
        house.getMembers().add(ParseUser.getCurrentUser().getObjectId());
        parse.updateHouse(house, FindHouseActivity.this);
    }

    @Override
    public void onGetHouseFailure(String e)
    {
        Toast.makeText(FindHouseActivity.this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateHouseSuccess(HomeBaseHouse house)
    {
        ParseUser.getCurrentUser().put("house", house.getId());
        Intent startFeed = new Intent(FindHouseActivity.this, NewsFeedActivity.class);
        startActivity(startFeed);
    }

    @Override
    public void onUpdateHouseFailure(String e)
    {
        Toast.makeText(FindHouseActivity.this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateHouseSuccess(HomeBaseHouse house)
    {
        ParseUser.getCurrentUser().put("house", house.getId());
        Intent startFeed = new Intent(FindHouseActivity.this, NewsFeedActivity.class);
        startActivity(startFeed);
    }

    @Override
    public void onCreateHouseFailure(String e)
    {
        Toast.makeText(FindHouseActivity.this, e, Toast.LENGTH_SHORT).show();
    }
}
