package com.boloutaredoubeni.neighborly.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.neighborly.activities.MainActivity;
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
    // TODO: get current location from the main activity

    List<OverlayItem> items = new ArrayList<>();
    // TODO: Add points based on the location from the server || db if valid
    // FIXME: Handle a null value
    Location location =
        (Location)getArguments().getSerializable(MainActivity.USER_LOCATION);
    controller.setCenter(location != null
                             ? location.getCoordinates().asGeoPoint()
                             : new GeoPoint(40.7398848, -73.9922705));
    items.add(LocationOverlayItem.bindWith(location));

    mOverlay = new ItemizedOverlayWithFocus<>(
        items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
          @Override
          public boolean onItemSingleTapUp(final int index,
                                           final OverlayItem item) {
            Log.d(TAG, "Single tapped");
            // TODO: display info in bottom screen
            // 1. Send data to the detail activity
            // 2. pop the previous frag if its a detail for a marker
            // 3. push the previous if its a summary frag
            return true;
          }
          @Override
          public boolean onItemLongPress(final int index,
                                         final OverlayItem item) {
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
