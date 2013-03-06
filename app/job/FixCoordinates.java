package job;

import com.mujobo.com.mujobo.maps.LatLongFinder;
import com.mujobo.com.mujobo.maps.LatLongFinderFactory;
import models.Company;
import models.CompanyMapLocation;
import models.Job;
import models.Location;
import play.jobs.Every;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 2/6/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Every("1min")
public class FixCoordinates extends play.jobs.Job {
   // StringBuilder

    @Override
    public void doJob() throws Exception {
        if(true)return;
        List<Company> missingCoordinates = Company.findMissingCoordinates();
        for(Company company : missingCoordinates){
            CompanyMapLocation companyLocation = company.getMapLocationList().size() > 0 ? company.getMapLocationList().get(0) : null;
            if(companyLocation != null){
                LatLongFinder latLongFinder = LatLongFinderFactory.create();
                Location location = latLongFinder.findCoordinate(companyLocation.getName());
                if(location != null){
                    companyLocation.setLatitude(location.getLatitude());
                    companyLocation.setLongitude(location.getLongitude());
                    company.save();
                }
            }

            List<Job> jobList = Job.findMissingCoordinatesByCompany(company);
            for(Job job : jobList){
                if(job.getLocationList().size() > 0 && company.getMapLocationList() != null && company.getMapLocationList().size() > 0){
                    job.getMapLocationList().get(0).setLatitude(Location.randomDeviation(company.getMapLocationList().get(0).getLatitude()));
                    job.getMapLocationList().get(0).setLongitude(Location.randomDeviation(company.getMapLocationList().get(0).getLongitude()));
                }
                job.save();
            }
        }
    }

    public double[] getCoordinate(String companyName){
        return null;
        //http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=false
        //NetUtil.readUrl()
    }
}
