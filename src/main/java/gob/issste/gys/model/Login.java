package gob.issste.gys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Login {
    private String claveUser;
    private String pwd;
    private String token;

public Login(){}
    public Login(String claveUser, String pwd){
        this.claveUser = claveUser;
        this.pwd = pwd;
    }
    public Login(String claveUser, String pwd, String token) {
        this.claveUser = claveUser;
        this.pwd = pwd;
        this.token = token;
    }

    public String getClaveUser() {
        return claveUser;
    }

    public void setClaveUser(String claveUser) {
        this.claveUser = claveUser;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

