package models;


import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/25/13
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Location {
    private static DecimalFormat df2 = new DecimalFormat( "##.#######" );
    double latitude;
    double longitude;
    public Location(double latitude, double longitude){
        this.latitude =latitude;
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static double[] toLatLng(String str){
        if(str == null){
            throw new IllegalArgumentException("Lat Long value is malformed: " + str);
        }
        String[] latLngStrArr = str.split(",");
        if(latLngStrArr.length != 2){
            throw new IllegalArgumentException("Lat Long value is malformed: " + str);
        }
        return new double[]{Double.parseDouble(latLngStrArr[0]), Double.parseDouble(latLngStrArr[1])};

    }

    public static double randomDeviation(double coordinate){
        if(coordinate < 0){
            return -1.0;
        }
        double deviation = ( (Math.random() + 0.1)  / 5000) * (Math.random() < 0 ? -1 : 1);
        return Double.valueOf(df2.format(coordinate + deviation));
    }
}
