package app.android.homeBase;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class NewHouseActivity extends HomeBaseActivity {

    public parseBase parse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_house);
        parse = new parseBase();
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
        Intent startFeed = new Intent(NewHouseActivity.this, ChoresActivity.class);
        startActivity(startFeed);
    }

    public void onHouseClick(View v)
    {
        String housename = "Name";
        String address = "123 street";
        int zipode = 12345;

        //TODO do google look up address get lat and long if not found drop anchor
        parse.createHouse(housename, address, zipode, NewHouseActivity.this);
    }
}
