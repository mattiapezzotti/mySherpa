package it.unimib.camminatori.mysherpa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("tourism")
    @Expose
    private String tourism;
    @SerializedName("road")
    @Expose
    private String road;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("house_number")
    @Expose
    private String houseNumber;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("neighbourhood")
    @Expose
    private String neighbourhood;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("postcode")
    @Expose
    private String postcode;

    public String getTourism() {
        return tourism;
    }

    public void setTourism(String tourism) {
        this.tourism = tourism;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

}