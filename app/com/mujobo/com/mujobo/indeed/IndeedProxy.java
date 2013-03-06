package com.mujobo.com.mujobo.indeed;

import com.google.gson.*;
import models.IndeedResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/1/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndeedProxy {
    private static final JsonParser parser = new JsonParser();
    StringBuilder querySb = new StringBuilder("http://api.indeed.com/ads/apisearch?publisher=<secret>&sort=&radius=&st=&jt=&start=&limit=100&fromage=&filter=&latlong=1&co=tr&chnl=&v=2&c0=tr&format=json");
    public List<IndeedResult> resultList = new ArrayList(25);
    public List<String> jobKeyList = new ArrayList(25);
    public List<IndeedResult> query(String keyword, String location, String userIp, String userAgent){
        this.querySb.append("&q=").append(keyword).append("&l=").append(location).append("&userip=").append(userIp).append("&useragent=").append(userAgent);
        try {
            String jsonText = NetUtil.readUrl(this.querySb.toString());
            JsonElement jsonElement = parser.parse(jsonText);
            JsonArray jsonResultArray = jsonElement.getAsJsonObject().getAsJsonArray("results");
            for(JsonElement element : jsonResultArray){
                try{
                    IndeedResult indeedResult = new IndeedResult((JsonObject)element);
                    resultList.add(indeedResult);
                    jobKeyList.add(indeedResult.jobkey);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return resultList;
    }

    public List<IndeedResult> query(String keyword, String location){
        return query(keyword, location, "127.0.0.1", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17");
    }


}
