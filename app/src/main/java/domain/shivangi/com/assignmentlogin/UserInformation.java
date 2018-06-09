package domain.shivangi.com.assignmentlogin;


/**
 * Created by Shiavngi Pandey on 31/05/2018.
 */

public class UserInformation {
    private String email,user_name,password,mobile_num,full_name,dob,address;

    public UserInformation(){}

    public UserInformation(String email,String user_name,String password,String mobile_num,String full_name,String dob,String address){
        this.email = email;
        this.user_name = user_name;
        this.password = password;
        this.mobile_num = mobile_num;
        this.full_name = full_name;
        this.dob = dob;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

}
