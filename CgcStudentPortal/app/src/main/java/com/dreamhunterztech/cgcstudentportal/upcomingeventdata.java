package com.dreamhunterztech.cgcstudentportal;

/**
 * Created by Dreamer on 29-07-2017.
 */

public class upcomingeventdata {

    String eventname ,eventdate ,eventtype ,eventopentype,eventimgurl;

    upcomingeventdata()
    {

    }

    upcomingeventdata(String eventname,String eventdate,String eventtype,String eventopentype,String eventimgurl)
    {
        this.eventname = eventname;
        this.eventdate = eventdate;
        this.eventtype = eventtype;
        this.eventopentype = eventopentype;
        this.eventimgurl = eventimgurl;
    }

    public String getEventname() {
        return eventname;
    }

    public String getEventtype() {
        return eventtype;
    }

    public String getEventdate() {
        return eventdate;
    }

    public String getEventimgurl() {
        return eventimgurl;
    }

    public String getEventopentype() {
        return eventopentype;
    }
}
