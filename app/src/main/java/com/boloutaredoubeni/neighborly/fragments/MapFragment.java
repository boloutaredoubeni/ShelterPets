package com.boloutaredoubeni.neighborly.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.neighborly.activities.MainActivity;
import com.boloutaredoubeni.neighborly.models.Location;
import com.boloutaredoubeni.neighborly.views.UserLocationOverlayItem;

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

  public static final String TAG = MapFragment.class.getCanonicalName();
  private final int DEFAULT_ZOOM_LEVEL = 15;

  private List<Location> mPlaces;

  protected MapView mMapView;
  protected ItemizedOverlayWithFocus<OverlayItem> mOverlay;
  protected List<OverlayItem> mOverlayItems;
  private ResourceProxy mResourceProxy;
  private IMapController mController;

  /* package */ OnOverlayItemClickedListener mOnOverlayItemClickedListener;

  public MapFragment() {}

  public IMapController getMapController() { return mController; }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mMapView = new MapView(inflater.getContext());
    return mMapView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mMapView.setMultiTouchControls(true);
    mMapView.setTilesScaledToDpi(true);
    mResourceProxy = new ResourceProxyImpl(getActivity());

    mController = mMapView.getController();
    mController.setZoom(DEFAULT_ZOOM_LEVEL);
    List<Location> places = ((MainActivity)getActivity()).getPlaces();
    mOverlayItems = new ArrayList<>();
    // TODO: Add points based on the location from the server || db if valid
    // FIXME: Handle a null value
    final Location location =
        (Location)getArguments().getSerializable(MainActivity.USER_LOCATION);
    mController.setCenter(location != null
                              ? location.getCoordinates().asGeoPoint()
                              : new GeoPoint(40.7398848, -73.9922705));
    mOverlayItems.add(UserLocationOverlayItem.bindWith(location));

    mOverlay = new ItemizedOverlayWithFocus<>(
        mOverlayItems,
        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
          @Override
          public boolean onItemSingleTapUp(final int index,
                                           final OverlayItem item) {
            Log.d(TAG, "Single tapped");
            sendLocationData(location);
            // TODO: replace the map with the photo fragment
            return true;
          }
          @Override
          public boolean onItemLongPress(final int index,
                                         final OverlayItem item) {
            Log.d(TAG, "Long Press");
            return false;
          }
        },
        mResourceProxy);
    mOverlay.setFocusItemsOnTap(true);

    mMapView.getOverlays().add(mOverlay);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      mOnOverlayItemClickedListener = (OnOverlayItemClickedListener)context;
    } catch (ClassCastException ex) {
      throw new ClassCastException(context.toString() +
                                   " must implement OnOverlayItemClicked");
    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mOnOverlayItemClickedListener = (OnOverlayItemClickedListener)activity;
    } catch (ClassCastException ex) {
      throw new ClassCastException(activity.toString() +
                                   " must implement OnOverlayItemClicked");
    }
  }

  @Override
  public void onDetach() {
    mMapView.onDetach();
    mMapView = null;
    mOnOverlayItemClickedListener = null;
    mResourceProxy = null;
    super.onDetach();
  }

  private void sendLocationData(Location location) {
    mOnOverlayItemClickedListener.update(location);
  }

  public interface OnOverlayItemClickedListener {
    void update(Location location);
  }
}
