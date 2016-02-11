package com.boloutaredoubeni.neighborly.models;

import java.io.Serializable;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public enum Direction implements Serializable {
  WEST(225),
  SOUTH(180),
  EAST(90),
  NORTH(0);

  private double mHeading;

  static final long serialVersionUID = 1L;

  Direction(double heading) { mHeading = heading; }

  public double getHeading() { return mHeading; }
}
