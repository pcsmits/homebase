package app.android.homeBase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

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
                Toast.makeText(NewHouseActivity.this, "Please fill out the "+item.getHint()+" field", Toast.LENGTH_LONG).show();
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
            ParseUser curr = ParseUser.getCurrentUser();
            parse.createHouse(name, address, city, state, zipcode, curr.getObjectId(), NewHouseActivity.this);

        } catch (NullPointerException e){
            Toast.makeText(NewHouseActivity.this, "Please fill out all the forms", Toast.LENGTH_LONG).show();
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
                parse.getHouse(code, NewHouseActivity.this);
            }
            else
            {
                Toast.makeText(NewHouseActivity.this, "Plase enter the username of the house admin", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e)
        {
            Toast.makeText(NewHouseActivity.this, "Please enter the username of the house admin", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetHouseSuccess(HomeBaseHouse house)
    {
        house.getMembers().add(ParseUser.getCurrentUser().getObjectId());
        parse.updateHouse(house, NewHouseActivity.this);
    }

    @Override
    public void onGetHouseFailure(String e)
    {
        Toast.makeText(NewHouseActivity.this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateHouseSuccess(HomeBaseHouse house)
    {
        Intent startFeed = new Intent(NewHouseActivity.this, NewsFeedActivity.class);
        startActivity(startFeed);
    }

    @Override
    public void onUpdateHouseFailure(String e)
    {
        Toast.makeText(NewHouseActivity.this, e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateHouseSuccess(HomeBaseHouse saved)
    {
        Intent startFeed = new Intent(NewHouseActivity.this, NewsFeedActivity.class);
        startActivity(startFeed);
    }

    @Override
    public void onCreateHouseFailure(String e)
    {
        Toast.makeText(NewHouseActivity.this, e, Toast.LENGTH_SHORT).show();
    }
}
