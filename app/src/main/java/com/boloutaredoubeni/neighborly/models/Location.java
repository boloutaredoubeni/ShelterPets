package com.boloutaredoubeni.neighborly.models;

import android.support.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class Location {

  private final String mName;
  private final Coordinates mCoordinates;
  private String mAmenity; // or shop if they are different
  private String mHours;
  private URL mURL;
  private Location(String name, Coordinates coordinates) {
    mName = name;
    mCoordinates = coordinates;
  }

  public String getName() { return mName; }

  public Coordinates getCoordinates() { return mCoordinates; }

  @Nullable
  public String getHours() {
    return mHours;
  }

  @Nullable
  public String getAmenity() {
    return mAmenity;
  }

  @Nullable
  public URL getURL() {
    return mURL;
  }

  public static class Builder {
    private String mName;
    private Coordinates mCoordinates;
    private String mAmenity;
    private String mHours;
    private URL mURL;

    public Builder name(String name) {
      mName = name;
      return this;
    }

    public Builder coordinates(double latitude, double longitude) {
      mCoordinates = new Coordinates(latitude, longitude);
      return this;
    }

    public Builder amenity(String amenityName) {
      mAmenity = amenityName;
      return this;
    }

    /** take in a dash seperated string, this will be changed to handle
     * exceptional cases */
    public Builder hours(String hours) {
      mHours = hours;
      return this;
    }

    public Builder url(URL url) {
      if (mURL == null) {
        mURL = url;
      }
      return this;
    }

    public Builder url(String url) {
      if (mURL == null) {
        try {
          mURL = new URL(url);
        } catch (MalformedURLException ex) { /* TODO: handle this better */
        }
      }
      return this;
    }

    public Location build() {
      if (mCoordinates == null) {
        throw new IllegalArgumentException(
            "The location does not have coordinates. Did you call Location.Builder.coordinates()?");
      }
      if (mName == null) {
        throw new IllegalArgumentException(
            "The name is not set. Did you call Location.Builder.name()?");
      }

      final Location location = new Location(mName, mCoordinates);
      if (mAmenity != null && !mAmenity.isEmpty()) {
        location.mAmenity = mAmenity;
      }
      if (mHours != null && !mHours.isEmpty()) {
        location.mHours = mHours;
      }
      if (mURL != null) {
        location.mURL = mURL;
      }
      return location;
    }
  }
}
