package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/24/13
 * Time: 1:36 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class MjAdmin extends Model {
    public  String email;
    public String password;
    public Date createdAt;
    public Date lastLogin;
    public String key;

    public static MjAdmin getAdminByKey(String key){
        return find("byKey",key).first();
    }

    public static MjAdmin getAdminByCredentials(String email, String password){
         return find("email = ?1 and password = ?2", email, password).first();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
