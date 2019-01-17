package modules;

public class User {
    private String UserName;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String Email;
    private boolean HospitalMember;
    private String HospitalKey;
    private String Password;
    private String BloodType;
    private String Date;
    private boolean Admin;
    private boolean Activated;
    private String UserKey;
    private String Gender;
    private String Address;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public User(){}

    public User(String userName, String firstName, String middleName, String lastName, String email, boolean hospitalMember, String hospitalKey, String password, String bloodType, String date, boolean admin, boolean activated, String userKey,String Gender,String add) {
        UserName = userName;
        FirstName = firstName;
        MiddleName = middleName;
        LastName = lastName;
        Email = email;
        HospitalMember = hospitalMember;
        HospitalKey = hospitalKey;
        Password = password;
        BloodType = bloodType;
        Date = date;
        Admin = admin;
        Activated = activated;
        UserKey = userKey;
        this.Gender = Gender;
        Address = add;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public boolean isHospitalMember() {
        return HospitalMember;
    }

    public void setHospitalMember(boolean hospitalMember) {
        HospitalMember = hospitalMember;
    }

    public String getHospitalKey() {
        return HospitalKey;
    }

    public void setHospitalKey(String hospitalKey) {
        HospitalKey = hospitalKey;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public boolean isActivated() {
        return Activated;
    }

    public void setActivated(boolean activated) {
        Activated = activated;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBloodType() {
        return BloodType;
    }

    public void setBloodType(String bloodType) {
        BloodType = bloodType;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

