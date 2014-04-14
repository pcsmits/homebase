package app.android.homeBase;

import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.parse.*;

public abstract class HomeBaseActivity extends ActionBarActivity{

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

    public void onGetHouseSuccess(HomeBaseHouse house)
    {

    }
    public void onGetHouseFailure(String e)
    {

    }
    public void onCreateHouseSuccess(HomeBaseHouse house)
    {

    }
    public void onCreateHouseFailure(String e)
    {

    }
    public void onUpdateHouseSuccess(HomeBaseHouse house)
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

    public void onGetAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {

    }

    public void onGetAlertListFailure(String e)
    {

    }

    public void onGetAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {

    }

    public void onGetAlertListByTypeFailure(String e)
    {

    }

    public void onGetBillSuccess(HomeBaseBill bill)
    {

    }

    public void onGetBillsSuccess(List<HomeBaseBill> bills)
    {

    }

    public void onGetBillFailure(String e)
    {

    }

    public void onGetBillsFailure(String e)
    {

    }
    public void onCreateBillSuccess(HomeBaseBill bill)
    {

    }
    public void onCreateBillFailure(String e)
    {

    }
    public void onUpdateBillSuccess(HomeBaseBill bill)
    {

    }
    public void onUpdateBillFailure(String e)
    {

    }

    public void onDeleteBillSuccess()
    {

    }
    public void onDeleteBillFailure(String e)
    {

    }

}
