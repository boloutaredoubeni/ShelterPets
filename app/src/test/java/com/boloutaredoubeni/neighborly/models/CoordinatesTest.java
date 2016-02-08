package com.boloutaredoubeni.neighborly.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class CoordinatesTest {

  Coordinates coordinates;

  @Before
  public void setup() {
    coordinates = new Coordinates(0, 0);
  }

  @Test
  public void buildsABoxWithFourCorners() {
    final List<LatLng> latLngs = coordinates.getBox();

    // Verify the lengths are the same
    assertEquals(latLngs.size(), Direction.values().length);
    assertTrue(checkCorners(latLngs));
  }

  private boolean checkCorners(List<LatLng> latLngs) {
    final Direction[] directions = Direction.values();
    for (int i = 0; i < latLngs.size(); ++i) {
      final Direction direction = directions[i];
      final LatLng point = SphericalUtil.computeOffset(
          coordinates.asLatLng(), Coordinates.DISTANCE, direction.getHeading());
      if (!latLngs.get(i).equals(point)) {
        return false;
      }
    }
    return true;
  }
}
