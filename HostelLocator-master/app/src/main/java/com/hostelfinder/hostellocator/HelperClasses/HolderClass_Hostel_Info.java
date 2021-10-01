package com.hostelfinder.hostellocator.HelperClasses;

import java.util.Comparator;

public class HolderClass_Hostel_Info {

    HolderClass_Hostel_Info() {

    }

    public String hostelname;
    public String hostelcity;
    public String hosteldescription;
    public String wardenphonenumber;
    public String hostelimage;
    public String wardenName;
    public String wardenEmail;
    public String wardenPassword;
    public String wardenUid;
    public String completeLocationAddress;
    /*  String distancecalculated;*/

    double longitude;
    double latitude;

    public HolderClass_Hostel_Info(String hostelname,
                                   String hostelcity,
                                   String wardenphonenumber,
                                   String hosteldescription,
                                   double longitude,
                                   double latitude,
                                   String hostelimage,
                                   String wardenName,
                                   String wardenEmail,
                                   String wardenPassword,
                                   String wardenUid,
                                   String completeLocationAddress) {
        this.hostelname = hostelname;
        this.hostelcity = hostelcity;
        this.hosteldescription = hosteldescription;
        this.longitude = longitude;
        this.latitude = latitude;
        this.wardenphonenumber = wardenphonenumber;
        this.hostelimage = hostelimage;
        this.wardenEmail = wardenEmail;
        this.wardenPassword = wardenPassword;
        this.wardenName = wardenName;
        this.wardenUid = wardenUid;
        this.completeLocationAddress = completeLocationAddress;
    }


    public String getWardenUid() {
        return wardenUid;
    }

    public void setWardenUid(String wardenUid) {
        this.wardenUid = wardenUid;
    }

    public String getWardenName() {
        return wardenName;
    }

    public void setWardenName(String wardenName) {
        this.wardenName = wardenName;
    }

    public String getWardenEmail() {
        return wardenEmail;
    }

    public void setWardenEmail(String wardenEmail) {
        this.wardenEmail = wardenEmail;
    }

    public String getWardenPassword() {
        return wardenPassword;
    }

    public void setWardenPassword(String wardenPassword) {
        this.wardenPassword = wardenPassword;
    }

    public String getHostelimage() {
        return hostelimage;
    }

    public void setHostelimage(String hostelimage) {
        this.hostelimage = hostelimage;
    }

    public String getHostelname() {
        return hostelname;
    }

    public void setHostelname(String hostelname) {
        this.hostelname = hostelname;
    }

    public String getWardenphonenumber() {
        return wardenphonenumber;
    }

    public void setWardenphonenumber(String wardenphonenumber) {
        this.wardenphonenumber = wardenphonenumber;
    }

    public String getHostelcity() {
        return hostelcity;
    }

    public void setHostelcity(String hostelcity) {
        this.hostelcity = hostelcity;
    }

    public String getHosteldescription() {
        return hosteldescription;
    }

    public void setHosteldescription(String hosteldescription) {
        this.hosteldescription = hosteldescription;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCompleteLocationAddress() {
        return completeLocationAddress;
    }

    public void setCompleteLocationAddress(String completeLocationAddress) {
        this.completeLocationAddress = completeLocationAddress;
    }


}
