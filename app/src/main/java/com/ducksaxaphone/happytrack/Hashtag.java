package com.ducksaxaphone.happytrack;

/**
 * Created by jrh on 18/02/17.
 */
public class Hashtag {
    public float score;
    public String tag;
    public int counts;

    public Hashtag(String name, float firstRating){
        score=firstRating;
        tag=name;
        counts=1;
    }

    public int compare(Hashtag other){
        if (other.score>score){
            return -1;
        }
        else if (other.score < score){
            return 1;
        }
        else{
            return 0;
        }
    }
}

