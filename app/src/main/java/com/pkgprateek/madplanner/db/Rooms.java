package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 07/11/15.
 */
public class Rooms {
    int id;
    String name;
    DB.OP op;
    public Rooms(DB.OP op,int id, String name) {
        this.op = op;
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return "[" + id + "," + name + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }
}
