package com.boloutaredoubeni.neighborly;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright 2016 Boloutare Doubeni
 *
 *
 */
public class DashboardFragment extends Fragment {

  public DashboardFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
    CardView cardView = (CardView)view.findViewById(R.id.main_card);
    return view;
  }
}
