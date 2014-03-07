package app.android.homeBase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
    private parseBase parse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse = new parseBase(this);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void loginOnClick(View v)
    {
        EditText usernameText= (EditText) findViewById(R.id.login_username_etext);
        EditText passwordText = (EditText) findViewById(R.id.login_password_etext);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(!username.isEmpty() && !password.isEmpty())
        {

        }
        else
        {
            Toast.makeText(v.getContext(), "Please enter your username and password", Toast.LENGTH_LONG).show();
        }
    }
}
