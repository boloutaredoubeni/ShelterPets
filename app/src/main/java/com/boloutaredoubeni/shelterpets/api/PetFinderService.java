package com.boloutaredoubeni.shelterpets.api;

import com.boloutaredoubeni.shelterpets.ShelterPetsApplication;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class PetFinderService {

  private static PetFinderService INSTANCE = null;
  private static OkHttpClient mClient;

  private PetFinderService() {}

  public static PetFinderService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PetFinderService();
      mClient = new OkHttpClient();
    }
    return INSTANCE;
  }

  public String getBreeds(String animalName) throws IOException {
    //    String url = ShelterPetsApplication.API_URL +
    //                 "/breed.list?format=json&key=" +
    //                 ShelterPetsApplication.KEY +
    //                 "&animal=" + animalName;
    HttpUrl url =
        new HttpUrl.Builder()
            .scheme("http")
            .host(ShelterPetsApplication.API_URL)
            .addEncodedPathSegment("breed.list")
            .addEncodedQueryParameter("format", "json")
            .addEncodedQueryParameter("animal", animalName.toLowerCase())
            .addEncodedQueryParameter("key", ShelterPetsApplication.KEY)
            .build();
    Request request = new Request.Builder().url(url).build();
    Response response = mClient.newCall(request).execute();
    return response.body().string();
  }
}
