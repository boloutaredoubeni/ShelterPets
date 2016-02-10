package com.boloutaredoubeni.neighborly.views;

import com.boloutaredoubeni.neighborly.models.Location;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class LocationOverlayItem extends OverlayItem {

  private Location mLocation;

  private LocationOverlayItem(String aTitle, String aSnippet,
                              IGeoPoint aGeoPoint, Location location) {
    super(aTitle, aSnippet, aGeoPoint);
    mLocation = location;
  }

  public static OverlayItem bindWith(Location location) {
    return new LocationOverlayItem(location.getName(), location.getAmenity(),
                                   location.getCoordinates().asGeoPoint(),
                                   location);
  }

  /* TODO: add some custom stuff */
}
