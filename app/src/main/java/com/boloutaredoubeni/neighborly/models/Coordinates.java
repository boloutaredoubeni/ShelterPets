package com.boloutaredoubeni.neighborly.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class Coordinates {
  public final double mLongitude;
  public final double mLatitude;

  final static double DISTANCE = 3218.69;

  Coordinates(final double longitude, final double latitude) {
    mLongitude = longitude;
    mLatitude = latitude;
  }

  public LatLng asLatLng() { return new LatLng(mLongitude, mLatitude); }

  /* package */ List<LatLng> getBox() {
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