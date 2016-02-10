package com.boloutaredoubeni.neighborly.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.neighborly.models.Location;
import com.boloutaredoubeni.neighborly.views.CustomMapView;
import com.boloutaredoubeni.neighborly.views.LocationOverlayItem;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class MapFragment extends Fragment {

  private final String TAG = MapFragment.class.getCanonicalName();
  private final int DEFAULT_ZOOM_LEVEL = 15;

  protected MapView mMapView;
  protected ItemizedOverlayWithFocus<OverlayItem> mOverlay;
  private ResourceProxy mResourceProxy;
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
    mResourceProxy = new ResourceProxyImpl(getActivity());

    IMapController controller = mMapView.getController();
    controller.setZoom(DEFAULT_ZOOM_LEVEL);
    GeoPoint point = new GeoPoint(40.7398848, -73.9922705);
    controller.setCenter(point);

    List<OverlayItem> items = new ArrayList<>();
    // TODO: Add points based on the location from the server || db if valid
    Location location =
        new Location.Builder()
            .name("GA")
            .amenity("school")
            .coordinates(point.getLatitude(), point.getLongitude())
            .build();
    items.add(LocationOverlayItem.bindWith(location));

    mOverlay = new ItemizedOverlayWithFocus<>(items,
        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
          @Override
          public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
            Log.d(TAG, "Single tapped");
            // TODO: display info in bottom screen
            return true;
          }
          @Override
          public boolean onItemLongPress(final int index, final OverlayItem item) {
            Log.d(TAG, "Long Press");
            return false;
          }
        }, mResourceProxy);
    mOverlay.setFocusItemsOnTap(true);

    mMapView.getOverlays().add(mOverlay);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mMapView.onDetach();
    mMapView = null;
  }
}
