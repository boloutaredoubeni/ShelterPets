package com.boloutaredoubeni.neighborly.models;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public enum Direction {
  WEST(225),
  SOUTH(180),
  EAST(90),
  NORTH(0);

  private double mHeading;

  Direction(double heading) { mHeading = heading; }

  public double getHeading() { return mHeading; }
}
