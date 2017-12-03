package com.ducksaxophone.happytrack;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


/**
 * Created by jrh on 22/01/17.
 */

public class Day extends BaseObservable{
    private long id;
    private String date;
    private Integer rating;
    private String notes;

    public Day(){}

    public Day(String today){
        this.id=0;
        this.date=today;
        this.rating=5;
        this.notes=null;
    }

    @Bindable
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Bindable
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
