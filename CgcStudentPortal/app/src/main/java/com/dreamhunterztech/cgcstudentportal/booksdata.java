package com.dreamhunterztech.cgcstudentportal;

/**
 * Created by Dreamer on 30-10-2017.
 */

public class booksdata {
    String bookauther;
    String bookcoverpic;
    String bookdesc;
    String bookfileurl;
    String booktype;
    String bookname;
    String bookuploaddate;
    String bookuploader;

    booksdata()
    {

    }

    booksdata(String bookauther , String bookcoverpic , String bookdesc , String bookfileurl , String bookname , String booktype , String bookuploaddate , String bookuploader)
    {
        this.bookauther = bookauther;
        this.bookcoverpic = bookcoverpic;
        this.bookdesc = bookdesc;
        this.bookfileurl = bookfileurl;
        this.bookname = bookname;
        this.booktype = booktype;
        this.bookuploaddate = bookuploaddate;
        this.bookuploader = bookuploader;
    }

    public String getBookauther() {
        return bookauther;
    }

    public String getBookcoverpic() {
        return bookcoverpic;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public String getBookfileurl() {
        return bookfileurl;
    }

    public String getBookname() {
        return bookname;
    }

    public String getBooktype() {
        return booktype;
    }

    public String getBookuploaddate() {
        return bookuploaddate;
    }

    public String getBookuploader() {
        return bookuploader;
    }
}
