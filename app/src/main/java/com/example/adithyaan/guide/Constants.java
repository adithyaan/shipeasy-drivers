package com.example.adithyaan.guide;

import java.util.ArrayList;

public class Constants {

    public static String Url = "https://wse.cit.api.here.com/2/findsequence.json?start=Thane;19.186854,72.975447&destination1=Airoli;19.158854,72.999298&destination2=Rabale;19.138403,73.001384&destination3=Ghansoli;19.116198,73.005233&mode=fastest;truck&app_id=VCxfv5dHl0Dy3KT04y0x&app_code=2BZJjuyTG0paa5LNkK0qPg";

    public  static final String REQUEST_METHOD = "GET";

    public  static final int READ_TIMEOUT = 15000;

    public   static final int CONNECTION_TIMEOUT = 15000;

    static final int Handle_Time = 2000;

    static final int Quick_Handle_Time = 1000;

    static final int Enter_Animation = 0;

    static final int Exit_Animation = 0;

    static final String Next_Page = "Next Page";

    static final String []Spinner = {"3", "5", "10", "20"};

//    static int id[] = {1, 2};
//
//    static String title[] = {"Google", "Yahoo"};
//
//    static String message[] = {"Google Webpage", "Yahoo Webpage"};
//
//    static String url[] = {"http://www.google.com", "http://www.yahoo.com"};

    static int array_Length = 0;

    static ArrayList<Integer> ID = new ArrayList<>();

    static ArrayList<String> Title = new ArrayList<>();

    static ArrayList<String> Message = new ArrayList<>();

    public static ArrayList<String> URL = new ArrayList<>();

    static ArrayList<String> Date_Time = new ArrayList<>();

    public static ArrayList<String> Location = new ArrayList<>();

    //    static int check = 0;
    public static ArrayList<NewsFeedData> post=new ArrayList<NewsFeedData>();

    public static String LoadUrl;

    static String from;

    static String to;

    static String goods;

    public static ArrayList<String> Name = new ArrayList<>();

    public static ArrayList<Double> Latitude = new ArrayList<>();

    public static ArrayList<Double> Longitude = new ArrayList<>();

    public static final String name = "Hackerz";

}
