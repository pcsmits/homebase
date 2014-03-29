package app.android.homeBase;

/**
 * Created by kyle on 3/10/14.
 */
public class House {
    private String housename;
    private String address;
    private String city;
    private String state;
    private int zipCode;
    private double latitude;
    private double longitude;
    private String id;

    public House(String housename, String address, String city, String state, int zipCode, double lat, double lng)
    {
        this.housename = housename;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.latitude = lat;
        this.longitude = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHousename() {

        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
