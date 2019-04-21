package com.example.comp7855;

import android.location.Location;
import android.media.ExifInterface;

import java.io.IOException;
import java.util.ArrayList;

public class RunSearch {

    private ArrayList<String> imageGallery;

    public ArrayList<String> run(ArrayList<String> photoGallery, String fromDate, String toDate,
                                 String keyWord, String latitude, String longitude, String distance) throws IOException {

        imageGallery = photoGallery;

        if (!fromDate.equals("") || !toDate.equals(""))
            if(imageGallery.size() > 0)
                parseDate(fromDate, toDate);

        if (!keyWord.equals(""))
            if(imageGallery.size() > 0)
                parseKeyword(keyWord);

        if (!latitude.equals("") || !longitude.equals(""))
            if(imageGallery.size() > 0)
                parseLocation(latitude, longitude, distance);

        return imageGallery;
    }

    private void parseDate(String from, String to) {
        String temp;
        int i = 0;
        long fromInt, toInt, tempInt = 0;

        while (i < imageGallery.size()) {
            temp = imageGallery.get(i);
            if (!temp.contains("JPEG_")) {
                imageGallery.remove(i);
                i--;
            }
            i++;
        }

        i = 0;

        if (!from.equals(""))
            fromInt = Long.parseLong(from);
        else
            fromInt = Long.MIN_VALUE;

        if (!to.equals(""))
            toInt = Long.parseLong(to);
        else
            toInt = Long.MAX_VALUE;

        while (i < imageGallery.size()) {
            temp = imageGallery.get(i);
            String[] temp_parsed = temp.split("_", 4);
            if (temp_parsed.length > 3) {
                temp = temp_parsed[1] + temp_parsed[2];
                tempInt = Long.parseLong(temp);
            }
            if (tempInt < fromInt || tempInt > toInt) {
                imageGallery.remove(i);
                i--;
            }
            i++;
        }
    }

    private void parseKeyword(String keyWord) throws IOException {
        String temp;
        ExifInterface exif;
        int i = 0;

        while (i < imageGallery.size()) {
            exif = new ExifInterface(imageGallery.get(i));
            temp = exif.getAttribute(ExifInterface.TAG_USER_COMMENT);
            if (!temp.contains(keyWord)) {
                imageGallery.remove(i);
                i--;
            }
            i++;
        }
    }

    private void parseLocation(String lat, String lon, String dist) throws IOException {
        int i = 0;
        Location photo = new Location("");
        Location search = new Location("");
        float[] latLongPhoto = {0,0};
        float temp_dist;
        float latFloat = Float.parseFloat(lat);
        float longFloat = Float.parseFloat(lon);
        float distFloat = Float.parseFloat(dist);
        ExifInterface exif;
        search.setLatitude(latFloat);
        search.setLongitude(longFloat);

        while (i < imageGallery.size()){
            exif = new ExifInterface(imageGallery.get(i));
            if (exif.getLatLong(latLongPhoto)){
                photo.setLatitude(latLongPhoto[0]);
                photo.setLongitude(latLongPhoto[1]);
                temp_dist = photo.distanceTo(search);
                if (temp_dist > distFloat){
                    imageGallery.remove(i);
                    i--;
                }
            }
            else{
                imageGallery.remove(i);
                i--;
            }
            i++;
        }
    }
}