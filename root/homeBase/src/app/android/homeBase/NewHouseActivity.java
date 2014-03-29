package app.android.homeBase;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NewHouseActivity extends HomeBaseActivity {
    public ParseBase parse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_house);
        parse = new ParseBase();
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

    @Override
    public void onSaveSuccess(Object saved)
    {
        House house = (House) saved;
        Intent startFeed = new Intent(NewHouseActivity.this, NewsFeedActivity.class);
        startActivity(startFeed);
    }

    public void onSubmitHouseClick(View view) {

        EditText houseNameET = (EditText) findViewById(R.id.newhouse_name_etext);
        EditText houseAddressET = (EditText) findViewById(R.id.newhouse_address_etext);
        EditText houseCityET = (EditText) findViewById(R.id.newhouse_city_etext);
        EditText houseStateET = (EditText) findViewById(R.id.newhouse_state_etext);
        EditText houseZipET = (EditText) findViewById(R.id.newhouse_zipcode_etext);

        try {
            String name = houseNameET.getText().toString();
            String address = houseAddressET.getText().toString();
            String city = houseCityET.getText().toString();
            String state = houseStateET.getText().toString();
            String zip = houseZipET.getText().toString();
            int zipcode = Integer.parseInt(zip);

            // Find latitude and longitude
            Geocoder gc = new Geocoder(NewHouseActivity.this, Locale.getDefault());
           // Geocoder gc = new Geocoder (NewHouseActivity.this);

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

                parse.createHouse(name, address, city, state, zipcode, lat, lng, NewHouseActivity.this);
            } else {
                Log.d("GeoCoder", "Not present");
            }

        } catch (Exception e) {
            Toast.makeText(NewHouseActivity.this, "Please fill out all of the registration fields correctly", Toast.LENGTH_LONG).show();
        }
    }

    public void onJoinHouseClick(View view)
    {
        EditText houseCodeET = (EditText) findViewById(R.id.newhouse_code_etext);
        try {
            String code = houseCodeET.getText().toString();
            Intent startFeed = new Intent(NewHouseActivity.this, NewsFeedActivity.class);
            startActivity(startFeed);
        } catch (Exception e) {
            Toast.makeText(NewHouseActivity.this, "Please fill out the code field", Toast.LENGTH_LONG).show();
        }
    }
}
