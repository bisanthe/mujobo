package models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import play.Logger;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/1/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndeedResult {
    public String jobtitle;
    public String company;
    public String city;
    public String state;
    public String country;
    public String formattedLocation;
    public String source;
    public Date date;
    public String snippet;
    public String url;
    public double latitude;
    public double longitude;
    public String jobkey;
    public boolean sponsored;
    public boolean expired;
    public String formattedLocationFull;
    public String formattedRelativeTime;

    //Tue, 29 Jan 2013 06:12:34 GMT
    private static final SimpleDateFormat parserSDF=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz");

    public IndeedResult(JsonObject source){
        if(source == null){
            throw new RuntimeException("Indeed response is null");
        }
        Field[] fieldArr = this.getClass().getFields();
        for(Field field : fieldArr){
            try {
                JsonElement element = source.get(field.getName());
                if(element == null){
                    Logger.error("Json response null. Field: %s, json: %s",field.getName(), source);
                    continue;
                }
                if(field.getType().equals(boolean.class))  {
                    field.setBoolean(this, element.getAsBoolean());
                }
                else if(field.getType().equals(Date.class)){
                    field.set(this, parserSDF.parse(element.getAsString()));
                }
                else if(field.getType().equals(double.class)){
                    field.setDouble(this, element.getAsDouble());
                }
                else{
                    if("company".equals(field.getName()) && element.getAsString().length() == 0){
                        throw new RuntimeException("Empty company name");

                    }
                    field.set(this, element.getAsString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }


}
