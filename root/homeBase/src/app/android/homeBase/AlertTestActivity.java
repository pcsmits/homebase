package app.android.homeBase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class AlertTestActivity extends HomeBaseActivity {
    public ParseBase parse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_test);
        parse = new ParseBase(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alert_test, menu);
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

    public void newOnClick(View v)
    {
        String type = "Grocery";
        String desc = "We need milk";
        String owner = "Kyle";
        String creator = "Parker";
        parse.createAlert(type, desc, owner, creator, AlertTestActivity.this);
    }

    public void getOnClick(View v)
    {
        String id = "h1PNOE1Twd";
        parse.getAlert(id,AlertTestActivity.this);
    }

    @Override
    public void onCreateAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(AlertTestActivity.this, alert.getId(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateAlertFailure(String e)
    {
        Toast.makeText(AlertTestActivity.this, e, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(AlertTestActivity.this, alert.getId(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetAlertFailure(String e)
    {
        Toast.makeText(AlertTestActivity.this, e, Toast.LENGTH_LONG).show();
    }
}
