package com.bignerdranch.android.choresmaster;

import java.util.Date;
import java.util.UUID;

public class Chores {

    //Variable
    private UUID cId;
    private String cTitle; //name of the chores
    private Date cDate; //Date of the chores
    private boolean cCompleted; //checkbox to be ticked when chores completed

    //Constructor
    public Chores() {
        //cId = UUID.randomUUID(); //To generate random id
        //cDate = new Date(); //To get the present date
        this(UUID.randomUUID());
    }
    //Constructor
    public Chores(UUID id){
        cId = id;
        cDate = new Date();
    }

    //Setter and getter
    public UUID getId() {
        return cId;
    }
    public void setId(UUID id) {
        cId = id;
    }

    public String getTitle() {
        return cTitle;
    }

    public void setTitle(String title) {
        cTitle = title;
    }

    public Date getDate() {
        return cDate;
    }

    public void setDate(Date date) {
        cDate = date;
    }

    public boolean isCompleted() {
        return cCompleted;
    }

    public void setCompleted(boolean complete) {
        cCompleted = complete;
    }
}
