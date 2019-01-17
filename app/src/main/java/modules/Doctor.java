package modules;

public class Doctor{
    private String firstName;
    private String lastName;
    private String middleName;
    private String service;

    public Doctor() {
    }

    public Doctor(String firstName, String lastName, String middleName, String service) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.service = service;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}