package com.boloutaredoubeni.neighborly.views;

import android.content.Context;

import org.osmdroid.views.MapView;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class CustomMapView extends MapView {

  public CustomMapView(final Context context) { super(context); }

  @Override
  public void scrollTo(int x, int y) {
    // TODO: disable touch if the other view is narrower that the map, i.e. it's
    // overlayed
    super.scrollTo(x, y);
  }
}
