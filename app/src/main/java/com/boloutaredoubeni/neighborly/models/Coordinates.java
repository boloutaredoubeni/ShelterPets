package com.boloutaredoubeni.neighborly.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.osmdroid.util.GeoPoint;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class Coordinates {
  public final double mLongitude;
  public final double mLatitude;

  final static double DISTANCE = 3218.69;

  Coordinates(final double latitude, final double longitude) {
    mLongitude = longitude;
    mLatitude = latitude;
  }

  public LatLng asLatLng() { return new LatLng(mLatitude, mLongitude); }

  public GeoPoint asGeoPoint() { return new GeoPoint(mLatitude, mLongitude); }

  public List<LatLng> getBox() {
    final List<LatLng> latLngList = new LinkedList<>();
    final LatLng coordinates = this.asLatLng();

    for (Direction direction : Direction.values()) {
      final LatLng position = SphericalUtil.computeOffset(
          coordinates, DISTANCE, direction.getHeading());
      latLngList.add(position);
    }
    return latLngList;
  }
}