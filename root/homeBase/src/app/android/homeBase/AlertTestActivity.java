package app.android.homeBase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class AlertTestActivity extends HomeBaseActivity {
    public ParseBase parse;
    public HomeBaseAlert testAlert;

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
        if(!testAlert.getId().isEmpty())
        {
            parse.getAlert(testAlert.getId(), AlertTestActivity.this);
        }
    }

    public void updateOnClick(View v)
    {
        testAlert.addSeen("Kyle");
        testAlert.setCreatorID("asjdalskjda");
        testAlert.setDescription("updated description");
        parse.updateAlert(testAlert, AlertTestActivity.this);
    }

    public void deleteOnClick(View v)
    {
        parse.deleteAlert(testAlert, AlertTestActivity.this);
    }

    @Override
    public void onCreateAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(AlertTestActivity.this, alert.getId(), Toast.LENGTH_LONG).show();
        testAlert = alert;
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

    @Override
    public void onUpdateAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(AlertTestActivity.this, alert.getId()+" was updated!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdateAlertFailure(String e)
    {
        Toast.makeText(AlertTestActivity.this, e, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteAlertSuccess()
    {
        Toast.makeText(AlertTestActivity.this, "Deleted!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteAlertFailure(String e)
    {
        Toast.makeText(AlertTestActivity.this, e, Toast.LENGTH_LONG).show();
    }
}
