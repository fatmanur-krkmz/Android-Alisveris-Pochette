package com.fnmeinss.pochette;

public class Users {
    public Users(){
    }

    public Users(
                String userEmail,
                String userName,
                String userSurname,
                String userNumber,
                String userAdress)
    {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userNumber = userNumber;
        this.userAdress = userAdress;
    }
    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getuserSurname() {
        return userSurname;
    }

    public void setuserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getuserNumber() {
        return userNumber;
    }

    public void setuserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getuserAdress() {
        return userAdress;
    }

    public void setuserAdress(String userAdress) {
        this.userAdress = userAdress;
    }

    public String getuserEmail() {
        return userEmail;
    }

    public void setuserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private String userEmail;
    private String userName;
    private String userSurname;
    private String userNumber;
    private String userAdress;

}
