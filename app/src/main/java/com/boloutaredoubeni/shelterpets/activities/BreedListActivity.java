package com.boloutaredoubeni.shelterpets.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.boloutaredoubeni.shelterpets.R;
import com.boloutaredoubeni.shelterpets.ShelterPetsApplication;
import com.boloutaredoubeni.shelterpets.api.BreedDataResponse;
import com.boloutaredoubeni.shelterpets.api.PetFinderService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class BreedListActivity extends AppCompatActivity {
  private static String TAG = BreedListActivity.class.getCanonicalName();
  public static String CATEGORY = "C413g0Ry";

  private ListView mListView;
  private ArrayAdapter<String> mArrayAdapter;
  private ArrayList<String> mBreedList;
  private String mCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_breed_list);

    mListView = (ListView)findViewById(R.id.breed_list);
    mBreedList = new ArrayList<>();
    mArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_list_item_1, mBreedList);
    mListView.setAdapter(mArrayAdapter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    String category = (String)getIntent().getExtras().get(CATEGORY);
    if (category != null) {
      if (!category.equals(
          ShelterPetsApplication
              .getAnimals()[ShelterPetsApplication.getAnimals().length -
              1]))
        new BreedListTask().execute(category);
    }
  }

  private class BreedListTask
      extends AsyncTask<String, Void, ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(String... params) {
      String category = params[0];
      ArrayList<String> breeds = new ArrayList<>();
      try {
        String response = PetFinderService.getInstance().getBreeds(category);
        Gson gson = new Gson();
        BreedDataResponse dataResponse = gson.fromJson(response, BreedDataResponse.class);
        breeds = dataResponse.getBreeds();
      } catch (IOException e) {
        Log.e(TAG, e.getMessage());
      }
      return breeds;
    }

    @Override
    protected void onPostExecute(ArrayList<String> breeds) {
      if (mArrayAdapter.getCount() == 0) {
        mArrayAdapter.addAll(breeds);
      }
      mArrayAdapter.notifyDataSetChanged();
    }
  }
}
