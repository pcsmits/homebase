package app.android.homeBase;


import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.parse.*;

public abstract class HomeBaseActivity extends ActionBarActivity{
    private HomeBaseEventNotifier eventHandler;

    public void onLoginSuccess()
    {

    }

    public void onLoginError()
    {

    }

    public void onSignupSuccess(ParseUser user)
    {

    }

    public void onSignupError(ParseException e)
    {

    }

    public void onCheckUserSuccess()
    {

    }

    public void onCheckUserFailure()
    {

    }

    public void onCheckUserError(ParseException e)
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

    public void onSaveSuccess(Object saved)
    {

    }

    public void onSaveSuccess()
    {

    }

    public void onSaveError(String error)
    {
        Toast.makeText(this, "Parse error: "+error, Toast.LENGTH_LONG).show();
    }



    public void onGetAlertSuccess(HomeBaseAlert alert)
    {

    }
    public void onGetAlertFailure(String e)
    {

    }
    public void onCreateAlertSuccess(HomeBaseAlert alert)
    {

    }
    public void onCreateAlertFailure(String e)
    {

    }
    public void onUpdateAlertSuccess(HomeBaseAlert alert)
    {

    }
    public void onUpdateAlertFailure(String e)
    {

    }

    public void onDeleteAlertSuccess()
    {

    }
    public void onDeleteAlertFailure(String e)
    {

    }

    public void onGetHouseSuccess(House house)
    {

    }
    public void onGetHouseFailure(String e)
    {

    }
    public void onCreateHouseSuccess(House house)
    {

    }
    public void onCreateHouseFailure(String e)
    {

    }
    public void onUpdateHouseSuccess(House house)
    {

    }
    public void onUpdateHouseFailure(String e)
    {

    }

    public void onDeleteHouseSuccess()
    {

    }
    public void onDeleteHouseFailure(String e)
    {

    }
}
