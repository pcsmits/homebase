package app.android.homeBase;


import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import com.parse.*;

public abstract class HomeBaseActivity extends ActionBarActivity{
    private HomeBaseEventNotifier eventHandler;

    public void onLoginSuccess()
    {

    }

    public void onLoginError()
    {

    }

    public void onSignupSuccess()
    {

    }

    public void onSignupError()
    {

    }

    public void onParseSuccess()
    {

    }

    public void onParseSuccess(ParseObject parseObject)
    {

    }

    public void onParseSuccess(ParseObject parseObject, ParseException e)
    {

    }

    public void onParseSuccess(ParseObject parseObject, ParseException e, HomeBaseCallback callback)
    {

    }


}
