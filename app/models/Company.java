package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

import play.db.jpa.Model;

@Entity
public class Company extends Model {
	public String logo;
    @Column(unique = true)
	public String name;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    public List<Job> jobList = new ArrayList<Job>();
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    public List<CompanyAlias> aliasList = new ArrayList<CompanyAlias>();
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    public List<CompanyMapLocation> mapLocationList = new ArrayList<CompanyMapLocation>();

    public boolean askLocation = true;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public boolean isAskLocation() {
        return askLocation;
    }

    public void setAskLocation(boolean askLocation) {
        this.askLocation = askLocation;
    }

    public List<CompanyMapLocation> getMapLocationList() {
        return mapLocationList;
    }

    public void setMapLocationList(List<CompanyMapLocation> mapLocationList) {
        this.mapLocationList = mapLocationList;
    }

    public static Company findByName(String name){
        return Company.find("byName", name).first();
    }
    public static List<Company> findMissingCoordinates(){
        return Company.find("latitude < 0 and longitude < 0").fetch();
    }

    public static Company byId(long id){
        return Company.find("byId", id).first();
    }

    public static List<Company> getCompanyMissingLocationList() {
        return find("select c from Company c where c.id not in (select company from CompanyMapLocation)").fetch();
    }

    public String firstJobLocationName(){
        if(jobList.size() > 0 && jobList.get(0).getLocationList().size() > 0){
            return jobList.get(0).getLocationList().get(0).getName();
        }
        return "";
    }

    public String randomJobUrl(){
        if(jobList.size() > 0){
            return jobList.get( (int)(Math.random() * jobList.size()) ).getUrl();
        }
        return "";
    }
}
