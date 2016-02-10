package com.boloutaredoubeni.neighborly.views;

import com.boloutaredoubeni.neighborly.models.Location;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Copyright 2016 Boloutare Doubeni
 *
 * A custom marker for the users present location
 */
public class UserLocationOverlayItem extends OverlayItem {

  private Location mLocation;

  /**
   *
   * @param aTitle
   * @param aSnippet
   * @param aGeoPoint
   * @param location
   */
  private UserLocationOverlayItem(String aTitle, String aSnippet,
                                  IGeoPoint aGeoPoint, Location location) {
    super(aTitle, aSnippet, aGeoPoint);
    mLocation = location;
  }

  /**
   * Bind the overlay item to a location
   * @param location
   * @return
   */
  public static OverlayItem bindWith(Location location) {
    return new UserLocationOverlayItem(
        location.getName(), location.getAmenity(),
        location.getCoordinates().asGeoPoint(), location);
  }

  /* TODO: add some custom stuff */
}
