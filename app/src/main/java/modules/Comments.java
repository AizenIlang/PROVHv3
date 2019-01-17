package modules;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class Comments {

    private String Uid;
    private String Message;
    private String Name;
    private boolean Approved;
    private float Rate;
    private HashMap<String,Object> datePosted;

    public Comments() {
        HashMap<String,Object> timestampnow = new HashMap<>();
        timestampnow.put("timestamp", ServerValue.TIMESTAMP);
        this.datePosted = timestampnow;
    }

    public HashMap<String, Object> getDatePosted() {
        return datePosted;
    }

    @Exclude
    public long getDatePostedLong(){
        return (long)datePosted.get("timestamp");
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Comments(String uid, String message, String name, boolean approved, int rate) {
        Uid = uid;
        Message = message;
        Name = name;
        Approved = approved;
        Rate = rate;
        HashMap<String,Object> timestampnow = new HashMap<>();
        timestampnow.put("timestamp", ServerValue.TIMESTAMP);
        this.datePosted = timestampnow;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isApproved() {
        return Approved;
    }

    public void setApproved(boolean approved) {
        Approved = approved;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }
}
