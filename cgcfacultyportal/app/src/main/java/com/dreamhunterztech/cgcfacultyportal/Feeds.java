package com.dreamhunterztech.cgcfacultyportal;

/**
 * Created by suwas on 30-12-2016.
 */

public class Feeds
{
    private String feedtopic;
    private String feedcontext;
    private String feeddate;
    private String feedtime;
    private String feedauther;
    private String feedimgurl;

    public Feeds()
    {

    }

    public Feeds(String feedtopic,String feedcontext,String feeddate,String feedtime,String feedauther,String feedimgurl)
    {
        this.feedtopic = feedtopic;
        this.feedcontext = feedcontext;
        this.feeddate = feeddate;
        this.feedtime = feedtime;
        this.feedauther = feedauther;
        this.feedimgurl = feedimgurl;
    }

    public String getFeedtopic()
    {
        return feedtopic;
    }

    public String getFeedcontext()
    {
        return feedcontext;
    }

    public String getFeeddate()
    {
        return feeddate;
    }

    public String getFeedtime()
    {
        return feedtime;
    }

    public String getFeedauther()
    {
        return feedauther;
    }

    public String getFeedimgurl() {
        return feedimgurl;
    }

}
