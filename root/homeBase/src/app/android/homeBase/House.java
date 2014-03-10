package app.android.homeBase;

/**
 * Created by kyle on 3/10/14.
 */
public class House{
    private String housename;
    private String address;
    private int zipCode;
    private float latitude;
    private float longitude;

    public House(String housename, String address, int zipCode)
    {
        this.housename = housename;
        this.address = address;
        this.zipCode = zipCode;
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

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {

        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
