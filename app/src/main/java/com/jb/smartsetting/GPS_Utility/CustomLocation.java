package com.jb.smartsetting.GPS_Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by wjdql on 2017-06-17.
 */

public class CustomLocation implements Serializable {
    private static final long serialVersionUID = 8124905464753305656L;
    // 위치정보 변수 정의
    public double indentificationNumber = 0;

    public String locationName;
    public String SoundType = "None";
    public String BluetoothType = "None";
    public String WiFiType = "None";

    public double Latitude, Longitude, Altitude;
    public float Accuracy;


    public boolean isEnabled = false;
    public Bitmap bitmap;
    public String imgFileName;
    public String objFilePath;
    public String objFileName;
    public String address;


    public void parseLocation(android.location.Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Accuracy = location.getAccuracy();
        Altitude = location.getAltitude();
        indentificationNumber = Altitude + Latitude;
        objFileName = Altitude + Latitude + ".sjb";
        imgFileName = Altitude + Latitude + ".png";
        objFilePath = "/data/data/com.jb.smartsetting/files/";
    }

    public String getLocationName() {
        return locationName;
    }

    public void setEnabled(boolean value) {
        isEnabled = value;
    }

    public void setSoundType(String SoundType) {
        this.SoundType = SoundType;
    }

    public void setBluetoothType(String BluetoothType) {
        this.BluetoothType = BluetoothType;
    }

    public void setWiFiType(String WiFiType) {
        this.WiFiType = WiFiType;
    }

    public double toDistance(double lat2, double lon2) {

        double theta, dist;
        theta = this.Longitude - lon2;
        dist = Math.sin(deg2rad(this.Latitude)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(this.Latitude))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    public void setAddress(Context context) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(Latitude, Longitude, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (RuntimeException e){
            Toast.makeText(context, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        this.address = nowAddress;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }
}
