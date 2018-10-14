package com.golaspico.vanhyori.prov_hv3;

public class HospitalList {

    private String Name;
    private String City;
    private String Title;
    private String Contents;
    private String ImageUrl;

    public HospitalList(String name, String city, String title, String contents, String imageUrl) {
        Name = name;
        City = city;
        Title = title;
        Contents = contents;
        ImageUrl = imageUrl;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
