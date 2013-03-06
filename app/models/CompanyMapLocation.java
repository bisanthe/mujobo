package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/26/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class CompanyMapLocation extends Model {
    String name;
    String address;
    double latitude;
    double longitude;
    @ManyToOne
    Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
