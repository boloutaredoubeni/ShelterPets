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
  private Location mLocation;
  private TextView mTextView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_detail, container, false);
    mTextView = (TextView)view.findViewById(R.id.location_info_txt);
    mTextView.setText(mLocation.toString());
    return view;
  }

  public void updateLocation(Location location) { mLocation = location; }
}
