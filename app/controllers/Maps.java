package controllers;


import com.mujobo.com.mujobo.search.Search;
import com.mujobo.controller.MjController;

import com.mujobo.solr.Solr;
import com.mujobo.solr.model.JobDoc;
import com.mujobo.util.HeaderTabs;
import models.Job;
import org.apache.commons.lang.StringUtils;
import play.mvc.Controller;
import static controllers.Constants.LOCATION_ALL;
import static controllers.Constants.JOB_ALL;

import java.util.ArrayList;
import java.util.List;

public class Maps extends Controller{
	public static void index(){
        String keyword = request.params.get("keyword");
        String location = request.params.get("location");

        renderArgs.put("activeTab", HeaderTabs.maps.toString());
        renderArgs.put("keyword",keyword);
        renderArgs.put("location", location);
        renderArgs.put("jobList", new ArrayList<JobDoc>());
		render("maps/index.html");
	}

    public static void search(String keyword, String location){
        //List<Job> jobList = Search.search(keyword, locationList, request.remoteAddress, request.headers.get("user-agent").value());
        if(LOCATION_ALL.equals(location)){
            location = "";
        }
        if(JOB_ALL.equals(keyword)){
            keyword = "";
        }
        List<JobDoc> jobList = Solr.mapSearch(keyword, location);

        if(StringUtils.isBlank(location)){
            location = LOCATION_ALL;
        }
        if(StringUtils.isBlank(keyword)){
            keyword = JOB_ALL;
        }
        renderArgs.put("activeTab",HeaderTabs.maps.toString());
        renderArgs.put("keyword",keyword);
        renderArgs.put("location", location);
        renderArgs.put("jobList", jobList);
        render("maps/index.html");
    }
}
