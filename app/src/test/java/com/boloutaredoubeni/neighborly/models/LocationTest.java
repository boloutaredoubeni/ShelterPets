package com.boloutaredoubeni.neighborly.models;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class LocationTest {

  Location location;

  @Test(expected = IllegalArgumentException.class)
  public void builderRequiresValidParams() {
    location = new Location.Builder("Some where over the rainbow").build();
  }

  @Test
  public void locationBuilderCreatesValidObjects() {
    location =
        new Location.Builder("Some other place").coordinates(0, 0).build();
    assertNotNull(location);
  }
}
