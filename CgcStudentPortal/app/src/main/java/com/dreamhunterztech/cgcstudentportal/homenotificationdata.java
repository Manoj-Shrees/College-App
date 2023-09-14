package com.dreamhunterztech.cgcstudentportal;

/**
 * Created by Dreamer on 27-07-2017.
 */

public class homenotificationdata {

    String content,title,type;

    homenotificationdata()
    {

    }

    homenotificationdata(String content ,String title,String type)
    {
        this.content=content;
        this.title=title;
        this.type=type;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }


    public String getType() {
        return type;
    }
}
