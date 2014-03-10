package app.android.homeBase;


import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

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
}
