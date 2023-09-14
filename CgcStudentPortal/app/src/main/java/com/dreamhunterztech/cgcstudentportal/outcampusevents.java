package com.dreamhunterztech.cgcstudentportal;

/**
 * Created by suwas on 26-01-2017.
 */

public class outcampusevents {

    private String outcameventname;
    private String outcameventdescrp;
    private String outcameventtype;
    private String outcameventdate;
    private String outcameventvenue;
    private String outcameventregdate;
    private String outcameventreglink;
    private String outcameventcontactno;
    private String outcameventmsg1;
    private String outcameventmsg2;
    private String outcameventimgurl;
    public outcampusevents()
    {

    }
    public outcampusevents(String outcameventname,String outcameventtype,String outcameventdescrp,String outcameventdate,String outcameventvenue,String outcameventregdate,String outcameventreglink,String outcameventcontactno,String outcameventmsg1,String outcameventmsg2,String outcameventimgurl)
    {
        this.outcameventname = outcameventname;
        this.outcameventtype = outcameventtype;
        this.outcameventdescrp = outcameventdescrp;
        this.outcameventdate = outcameventdate;
        this.outcameventvenue = outcameventvenue;
        this.outcameventregdate = outcameventregdate;
        this.outcameventreglink = outcameventreglink;
        this.outcameventcontactno = outcameventcontactno;
        this.outcameventmsg1 = outcameventmsg1;
        this.outcameventmsg2 = outcameventmsg2;
        this.outcameventimgurl = outcameventimgurl;
    }

    public String getOutcameventname() {
        return outcameventname;
    }

    public String getOutcameventtype() {
        return outcameventtype;
    }

    public String getOutcameventdescrp() {
        return outcameventdescrp;
    }

    public String getOutcameventdate() {
        return outcameventdate;
    }

    public String getOutcameventimgurl() {
        return outcameventimgurl;
    }

    public String getOutcameventmsg1() {
        return outcameventmsg1;
    }

    public String getOutcameventmsg2() {
        return outcameventmsg2;
    }

    public String getOutcameventregdate() {
        return outcameventregdate;
    }

    public String getOutcameventreglink() {
        return outcameventreglink;
    }

    public String getOutcameventvenue() {
        return outcameventvenue;
    }

    public String getOutcameventcontactno() {
        return outcameventcontactno;
    }
}
