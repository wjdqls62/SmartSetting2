package com.jb.smartsetting.GPS_Utility;

import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wjdql on 2017-06-17.
 */

public class Location extends android.location.Location implements Parcelable {
    //private static final long serialVersionUID = 8124905464753305656L;
    public double indentificationNumber;
    public String locationName;

    public int ringtone = 0;
    public int notification = 0;
    public int touchfeedback = 0;
    public int media = 0;

    public boolean isEnabled = false;
    public String imgFileName;
    public String objFilePath;
    public String objFileName;

    public Location(String provider){
        super(provider);

    }

    protected Location(Parcel in) {
        super(LocationManager.NETWORK_PROVIDER);
        indentificationNumber = in.readDouble();
        locationName = in.readString();
        ringtone = in.readInt();
        notification = in.readInt();
        touchfeedback = in.readInt();
        media = in.readInt();
        isEnabled = in.readByte() != 0;
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
        dest.writeString(imgFileName);
        dest.writeString(objFilePath);
        dest.writeString(objFileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public void init(){
        objFileName = getLatitude()+getLongitude()+".sjb";
        imgFileName = getLatitude()+getLongitude()+".png";
        indentificationNumber = getLatitude()+getLongitude();
        objFilePath = "/data/data/com.jb.smartsetting/files/";
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

    public String getLocationName(){
        return locationName;
    }

}
