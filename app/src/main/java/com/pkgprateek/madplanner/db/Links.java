package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class Links {
    int id;
    String title;
    String link;
    DB.OP op;
    public Links(DB.OP op, int id, String title, String link) {
        this.op = op;
        this.id = id;
        this.title = title;
        this.link = link;
    }


    public String toString() {
        return "[" + id + "," + title + "," + link + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }
}
