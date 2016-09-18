package com.msidimi.t24.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import org.json.JSONObject;

/**
 * Created by mucahit on 18/09/16.
 */
public class Category implements Parcelable {

    private int categoryID;
    private String name;
    private String alias;

    public Category(JSONObject jsonObject) {
        this.categoryID = jsonObject.optInt("id", -1);
        this.name = jsonObject.optString("name", "");
        this.alias = jsonObject.optString("alias", "");
    }

    protected Category(Parcel in) {
        categoryID = in.readInt();
        name = in.readString();
        alias = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryID);
        dest.writeString(name);
        dest.writeString(alias);
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        if (Build.VERSION.SDK_INT >= 24)
            return Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY).toString();
        else
            return Html.fromHtml(name).toString();
    }

    public String getAlias() {
        return alias;
    }
}
