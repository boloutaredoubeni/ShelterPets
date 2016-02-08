package com.boloutaredoubeni.neighborly.fragments;

import android.view.View;

/**
 * Copyright 2016 Boloutare Doubeni
 */
class DualPaned {
  static boolean isDualPaned(View view) {
    return (view != null) && (view.getVisibility() == View.VISIBLE);
  }
}
