package controllers;

import com.google.gson.Gson;
import com.mujobo.solr.Solr;
import com.mujobo.solr.model.JobDoc;
import com.mujobo.util.Util;
import models.*;
import org.hibernate.exception.ConstraintViolationException;
import play.mvc.Controller;
import play.mvc.Http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/21/13
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerApi  extends Controller {
    private static final Gson gson = new Gson();
    public static final DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
    public static void addJob(){
        String titleStr = params.get("title");
        String companyStr = params.get("company");
        String cityListStr = params.get("city");
        String date = params.get("date");
        String description = params.get("description");
        String uniqueId = params.get("uniqueId");
        String sector = params.get("sector");
        String source = params.get("source");
        String url = params.get("url");

        if(description.length() > Job.DESCRIPTION_LENGTH){
            description = description.substring(0, Job.DESCRIPTION_LENGTH);
        }

        Company company = Company.findByName(companyStr.trim());
        List<String> locationList =  locationList(cityListStr);
        if(company == null){
            company = new Company();
            company.setName(companyStr);
            company.save();
        }


        Job job = new Job();
        job.setCompany(company);
        job.setTitle(titleStr.trim());

        job.setDescription(description);
        job.setKey(Util.uuidPure());
        job.setUrl(url);
        job.setSourceId(uniqueId);
        job.setSector(sector);
        job.setSource(source);
        try {
            job.setLastUpdate(df.parse(date));
            job.setSubmitDate(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try{
        job.save();
        saveLocationLust(date, locationList,job);
        for(CompanyMapLocation companyMapLocation : company.getMapLocationList()){
             for(JobLocation jobLocation : job.getLocationList()){
                 if(companyMapLocation.getName().equals(jobLocation)){
                     JobMapLocation jobMapLocation = new JobMapLocation();
                     double lat = Location.randomDeviation(companyMapLocation.getLatitude());
                     double lng = Location.randomDeviation(companyMapLocation.getLongitude());
                     jobMapLocation.setName(companyMapLocation.getName());
                     jobMapLocation.setLatitude(lat);
                     jobMapLocation.setLongitude(lng);
                     jobMapLocation.setJob(job);
                     jobMapLocation.save();
                     job.getMapLocationList().add(jobMapLocation);
                     break;
                 }
             }
        }
        JobDoc jobDoc = new JobDoc(job);
        Solr.saveJob(jobDoc);
            response.status = Http.StatusCode.OK;
            renderXml("<result><status>OK</status></result>");
        }
        catch (ConstraintViolationException cve){
            renderXml("<result><status>FAILED</status><error>Job already exist</error></result>");
        }
        catch (Exception ex){
            response.status =  Http.StatusCode.OK;
            renderXml("<result><status>FAILED</status><error>"+ex.getMessage()+"</error></result");
        }
    }

    private static void saveLocationLust(String date, List<String> locationList, Job job) {
        List<JobLocation> jobLocationList = toJobLocationList(locationList);
        for(JobLocation jobLocation : jobLocationList){
            jobLocation.setJob(job);
            jobLocation.save();
        }
        job.setLocationList(jobLocationList);
    }

    private static List<String> locationList(String cityList){
        ArrayList<String> cityNameList = new ArrayList<String>();
        if(cityList == null){
            return cityNameList;
        }
        String[] cityListArr = cityList.split(",");
        for(String cityCityName : cityListArr){
            String trimmedCityName=  cityCityName.trim();
            if(trimmedCityName.length() > 0){
                cityNameList.add(trimmedCityName);
            }
        }
        return cityNameList;
    }

    private static List<JobLocation> toJobLocationList(List<String> locationStrList){
        List<JobLocation> jobLocationSet = new ArrayList<JobLocation>();
        for(String s: locationStrList){
            JobLocation jobLocation=  new JobLocation(s);
            jobLocationSet.add(jobLocation);
        }
        return jobLocationSet;
    }
}
