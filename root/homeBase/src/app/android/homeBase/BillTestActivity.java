package app.android.homeBase;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class BillTestActivity extends HomeBaseActivity {
    public ParseBase parse;
    public HomeBaseBill testBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_test);
        parse = new ParseBase(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bill_test, menu);
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

    public void billCreateOnClick(View v)
    {
        List<String> responsibleUsers = new LinkedList<String>();
        responsibleUsers.add("Parker");
        responsibleUsers.add("Emanuel");
        parse.createBill("test bill", "a test bill", responsibleUsers, 10.0, ParseUser.getCurrentUser().getObjectId(), BillTestActivity.this );
    }

    public void onCreateBillSuccess(HomeBaseBill bill)
    {
        testBill = bill;
        Toast.makeText(BillTestActivity.this, "Bill made "+testBill.getId(), Toast.LENGTH_LONG).show();
    }
    public void onCreateBillFailure(String e)
    {
        Toast.makeText(BillTestActivity.this, e, Toast.LENGTH_LONG).show();
    }

    public void billGetOnClick(View v)
    {
        parse.getAlert(testBill.getId(), BillTestActivity.this);
    }

    public void onGetAlertSuccess(HomeBaseBill bill)
    {
        Toast.makeText(BillTestActivity.this, bill.getId()+" vs "+testBill.getId(), Toast.LENGTH_LONG).show();
    }
    public void onGetAlertFailure(String e)
    {
        Toast.makeText(BillTestActivity.this, e, Toast.LENGTH_LONG).show();
    }

    public void billUpdateOnClick(View v)
    {
        if(testBill.clearResponsibility("Parker")) {
            parse.updateBill(testBill, BillTestActivity.this);
        }
    }

    public void onUpdateBillSuccess(HomeBaseBill bill)
    {
        Toast.makeText(BillTestActivity.this, testBill.getResponsibleUsers().get(0), Toast.LENGTH_LONG).show();
    }
    public void onUpdateBillFailure(String e)
    {
        Toast.makeText(BillTestActivity.this, e, Toast.LENGTH_LONG).show();
    }

    public void billDeleteOnClick(View v)
    {
        parse.deleteAlert(testBill, BillTestActivity.this);
    }

    public void onDeleteAlertSuccess()
    {
        Toast.makeText(BillTestActivity.this, "DEAD", Toast.LENGTH_LONG).show();
    }
    public void onDeleteAlertFailure(String e)
    {
        Toast.makeText(BillTestActivity.this, e, Toast.LENGTH_LONG).show();
    }

    public void billEmailOnClick(View v)
    {
        String parker = "smits2010@gmail.com";
        String amount = "$";
        amount = amount.concat(Double.toString(testBill.getAmount()));
        String body = "This is a test message, irl would have added a cc TO cash@square";
        Intent sendMail = new Intent(Intent.ACTION_SEND);
        sendMail.setType("message/rfc822");
        sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{parker});
        sendMail.putExtra(Intent.EXTRA_SUBJECT, amount);
        sendMail.putExtra(Intent.EXTRA_TEXT, body);

        try{
            startActivity(Intent.createChooser(sendMail, "Send money with?"));
        } catch (ActivityNotFoundException e)
        {
            Toast.makeText(BillTestActivity.this, "No clients installed", Toast.LENGTH_LONG).show();
        }
    }
}
