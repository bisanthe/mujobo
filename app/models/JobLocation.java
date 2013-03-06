package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/18/13
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"job_id", "name"})})
public class JobLocation extends Model {
    public String name;
    @ManyToOne
    Job job;
    public JobLocation(String jobLocation){
        this.name = jobLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString(){
        return name;
    }
}
