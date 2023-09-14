package com.dreamhunterztech.cgcfacultyportal;

/**
 * Created by Dreamer on 31-07-2017.
 */

public class repstatusdata {

    String Reporttopic,Reportcontent, Reporttype,Reportstatus,Reportby;

    repstatusdata()
    {

    }

    repstatusdata(String Reporttopic, String Reportcontent, String Reporttype, String Reportstatus, String Reportby)
    {
        this.Reporttopic=Reporttopic;
        this.Reportcontent=Reportcontent;
        this.Reporttype=Reporttype;
        this.Reportstatus=Reportstatus;
        this.Reportby=Reportby;

    }

    public String getReporttopic() {
        return Reporttopic;
    }

    public String getReportcontext() {
        return Reportcontent;
    }

    public String getReporttype() {
        return Reporttype;
    }

    public String getReportStatus() {
        return Reportstatus;
    }

    public String getReportby() {
        return Reportby;
    }
}
