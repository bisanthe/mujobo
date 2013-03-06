package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.Model;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"sourceid", "source"})})
public class Job extends Model {
    public static final int DESCRIPTION_LENGTH = 4096;
    @OneToOne
    public Company company;
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    public List<JobLocation> locationList = new ArrayList<JobLocation>();
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    public List<JobMapLocation> mapLocationList = new ArrayList<JobMapLocation>();
    public String title;

    public Date submitDate;
    public Date lastUpdate;
    public String source;
    @Column(length = DESCRIPTION_LENGTH)
    public String description;
    public boolean expired;
    @Column(unique = true)
    public String key;
    public String url;
    public String sourceId;
    public String sector;
    public boolean askLocation = true;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<JobLocation> getLocationList(){
        return locationList;
    }

    public void setLocationList(List<JobLocation> locationList){
        this.locationList = locationList;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean isAskLocation() {
        return askLocation;
    }

    public void setAskLocation(boolean askLocation) {
        this.askLocation = askLocation;
    }


    public List<JobMapLocation> getMapLocationList() {
        return mapLocationList;
    }

    public void setMapLocationList(List<JobMapLocation> mapLocationList) {
        this.mapLocationList = mapLocationList;
    }

    public static Job findByJobKey(String key){
        return Job.find("byKey", key).first();
    }

    public static Job byId(long id){
        return Job.find("byId", id).first();
    }

    public static List<Job> findByJobKey(List<String> jobKeyList){
        return Job.find("key in (:jobkeys)").bind("jobkeys", jobKeyList).fetch();
    }

    public static List<Job> findByCompany(Company company){
        return Job.find("byCompany",company).fetch();
    }

    public static List<Job> findMissingCoordinatesByCompany(Company company){
        return Job.find("latitude < 0 and longitude < 0 and company=?1",company).fetch();
    }

    public static List<Job> findJobsByValidLocation(){
        return Job.find("byLocationValid", true).fetch();
    }


    public static List<Job> getJobMissingLocationList() {
        return find("select j from Job j where j.id not in (select job from JobMapLocation)").fetch();
    }

    public String getFirstLatLng(){
        if(mapLocationList.size() == 0){
            return "0,0";
        }
        else{
            return mapLocationList.get(0).getLatitude() + ", "  + mapLocationList.get(0).getLatitude();
        }
    }
    //public static
}
