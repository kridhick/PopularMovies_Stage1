package com.example.udacity.karthikeyan.mypopularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class ResultReview implements Parcelable{

    @SerializedName("author")
    @Expose
    private String author;


    @SerializedName("content")
    @Expose
    private String content;


    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("url")
    @Expose
    private String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResultReview() {

    }

    public ResultReview(String author, String content, String id, String url) {
        super();
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    protected  ResultReview(Parcel in) {
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
    }


    public final static Parcelable.Creator<ResultReview> CREATOR = new Creator<ResultReview>() {
        @SuppressWarnings({"unchecked"})
        public ResultReview createFromParcel(Parcel in) {
            return new ResultReview(in);
        }

        public ResultReview[] newArray(int size) {
            return (new ResultReview[size]);
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(author);
        dest.writeValue(content);
        dest.writeValue(id);
        dest.writeValue(url);
    }
}





