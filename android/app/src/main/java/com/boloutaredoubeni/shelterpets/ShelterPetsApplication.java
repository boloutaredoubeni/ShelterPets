package com.boloutaredoubeni.shelterpets;

import android.app.Application;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class ShelterPetsApplication extends Application {

  public static final String KEY = "3fa520161187760bdff6bf023f368656";
  public static final String API_URL = "api.petfinder.com";
  private static final String[] animals =
      new String[] {"Dog",   "Cat",        "Horse", "Barnyard", "Reptile",
                    "Birds", "SmallFurry", "Pig",   "Favorites"};

  public static String[] getAnimals() { return animals; }
}
