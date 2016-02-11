package com.boloutaredoubeni.neighborly.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boloutaredoubeni.neighborly.R;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class PhotoViewFragment
    extends Fragment implements View.OnTouchListener {

  // TODO: get photo data based on location
  private ImageView mImageView;

  public PhotoViewFragment() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_image, container, false);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    return false;
  }
}
