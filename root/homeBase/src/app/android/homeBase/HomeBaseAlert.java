package app.android.homeBase;

import java.util.List;

/**
 * Created by kyle on 3/24/14.
 * Generic alert class for wrapping Chores, Messages, Groceries
 */
public class HomeBaseAlert {

    // These can only be set on creation
    private final String id;
    private String type;
    private String description;
    private String ownerID;
    private String creatorID;

    private List<String> seen;
    //TODO private Picture picture;

    public HomeBaseAlert(String id, String type, List<String> seen, String description, String ownerID, String creatorID)
    {
        this.id = id;
        this.type = type;
        this.seen = seen;
        this.description = description;
        this.ownerID = ownerID;
        this.creatorID = ownerID;

    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public void setSeen(List<String> seen) {
        this.seen = seen;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<String> getSeen() {
        return seen;
    }

    public void addSeen(String user) {
        this.seen.add(user);
    }

    public String getDescription() {
        return description;
    }

}
