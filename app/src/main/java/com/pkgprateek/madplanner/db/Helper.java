package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class Helper {
    int id;
    char linkType;
    int link;
    DB.OP op;
    public Helper(DB.OP op, int id, char linkType, int link) {
        this.op = op;
        this.id = id;
        this.linkType = linkType;
        this.link = link;
    }


    public String toString() {
        return "[" + id + "," + linkType + "," + link + "]";
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getLinkType() {
        return linkType;
    }

    public void setLinkType(char linkType) {
        this.linkType = linkType;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }
}
