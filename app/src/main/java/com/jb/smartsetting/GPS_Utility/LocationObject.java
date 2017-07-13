package com.jb.smartsetting.GPS_Utility;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.security.Provider;

/**
 * Created by wjdql on 2017-06-17.
 */

public class LocationObject extends Location implements Parcelable{
    //private static final long serialVersionUID = 8124905464753305656L;
    public double indentificationNumber;
    public String locationName;

    public int ringtone = 0;
    public int notification = 0;
    public int touchfeedback = 0;
    public int media = 0;

    public boolean isEnabled = false;
    public Location location;
    public Bitmap bitmap;
    public String imgFileName;
    public String objFilePath;
    public String objFileName;

    public LocationObject(Location location ){
        super(location);
        objFileName = location.getLatitude()+location.getLongitude()+".sjb";
        imgFileName = location.getLatitude()+location.getLongitude()+".png";
        indentificationNumber = location.getLatitude()+location.getLongitude();
        objFilePath = "/data/data/com.jb.smartsetting/files/";
    }

    protected LocationObject(Parcel in) {
        indentificationNumber = in.readDouble();
        locationName = in.readString();
        ringtone = in.readInt();
        notification = in.readInt();
        touchfeedback = in.readInt();
        media = in.readInt();
        isEnabled = in.readByte() != 0;
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        imgFileName = in.readString();
        objFilePath = in.readString();
        objFileName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(indentificationNumber);
        dest.writeString(locationName);
        dest.writeInt(ringtone);
        dest.writeInt(notification);
        dest.writeInt(touchfeedback);
        dest.writeInt(media);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeParcelable(bitmap, flags);
        dest.writeString(imgFileName);
        dest.writeString(objFilePath);
        dest.writeString(objFileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationObject> CREATOR = new Creator<LocationObject>() {
        @Override
        public LocationObject createFromParcel(Parcel in) {
            return new LocationObject(in);
        }

        @Override
        public LocationObject[] newArray(int size) {
            return new LocationObject[size];
        }
    };

    public void initLocation(){

    }

    public String getLocationName(){
        return locationName;
    }

    public void setEnabled(boolean value){
        isEnabled = value;
    }

    public void setSoundVolume(int ringtone, int notification, int touchfeedback, int media){
        this.ringtone = ringtone;
        this.notification = notification;
        this.touchfeedback = touchfeedback;
        this.media = media;
    }

}
