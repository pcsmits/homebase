package app.android.homeBase;


import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.parse.*;

public abstract class HomeBaseActivity extends ActionBarActivity implements HomeBaseCallback{
    private HomeBaseEventNotifier eventHandler;

    public void OnParseSuccess()
    {

    }

    public void OnParseSuccess(ParseObject parseObject)
    {

    }

    public void OnParseSuccess(ParseObject parseObject, ParseException e)
    {

    }

    public void OnParseSuccess(ParseObject parseObject, ParseException e, HomeBaseCallback callback)
    {

    }

    public void homeBaseCallbackAction()
    {


    }

    public void onSaveSuccess()
    {

    }

    public void onSaveSuccess(Object saved)
    {

    }

    public void onSaveError(String error)
    {
        Toast.makeText(this, "Parse error: "+error, Toast.LENGTH_LONG).show();
    }
}
