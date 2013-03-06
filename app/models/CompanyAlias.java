package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/21/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class CompanyAlias  extends Model {
    @ManyToOne
    //@JoinColumn(name="company_id", insertable=false, updatable=false)
    public Company company;
    public String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


}
