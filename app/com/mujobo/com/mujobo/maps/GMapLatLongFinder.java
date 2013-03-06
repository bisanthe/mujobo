package com.mujobo.com.mujobo.maps;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mujobo.com.mujobo.indeed.NetUtil;
import models.Location;
import play.Logger;

import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/6/13
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class GMapLatLongFinder implements LatLongFinder {
    private static final JsonParser parser = new JsonParser();
    StringBuffer sb = new StringBuffer("https://maps.googleapis.com/maps/api/place/textsearch/json?sensor=false&key=AIzaSyC0aB99QqTffXHK0POusNljnHoUDT0E034&query=");
    public Location findCoordinate(String address){
        if(address == null){
            Logger.error("Address is null or zero length, name: %s", address);
            return null;
        }
        try {
            sb.append(URLEncoder.encode(address,"UTF-8"));
            String response = NetUtil.readUrl(sb.toString());
            if(response == null){
                Logger.error("Google maps read url failed, url: %s, response: %s, address: %s", sb.toString(), response, address);
                return null;
            }
            JsonElement resultElement = parser.parse(response);
            if(resultElement == null){
                Logger.error("Google maps response result element not found, response: %s, address: %s", response, address);
                return null;
            }
            JsonArray resultArray = resultElement.getAsJsonObject().get("results").getAsJsonArray();
            if(resultArray == null){
                Logger.error("Google maps result array is null, response: %s, address: %s",response, address);
                return null;
            }
            if(resultArray.size() > 0){
                JsonElement geometryElement = resultArray.get(0).getAsJsonObject().get("geometry");
                JsonElement formattedAddressElement = resultArray.get(0).getAsJsonObject().get("formatted_address");
                JsonElement locationElement = geometryElement.getAsJsonObject().get("locationList");
                String formattedAddress = formattedAddressElement.getAsString();
                double lat = locationElement.getAsJsonObject().get("lat").getAsDouble();
                double lng = locationElement.getAsJsonObject().get("lng").getAsDouble();
                return new Location(lat,lng);
            }
            else{
                Logger.error("Google maps result array size is zero, response: %s, address: %s",response, address);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
