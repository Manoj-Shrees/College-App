package com.dreamhunterztech.cgcstudentportal;

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
    private String feedtype;

    public Feeds()
    {

    }

    public Feeds(String feedtopic,String feedcontext,String feeddate,String feedtime,String feedauther,String feedimgurl,String feedtype)
    {
        this.feedtopic = feedtopic;
        this.feedcontext = feedcontext;
        this.feeddate = feeddate;
        this.feedtime = feedtime;
        this.feedauther = feedauther;
        this.feedimgurl = feedimgurl;
        this.feedtype = feedtype;
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

    public String getFeedtype() {
        return feedtype;
    }
}
