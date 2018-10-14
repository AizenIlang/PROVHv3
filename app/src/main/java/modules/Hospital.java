package modules;

public class Hospital {
    private int HospitalID;
    private String Name;
    private String Location;
    private String Address;
    private String ContactNumber;
    private String Email;
    private String Coordinates;
    private String Details;
    private int Rating;
    private String image;

    public Hospital(int hospitalID, String name, String location, String address, String contactNumber, String email, String coordinates, String details, int rating, String image) {
        HospitalID = hospitalID;
        Name = name;
        Location = location;
        Address = address;
        ContactNumber = contactNumber;
        Email = email;
        Coordinates = coordinates;
        Details = details;
        Rating = rating;
        this.image = image;
    }

    public int getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(int hospitalID) {
        HospitalID = hospitalID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(String coordinates) {
        Coordinates = coordinates;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


