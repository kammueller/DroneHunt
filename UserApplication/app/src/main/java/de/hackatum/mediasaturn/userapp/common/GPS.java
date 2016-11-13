package de.hackatum.mediasaturn.userapp.common;

/**
 * represents GPS-Coordinates
 *
 * @author Matthias Kammueller
 */
public class GPS {
    private double longitude;
    private double latitude;

    public GPS(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
