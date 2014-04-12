package app.android.homeBase;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.parse.ParseUser;

public class BillsActivity extends HomeBaseActivity {
    ArrayList<BootstrapButton> billContainers;
    HashMap<BootstrapButton, HomeBaseBill> billDescriptions;
    ParseBase parse;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Init parse
        parse = new ParseBase(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores);

        layout = (LinearLayout) findViewById(R.id.chores_choreContainer_button);
        billContainers = new ArrayList<BootstrapButton>();
        billDescriptions = new HashMap<BootstrapButton, HomeBaseBill>();

        parse.getUnpaidBillsByUserID(ParseUser.getCurrentUser().getObjectId(), BillsActivity.this);

    }

    @Override
    public void onGetBillsSuccess(List<HomeBaseBill> bills)
    {
        // Fetch all the bills from parse
        for (HomeBaseBill bill : bills)
        {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.bill_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.bill_container_button);
            buttonCont.removeView(myButton);
            layout.addView(myButton);
            String text = "Bill: "+bill.getAmount();
            myButton.setText(text);
            billContainers.add(myButton);
            billDescriptions.put(myButton, bill);
        }
    }

    public void onChoreContainerClick(View view)
    {
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.bill_container_button);
        thisButton.setText("Clicked");
        Intent intent = new Intent(BillsActivity.this, ChoreInfoActivity.class);
        intent.putExtra("title", billDescriptions.get(thisButton).getTitle());
        intent.putExtra("info", billDescriptions.get(thisButton).getDescription());
        startActivity(intent);
    }

    @Override
    public void onGetBillsFailure(String e)
    {

    }
}
