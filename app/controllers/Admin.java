package controllers;

import com.mujobo.solr.Solr;
import com.mujobo.solr.model.JobDoc;
import com.mujobo.util.Encryption;
import com.mujobo.util.ListUtil;
import models.*;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/24/13
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class Admin extends Controller{
    @Before(unless = {"login", "signin"})
    static void authenticate(){
        if(session.get("adminKey") == null){
            login();
        }
    }

    public static void login(){
         render("admin/login.html");
    }

    public static void index(){
        render("admin/index.html");
    }

    public static void signin(String email, String password){
        MjAdmin admin = MjAdmin.getAdminByCredentials(email, Encryption.md5(password));
        if(admin != null){
            session.put("adminKey",admin.getKey());
            index();
        }
        else{
            login();
        }

    }

    public static void signout(){
        session.remove("adminKey");
        render("admin/login.html");
    }

    public static void showInvalidJobLocation(){
        List<Job> jobList = Job.getJobMissingLocationList();
        renderArgs.put("jobList", jobList);
        render("admin/jobMissingLocation.html");
    }

    public static void showInvalidCompanyLocation(){
        List<Company> companyList = Company.getCompanyMissingLocationList();
        renderArgs.put("companyList", companyList);
        renderArgs.put("type", "all");
        render("admin/companyMissingLocation.html");
    }

    public static void updateJobLocation(long jobId, String latlng, boolean askJobLocation){
        Job job = Job.byId(jobId);
        if(job != null){
            if(job.isAskLocation() != askJobLocation){
                job.setAskLocation(askJobLocation);
                job.save();
            }
            if(askJobLocation){
                JobMapLocation jobMapLocation = new JobMapLocation();
                jobMapLocation.setName(ListUtil.first(job.getLocationList()).getName());

                double[] latlngArr = Location.toLatLng(latlng);
                jobMapLocation.setLatitude(latlngArr[0]);
                jobMapLocation.setLongitude(latlngArr[1]);
                jobMapLocation.setJob(job);
                jobMapLocation.save();


                JobDoc jobDoc = Solr.byId(jobId);
                jobDoc.addJobMapLocation(jobMapLocation.getId(), jobMapLocation.getName(), jobMapLocation.getAddress(), latlngArr[0], latlngArr[1]);
                Solr.saveJob(jobDoc);

            }
        }
        String requestType = request.params.get("type");
        if(requestType == null || "all".equals(requestType)){
            showInvalidJobLocation();
        }
        else if("company".equals(requestType)){
            getJobsForCompany(job.getCompany().getId());
        }
    }

    public static void getJobsForCompany(long companyId){
        List<Job> jobList = new ArrayList<Job>();
        Company company = Company.byId(companyId);
        if(company != null){
             jobList = company.getJobList();
        }
        renderArgs.put("jobList", jobList);
        renderArgs.put("type", "company");
        render("admin/jobMissingLocation.html");
    }

    public static void updateCompanyLocation(long companyId, String latlng, String locationName, boolean askLocation, boolean updateAttachedJobs){
        Company company = Company.findById(companyId);
        List<Job> jobList = company.getJobList();
        if(company != null){
            if(company.isAskLocation() != askLocation){
                for(Job job : jobList){
                    job.setAskLocation(askLocation);
                    job.save();
                }
                company.setAskLocation(askLocation);
                company.save();
            }

            if(askLocation){
                CompanyMapLocation companyMapLocation = new CompanyMapLocation();
                companyMapLocation.setName(locationName);
                double[] latlngArr = Location.toLatLng(latlng);
                companyMapLocation.setLatitude(latlngArr[0]);
                companyMapLocation.setLongitude(latlngArr[1]);
                companyMapLocation.setCompany(company);
                companyMapLocation.save();
                if(updateAttachedJobs){

                    for(Job job : jobList){
                        if(job.getMapLocationList().size() == 0){
                            JobMapLocation jobMapLocation = new JobMapLocation();
                            jobMapLocation.setName(companyMapLocation.getName());
                            double lat = Location.randomDeviation(companyMapLocation.getLatitude());
                            double lng = Location.randomDeviation(companyMapLocation.getLongitude());
                            jobMapLocation.setLatitude(lat);
                            jobMapLocation.setLongitude(lng);
                            jobMapLocation.setJob(job);
                            jobMapLocation.save();
                            job.getMapLocationList().add(jobMapLocation);
                        }
                        JobDoc jobDoc = new JobDoc(job);
                        Solr.saveJob(jobDoc);
                    }

                }
            }
        }
        showInvalidCompanyLocation();
    }
}
