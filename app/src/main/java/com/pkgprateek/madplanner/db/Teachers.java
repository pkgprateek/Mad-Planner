package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 07/11/15.
 */
public class Teachers {
    int id;
    String name;
    DB.OP op;
    public Teachers(DB.OP op,int id, String name) {
        this.id = id;
        this.op = op;
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
