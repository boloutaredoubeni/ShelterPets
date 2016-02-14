package com.boloutaredoubeni.shelterpets.api;

import java.util.ArrayList;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class BreedDataResponse {

  public PetFinder petfinder;


  public ArrayList<String> getBreeds() {
    ArrayList<String> breeds = new ArrayList<>();
    for (final Breed breed: petfinder.breeds.breed) {
      breeds.add(breed.$t);
    }
    return breeds;
  }
}
