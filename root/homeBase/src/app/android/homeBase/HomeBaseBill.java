package app.android.homeBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kyle on 4/11/14.
 */
public class HomeBaseBill extends HomeBaseAlert
{
    private Double amount;
    private List<String> toPay;
    private List<String> paid;

    // Default Constructor
    public HomeBaseBill(String title, String id, List<String> seen, String description, String ownerID, String creatorID)
    {
        super(title, id, "bill", seen, description, ownerID, creatorID);
    }

    // Constructor with Amount (Use this one most of the time)
    public HomeBaseBill(String title, String id, Double amount, List<String> toPay, List<String> paid, List<String> seen, String description, String ownerID, String creatorID)
    {
        super(title, id, "bill", seen, description, ownerID, creatorID);
        this.amount = amount;
        this.toPay = toPay;
        this.paid = paid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<String> getToPay() {
        return toPay;
    }

    public void setToPay(List<String> toPay) {
        this.toPay = toPay;
    }

    public List<String> getPaid() {
        return paid;
    }

    public void setPaid(String paidID) {
        this.paid.add(paidID);
        this.toPay.remove(paidID);
    }




}
