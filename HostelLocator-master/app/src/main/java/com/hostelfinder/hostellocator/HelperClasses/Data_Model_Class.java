package com.hostelfinder.hostellocator.HelperClasses;

import android.text.Layout;
import android.widget.RelativeLayout;

public class Data_Model_Class {

    Data_Model_Class(){

    }

    /*this is a model class and here all the members of your xml
            file (hostel view in recylcer view ) are defined here and
            also have getter and setter methods defined*/

    public String HostelName;
    public String HostelDescription;
    public String CityName;
    public int HostelImage;
   /* private float Ratings;*/
    public float Distance;
    RelativeLayout relativeLayout;

    public String getHostelName() {
        return HostelName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public void setHostelName(String hostelName) {
        HostelName = hostelName;
    }

    public String getHostelDescription() {
        return HostelDescription;
    }

    public void setHostelDescription(String hostelDescription) {
        HostelDescription = hostelDescription;
    }


    public int getHostelImage() {
        return HostelImage;
    }

    public void setHostelImage(int hostelImage) {
        HostelImage = hostelImage;
    }

    public float getDistance() {
        return Distance;
    }
    public void setDistance() {
        Distance = Distance;
    }

    /*public void setRatings(float ratings) {
        Ratings = ratings;
    }
    public float getRatings() {
        return Ratings;
    }*/
}
