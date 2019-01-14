package com.msa.quizapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Quiz implements Parcelable{
    private String qid;
    private String qname;
    private String qimageurl;
    private String qcategory;
    private String qdifficulty;
    private String qamount;
    private String qurl;

    public Quiz() {
    }

    public Quiz(String qid, String qname, String qimageurl, String qcategory, String qdifficulty, String qamount, String qurl) {
        this.qid = qid;
        this.qname = qname;
        this.qimageurl = qimageurl;
        this.qcategory = qcategory;
        this.qdifficulty = qdifficulty;
        this.qamount = qamount;
        this.qurl = qurl;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };


    // Parcelling part
    public Quiz(Parcel in){
        this.qid = in.readString();
        this.qname = in.readString();
        this.qimageurl = in.readString();
        this.qcategory= in.readString();
        this.qdifficulty = in.readString();
        this.qamount = in.readString();
        this.qurl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.qid);
        dest.writeString(this.qname);
        dest.writeString(this.qimageurl);
        dest.writeString(this.qcategory);
        dest.writeString(this.qdifficulty);
        dest.writeString(this.qamount);
        dest.writeString(this.qurl);
    }

    public String getQid() {
        return qid;
    }

    public String getQname() {
        return qname;
    }

    public String getQimageurl() {
        return qimageurl;
    }

    public String getQcategory() {
        return qcategory;
    }

    public String getQdifficulty() {
        return qdifficulty;
    }

    public String getQamount() {
        return qamount;
    }

    public String getQurl() {
        return qurl;
    }
}