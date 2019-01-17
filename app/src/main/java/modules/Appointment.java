package modules;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Appointment {


    private String Message;
    private String Uid;
    private User user;
    private String Type;
    private String Date;
    private String HospitalName;
    private String Status;
    private String UserName;
    private String Key;
    private Doctor doctor;
    private String HospitalKey;


    public String getHospitalKey() {
        return HospitalKey;
    }

    public void setHospitalKey(String hospitalKey) {
        HospitalKey = hospitalKey;
    }

    public Appointment(){}

    public Appointment(String message, String uid, User user, String type, String date, String hospitalName, String status, String userName, String key,
                       Doctor doctor) {
        Message = message;
        Uid = uid;
        this.user = user;
        Type = type;
        Date = date;
        HospitalName = hospitalName;
        Status = status;
        UserName = userName;
        Key = key;
        Doctor tempDoctor = new Doctor();
        tempDoctor.setFirstName("Pending");
        tempDoctor.setMiddleName("for");
        tempDoctor.setLastName("Doctor");
        tempDoctor.setService("Assignment");
        this.doctor = tempDoctor;
    }

    @Exclude
    public String getDoctor2() {
        return this.doctor.getFirstName() + " " + this.doctor.getLastName();
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
