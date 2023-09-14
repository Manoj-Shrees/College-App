package com.dreamhunterztech.cgcstudentportal;

/**
 * Created by suwas on 24-01-2017.
 */

public class Incampusevents {
        private String incameventname;
        private String incameventdescrp;
        private String incameventtype;
        private String incameventdate;
        private String incameventvenue;
        private String incameventregdate;
        private String incameventreglink;
        private String incameventcontactno;
        private String incameventmsg1;
        private String incameventmsg2;
        private String incameventimgurl;
        public Incampusevents()
        {

        }
        public Incampusevents(String incameventname,String incameventtype,String incameventdescrp,String incameventdate,String incameventvenue,String incameventregdate,String incameventreglink,String incameventcontactno,String incameventmsg1,String incameventmsg2,String incameventimgurl)
        {
            this.incameventname = incameventname;
            this.incameventtype = incameventtype;
            this.incameventdescrp = incameventdescrp;
            this.incameventdate = incameventdate;
            this.incameventvenue = incameventvenue;
            this.incameventregdate = incameventregdate;
            this.incameventreglink = incameventreglink;
            this.incameventcontactno = incameventcontactno;
            this.incameventmsg1 = incameventmsg1;
            this.incameventmsg2 = incameventmsg2;
            this.incameventimgurl = incameventimgurl;
        }


    public String getIncameventname() {
                return incameventname;
        }

    public String getIncameventtype() {
        return incameventtype;
    }

    public String getIncameventdescrp() {
        return incameventdescrp;
    }

    public String getIncameventdate() {
        return incameventdate;
    }

    public String getIncameventvenue() {
        return incameventvenue;
    }

    public String getIncameventregdate() {
        return incameventregdate;
    }

    public String getIncameventreglink() {
        return incameventreglink;
    }

    public String getIncameventcontactno() {
        return incameventcontactno;
    }

    public String getIncameventmsg1() {
        return incameventmsg1;
    }

    public String getIncameventmsg2() {
        return incameventmsg2;
    }

    public String getIncameventimgurl() { return incameventimgurl; }
}

