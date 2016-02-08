package com.boloutaredoubeni.neighborly.models;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class Location {

  private final String mName;
  private final Coordinates mCoordinates;

  private Location(String name, Coordinates coordinates) {
    mName = name;
    mCoordinates = coordinates;
  }

  public Coordinates getCoordinates() { return mCoordinates; }

  public static class Builder {
    private final String mName;
    private Coordinates mCoordinates;

    public Builder(String name) { mName = name; }

    public Builder coordinates(double longitude, double latitude) {
      mCoordinates = new Coordinates(longitude, latitude);
      return this;
    }

    public Location build() {
      if (mCoordinates == null) {
        throw new IllegalArgumentException(
            "The location does not have coordinates. Location.Builder.coordinates()?");
      }
      return new Location(mName, mCoordinates);
    }
  }
}
