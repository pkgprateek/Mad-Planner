package com.pkgprateek.madplanner;

/**
 * Created by pkgprateek on 15/11/15.
 */
public class MenuAdapterItem {
    public String title = "";
    public String comment = "";
    public boolean notifier = false;
    public int notifications = 0;
    public int drawable = 0;

    public MenuAdapterItem(String title, String comment) {
        this.title = title;
        this.comment = comment;
        notifier = false;
        notifications = 0;
    }
    public MenuAdapterItem(String title, String comment, boolean notifier, int notifications) {
        this.title = title;
        this.comment = comment;
        this.notifier = notifier;
        this.notifications = notifications;
    }
    public MenuAdapterItem(String title, String comment, int drawable) {
        this.title = title;
        this.comment = comment;
        notifier = false;
        notifications = 0;
        this.drawable = drawable;
    }
    public MenuAdapterItem(String title, String comment,boolean notifier, int notifications, int drawable) {
        this.title = title;
        this.comment = comment;
        this.notifier = notifier;
        this.notifications = notifications;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isNotifier() {
        return notifier;
    }

    public void setNotifier(boolean notifier) {
        this.notifier = notifier;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }


    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
