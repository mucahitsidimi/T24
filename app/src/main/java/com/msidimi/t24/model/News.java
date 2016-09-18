package com.msidimi.t24.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import org.json.JSONObject;

/**
 * Created by mucahit on 18/09/16.
 */
public class News implements Parcelable {

    private int id;
    private String title;
    private String excerpt;
    private String alias;
    private String imageUrl;
    private String text;

    public News(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id", -1);
        this.title = jsonObject.optString("title", "");
        this.excerpt = jsonObject.optString("excerpt", "");
        this.alias = jsonObject.optString("alias", "");
        this.text = jsonObject.optString("text", "");

        JSONObject imagesJsonObject = jsonObject.optJSONObject("images");
        this.imageUrl = imagesJsonObject.optString("page", "");
    }

    protected News(Parcel in) {
        id = in.readInt();
        title = in.readString();
        excerpt = in.readString();
        alias = in.readString();
        imageUrl = in.readString();
        text = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(excerpt);
        dest.writeString(alias);
        dest.writeString(imageUrl);
        dest.writeString(text);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        if (Build.VERSION.SDK_INT >= 24)
            return Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString();
        else
            return Html.fromHtml(title).toString();
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getAlias() {
        return alias;
    }

    public String getImageUrl() {
        return "https:" + imageUrl;
    }

    public String getText() {
        return text;
    }
}
