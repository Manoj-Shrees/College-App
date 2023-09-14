package com.dreamhunterztech.cgcstudentportal;

/**
 * Created by Dreamer on 27-06-2017.
 */

public class libmainpagebookdata {
    private String booktype;
    private String book1topic;
    private String book1coverpicurl;
    private String book1auther;
    private String book2topic;
    private String book2coverpicurl;
    private String book2auther;
    private String book3topic;
    private String book3coverpicurl;
    private String book3auther;
    private String book4topic;
    private String book4coverpicurl;
    private String book4auther;
    private String book5topic;
    private String book5coverpicurl;
    private String book5auther;

    public libmainpagebookdata()
    {

    }
    
    public libmainpagebookdata(String booktype,String book1topic,String book1auther,String book1coverpicurl,String book2topic,String book2coverpicurl,String book2auther,String book3topic,String book3coverpicurl,String book3auther,String book4topic,String book4coverpicurl,String book4auther,String book5topic,String book5coverpicurl,String book5auther)
    {
        this.booktype=booktype;
        this.book1topic = book1topic;
        this.book1coverpicurl = book1coverpicurl;
        this.book1auther = book1auther;
        this.book2topic = book2topic;
        this.book2coverpicurl = book2coverpicurl;
        this.book2auther = book2auther;
        this.book3topic = book3topic;
        this.book3coverpicurl = book3coverpicurl;
        this.book3auther = book3auther;
        this.book4topic = book4topic;
        this.book4coverpicurl = book4coverpicurl;
        this.book4auther = book4auther;
        this.book5topic = book5topic;
        this.book5coverpicurl = book5coverpicurl;
        this.book5auther = book5auther;
    }

    public String getBook1topic() {
        return book1topic;
    }

    public String getBooktype() {
        return booktype;
    }

    public String getBook1auther() {
        return book1auther;
    }

    public String getBook1coverpicurl() {
        return book1coverpicurl;
    }

    public String getBook2topic() {
        return book2topic;
    }

    public String getBook2coverpicurl() {
        return book2coverpicurl;
    }

    public String getBook2auther() {
        return book2auther;
    }

    public String getBook3topic() {
        return book3topic;
    }

    public String getBook3coverpicurl() {
        return book3coverpicurl;
    }

    public String getBook3auther() {
        return book3auther;
    }

    public String getBook4topic() {
        return book4topic;
    }

    public String getBook4coverpicurl() {
        return book4coverpicurl;
    }

    public String getBook4auther() {
        return book4auther;
    }


    public String getBook5topic() {
        return book5topic;
    }

    public String getBook5coverpicurl() {
        return book5coverpicurl;
    }

    public String getBook5auther() {
        return book5auther;
    }
}
