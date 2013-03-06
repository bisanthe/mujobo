package com.mujobo.com.mujobo.search;

import com.mujobo.com.mujobo.indeed.IndeedProxy;
import models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/7/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Search {
    public static List<Job> search(String keyword, String location, String remoteAddress, String userAgent){
        IndeedProxy indeedProxy = new IndeedProxy();
        List<IndeedResult> indeedResultsList = indeedProxy.query(keyword, location, remoteAddress, userAgent);
        List<String> jobKeyList = indeedProxy.jobKeyList;
        List<Job> jobList = new ArrayList<Job>(100);
        for(IndeedResult result : indeedResultsList){
            Company company = Company.findByName(result.company);
            if(company == null){
                company = new Company();
                company.name = result.company;
                company.save();
            }
            Job job = Job.findByJobKey(result.jobkey);
            if(job == null){
                job = new Job();
            }
            job.company = company;
            job.submitDate = result.date;
            job.lastUpdate = result.date;
            job.expired = result.expired;
            job.key = result.jobkey;
            job.title = result.jobtitle;
            job.locationList.add(new JobLocation(result.formattedLocationFull));
            job.description = result.snippet;
            job.source = result.source;
            job.save();
            jobList.add(job);

        }
        return jobList;
    }
}
