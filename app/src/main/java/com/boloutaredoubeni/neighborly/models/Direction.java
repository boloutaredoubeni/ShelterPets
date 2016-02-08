package com.boloutaredoubeni.neighborly.models;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public enum Direction {
  NORTHEAST(45),
  SOUTHEAST(135),
  SOUTHWEST(225),
  NORTHWEST(315);

  private double mHeading;

  Direction(double heading) { mHeading = heading; }

  public double getHeading() { return mHeading; }
}
