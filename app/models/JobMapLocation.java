package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/26/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class JobMapLocation extends Model {
    String name;
    String address;
    public double latitude = 0.0;
    public double longitude = 0.0;
    @ManyToOne
    Job job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static JobMapLocation byId(long id){
        return find("byId", id).first();
    }
}
