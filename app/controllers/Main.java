package controllers;


import java.util.ArrayList;
import java.util.List;

import com.mujobo.solr.Solr;
import com.mujobo.solr.model.JobDoc;
import com.mujobo.util.HeaderTabs;
import models.*;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Model;
import play.mvc.Controller;

import static controllers.Constants.JOB_ALL;
import static controllers.Constants.LOCATION_ALL;

public class Main extends Controller {

	public static void index(){
        String keyword = request.params.get("keyword");
        String location = request.params.get("location");

        renderArgs.put("activeTab", HeaderTabs.main.toString());
        renderArgs.put("keyword",keyword);
        renderArgs.put("location", location);
        renderArgs.put("jobList",new ArrayList<JobDoc>());
		render("main/index.html");
	}
	
	public static void search(String keyword, String location){
        //List<Job> jobList = Search.search(keyword,locationList,request.remoteAddress, request.headers.get("user-agent").value());
        if(LOCATION_ALL.equals(location)){
            location = "";
        }
        if(JOB_ALL.equals(keyword)){
            keyword = "";
        }

        List<JobDoc> jobList = Solr.defaultSearch(keyword, location);

        if(StringUtils.isBlank(location)){
            location = LOCATION_ALL;
        }
        if(StringUtils.isBlank(keyword)){
            keyword = JOB_ALL;
        }

        renderArgs.put("activeTab",HeaderTabs.main.toString());
        renderArgs.put("keyword",keyword);
        renderArgs.put("location", location);
        renderArgs.put("jobList",jobList);
        render("main/index.html");
	}

    public static void fixwrongcoordinates(){
        //42.230325,26.618652  42.083729,43.262939

        List<JobDoc> jobDocList = Solr.getAll();
        for(JobDoc jobDoc : jobDocList){
            for(int i = 0; i < jobDoc.getJobLocation().size(); i ++){
                double latitude = jobDoc.getJobLatitude().get(i);
                double longitude = jobDoc.getJobLongitude().get(i);
                if(latitude > 34 && latitude < 43 && longitude > 25 && longitude < 47){
                     //System.out.println("Valid: " + jobDoc.getTitle() + ", [" + jobDoc.getJobLatitude() + ", " + jobDoc.getJobLongitude() + "], "+jobDoc.getLocationValidList().get(i));
                    //jobDoc.getLocationValidList().set(i, Boolean.TRUE);
                }
                else {
                    //System.out.println("InValid: " + jobDoc.getTitle() + ", [" + jobDoc.getJobLatitude() + ", " + jobDoc.getJobLongitude() + "], "+jobDoc.getLocationValidList().get(i));
                    //jobDoc.getLocationValidList().set(i, Boolean.FALSE);
                }
                Solr.saveJob(jobDoc);
            }

        }
        renderText("Hello world");
    }

    public static void deleteall(){
        //JobLocation.deleteAll();
        JobMapLocation.deleteAll();
        JobLocation.deleteAll();
        Job.deleteAll();
        CompanyMapLocation.deleteAll();
        CompanyAlias.deleteAll();
        Company.deleteAll();


        Solr.deleteAll();
    }
}
