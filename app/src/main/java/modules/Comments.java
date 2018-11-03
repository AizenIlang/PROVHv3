package modules;

public class Comments {

    private String Uid;
    private String Message;
    private String Name;
    private boolean Approved;
    private float Rate;

    public Comments() {
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
