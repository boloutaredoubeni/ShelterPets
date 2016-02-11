package com.boloutaredoubeni.neighborly.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boloutaredoubeni.neighborly.R;
import com.boloutaredoubeni.neighborly.models.Location;

/**
 * Copyright 2016 Boloutare Doubeni
 *
 * Holds detailed information about the clicked location
 */
public class DetailFragment extends Fragment {
  public static final String TAG = DetailFragment.class.getCanonicalName();
  private Location mLocation;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_detail, container, false);
    TextView nameTextView = (TextView)view.findViewById(R.id.location_name_txt);
    nameTextView.setText(mLocation.getName());

    TextView amenityTextView =
        (TextView)view.findViewById(R.id.location_amenity_txt);
    amenityTextView.setText(
        mLocation.getAmenity() != null ? mLocation.getAmenity() : "");

    TextView hoursTextView =
        (TextView)view.findViewById(R.id.location_hours_txt);
    hoursTextView.setText(mLocation.getHours() != null ? mLocation.getHours()
                                                       : "");

    TextView urlTextView = (TextView)view.findViewById(R.id.location_url_txt);
    urlTextView.setText(
        mLocation.getURL() != null ? mLocation.getURL().toString() : "");
    return view;
  }

  public void updateLocation(Location location) { mLocation = location; }
}
