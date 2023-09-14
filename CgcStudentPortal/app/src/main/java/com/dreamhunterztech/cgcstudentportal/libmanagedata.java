package com.dreamhunterztech.cgcstudentportal;





public class libmanagedata  {

    private String bookname;
    private String  bookcoverpic;
    private String bookauther;
    private String booktype;
    private String bookdesc;
    private String bookuploaddate;
    private String bookuploader;
    private String bookfileurl;

    libmanagedata()
    {

    }


    libmanagedata(String bookname,String bookcoverpic,String booktype,String bookauther,String bookdesc , String bookuploaddate ,String bookuploader,String bookfileurl)
    {
        this.bookname=bookname;
        this.bookcoverpic =bookcoverpic;
        this.bookauther = bookauther;
        this.booktype = booktype;
        this.bookdesc = bookdesc;
        this.bookuploaddate = bookuploaddate;
        this.bookuploader = bookuploader;
        this.bookfileurl = bookfileurl;

    }

    public String getBookname() {
        return bookname;
    }

    public String getBookcoverpic() {
        return bookcoverpic;
    }

    public String getBookauther() {
        return bookauther;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public String getBookfileurl() {
        return bookfileurl;
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
