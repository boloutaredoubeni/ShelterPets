package com.boloutaredoubeni.neighborly.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class Coordinates implements Serializable {

  static final long serialVersionUID = 1L;

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
      LatLng position = SphericalUtil.computeOffset(coordinates, DISTANCE,
                                                    direction.getHeading());
      latLngList.add(position);
    }
    return latLngList;
  }

  // TODO: Dry these out
  public static LatLng getMins(List<LatLng> box) {
    double minLat = Integer.MAX_VALUE;
    double minLon = Integer.MAX_VALUE;

    for (LatLng coordinates : box) {
      if (coordinates.longitude < minLon) {
        minLon = coordinates.longitude;
      }
      if (coordinates.latitude < minLat) {
        minLat = coordinates.latitude;
      }
    }
    return new LatLng(minLat, minLon);
  }

  public static LatLng getMaxes(List<LatLng> box) {
    double maxLat = Integer.MIN_VALUE;
    double maxLon = Integer.MIN_VALUE;

    for (LatLng coordinates : box) {
      if (coordinates.longitude > maxLon) {
        maxLon = coordinates.longitude;
      }
      if (coordinates.latitude > maxLat) {
        maxLat = coordinates.latitude;
      }
    }

    return new LatLng(maxLat, maxLon);
  }
}