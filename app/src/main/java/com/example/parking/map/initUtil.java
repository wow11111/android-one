package com.example.parking.map;

import java.io.Serializable;

public class initUtil implements Serializable {

    /**
     * id : 1
     * longitude : 121.545881
     * latitude : 29.815882
     * carName : 停车场(首南中路)
     */

    private int id;
    private String longitude;
    private String latitude;
    private String carName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}
