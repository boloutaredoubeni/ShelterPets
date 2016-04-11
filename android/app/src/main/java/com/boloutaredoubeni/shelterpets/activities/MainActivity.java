package com.boloutaredoubeni.shelterpets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.boloutaredoubeni.shelterpets.R;
import com.boloutaredoubeni.shelterpets.ShelterPetsApplication;
import com.boloutaredoubeni.shelterpets.views.AnimalAdapter;

public class MainActivity extends AppCompatActivity {

  private GridView mGridView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mGridView = (GridView)findViewById(R.id.grid_view);

    ArrayAdapter<String> adapter = new AnimalAdapter(this);
    mGridView.setAdapter(adapter);

    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
                              long id) {
        String category = ShelterPetsApplication.getAnimals()[position];
        Intent intent = new Intent(MainActivity.this, BreedListActivity.class);
        intent.putExtra(BreedListActivity.CATEGORY, category);
        startActivity(intent);
      }
    });
  }
}
