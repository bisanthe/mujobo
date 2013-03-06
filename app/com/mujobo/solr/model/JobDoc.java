package com.mujobo.solr.model;

import models.JobLocation;
import models.JobMapLocation;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.DateUtil;

import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/11/13
 * Time: 9:40 AM
 *
 */

public class JobDoc {
    @Field("id")
    public String id;
    @Field("title")
    public String title;
    @Field("submitDate")
    public Date submitDate;
    @Field("lastUpdate")
    public Date lastUpdate;
    @Field("source")
    public String source;
    @Field("description")
    public String description;
    @Field("expired")
    public boolean expired;
    @Field("key")
    public String key;
    @Field("url")
    public String url;
    @Field("sector")
    public String sector;
    @Field("hasLocation")
    public boolean hasLocation;

    //Company
    @Field("companyId")
    public long companyId;
    @Field("companyName")
    public String companyName;

    //JobLocation
    @Field("jobLocation")
    public List<String> jobLocation = new ArrayList<String>();

    //JobMapLocation
    @Field("mapLocation")
    public List<String> jobMapLocation = new ArrayList<String>();
    @Field("mapLocationId")
    public List<Long> jobMapLocationId = new ArrayList<Long>();
    @Field("jobLatLng")
    public List<String> jobLatLng = new ArrayList<String>();
    @Field("jobAddress")
    public List<String> jobAddress = new ArrayList<String>();

    public List<Double> jobLatitude = new ArrayList<Double>();
    public List<Double> jobLongitude = new ArrayList<Double>();

    public StringBuilder highligtedTitle = new StringBuilder();
    public StringBuilder highligtedDescription = new StringBuilder();

    public JobDoc(models.Job job){
        this.id = String.valueOf(job.getId());
        this.title = job.getTitle();
        this.companyId = job.getCompany().getId();
        this.companyName = job.getCompany().getName();
        for(JobLocation location : job.getLocationList()){
            this.jobLocation.add(location.getName());
        }

        for(JobMapLocation mapLocation : job.getMapLocationList()){
            this.jobMapLocation.add(mapLocation.getName());
            this.jobLatLng.add(mapLocation.getLatitude() + ", " + mapLocation.getLongitude());
            this.jobAddress.add(mapLocation.getAddress());
        }

        this.submitDate = job.getSubmitDate();
        this.lastUpdate = job.getLastUpdate();
        this.source = job.getSource();
        this.description = job.getDescription();
        this.expired = job.isExpired();
        this.key = job.getKey();
        this.url = job.getUrl();
        this.sector = job.getSector();
        if(job.getMapLocationList().size() > 0 ){
            this.hasLocation = true;
        }
        else{
            this.hasLocation = false;
        }
    }

    public JobDoc(SolrDocument doc){
        this.id = doc.getFieldValue("id").toString();
        this.title = doc.getFieldValue("title").toString();
        try {
            this.submitDate = DateUtil.parseDate(doc.getFieldValue("submitDate").toString());
            this.lastUpdate = DateUtil.parseDate(doc.getFieldValue("lastUpdate").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.source = doc.getFieldValue("source").toString();
        this.description = doc.getFieldValue("description").toString();
        this.expired = Boolean.parseBoolean(doc.getFieldValue("expired").toString());
        this.key = doc.getFieldValue("key").toString();
        this.url = doc.getFieldValue("url").toString();
        this.sector = doc.getFieldValue("sector").toString();
        this.hasLocation = Boolean.parseBoolean(doc.getFieldValue("hasLocation").toString());

        //company
        this.companyId = Long.parseLong(doc.getFieldValue("companyId").toString());
        this.companyName = doc.getFieldValue("companyName").toString();

        //joblocation
        this.jobLocation = new ArrayList(doc.getFieldValues("jobLocation") == null ? new ArrayList() : doc.getFieldValues("jobLocation"));
        //jobmap
        this.jobMapLocation = new ArrayList(doc.getFieldValues("mapLocation") == null ? new ArrayList() : doc.getFieldValues("mapLocation"));
        this.jobMapLocationId = new ArrayList(doc.getFieldValues("mapLocationId") == null ? new ArrayList() : doc.getFieldValues("mapLocationId"));
        this.jobLatLng = new ArrayList(doc.getFieldValues("jobLatLng") == null ? new ArrayList() : doc.getFieldValues("jobLatLng"));
        this.jobAddress  = new ArrayList(doc.getFieldValues("jobAddress") == null ? new ArrayList() : doc.getFieldValues("jobAddress"));
        parseJobLocation(jobLatLng);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public List<String> getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(List<String> jobLocation) {
        this.jobLocation = jobLocation;
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

    public List<Double> getJobLatitude() {
        return jobLatitude;
    }

    public void setJobLatitude(List<Double> jobLatitude) {
        this.jobLatitude = jobLatitude;
    }

    public List<Double> getJobLongitude() {
        return jobLongitude;
    }

    public void setJobLongitude(List<Double> jobLongitude) {
        this.jobLongitude = jobLongitude;
    }

    public String getActualTitle(){
         if(highligtedTitle == null || StringUtils.isBlank(highligtedTitle.toString())){
             return title;
         }
         else{
             return highligtedTitle.toString();
         }
    }

    public boolean isHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(boolean hasLocation) {
        this.hasLocation = hasLocation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getActualDescription(){
        if(highligtedDescription == null || StringUtils.isBlank(highligtedDescription.toString())){
            return description;
        }
        else{
            return highligtedDescription.toString();
        }
    }

    private void parseJobLocation(List<String> jobLatLngList){
        if(jobLatLngList == null)return;
        for(String location : jobLatLngList){
            String[] latLng = location.split(",");
            this.jobLatitude.add(Double.parseDouble(latLng[0]));
            this.jobLongitude.add(Double.parseDouble(latLng[1]));
        }
    }

    public void applyHighlightedTitle(List<String> highlightedTitleList){
        for(String s : highlightedTitleList){
            highligtedTitle.append(s);
        }
    }

    public void applyHighlightedDescription(List<String> highlightedDescriptionList){
        for(String s : highlightedDescriptionList){
            highligtedDescription.append(s).append("... ");
        }
    }

    public void addJobMapLocation(long id,String name, String address, double lat, double lng) {
        if(!isJobMapLocationIdExist(id)){
            jobAddress.add(address);
            jobMapLocation.add(name);
            jobLatLng.add(lat + "," + lng);
            jobMapLocationId.add(id);
            hasLocation = true;
        }
    }

    public boolean isJobMapLocationIdExist(long id){
        for(Long solrId : jobMapLocationId){
            if(solrId.equals(id)){
                return true;
            }
        }
        return false;
    }
}
