package com.boloutaredoubeni.neighborly.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.neighborly.views.CustomMapView;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class MapFragment extends Fragment {

  private final int DEFAULT_ZOOM_LEVEL = 15;

  protected MapView mMapView;
  public MapFragment() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mMapView = new CustomMapView(inflater.getContext());
    return mMapView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mMapView.setMultiTouchControls(true);
    mMapView.setTilesScaledToDpi(true);

    IMapController controller = mMapView.getController();
    controller.setZoom(DEFAULT_ZOOM_LEVEL);
    GeoPoint point = new GeoPoint(40.7398848, -73.9922705);
    controller.setCenter(point);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mMapView.onDetach();
    mMapView = null;
  }
}
