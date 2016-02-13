package com.boloutaredoubeni.shelterpets;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OldMainActivity extends AppCompatActivity {

  private ListView mListView;

  private CursorAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_old);
    Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action",
                      Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });

    /* TODO: run this only once
    List<String> pets = new ArrayList<String>() {
      {
        add("Dog");
        add("Snake");
        add("Ferret");
        add("Cat");
      }
    };

    PetDatabase.getInstance(this).insert(pets);
    */

    final Cursor cursor = PetDatabase.getInstance(this).getPets();
    mAdapter = new CursorAdapter(OldMainActivity.this, cursor, 0) {

      @Override
      public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false);
      }

      @Override
      public void bindView(View view, Context context, Cursor cursor) {
        ((TextView)view.findViewById(android.R.id.text1))
            .setText(cursor.getString(
                cursor.getColumnIndex(PetDatabase.COL_SPECIES)));
      }
    };

    mListView = (ListView)findViewById(R.id.pets_list);
    mListView.setAdapter(mAdapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
                              long id) {
        Intent intent = new Intent(OldMainActivity.this, PetInfoActivity.class);
        cursor.moveToPosition(position);
        intent.putExtra("id", cursor.getInt(cursor.getColumnIndexOrThrow(PetDatabase.COL_ID)));
        startActivity(intent);
      }
    });

    handleIntent(getIntent());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    SearchManager searchManager =
        (SearchManager)getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView =
        (SearchView)menu.findItem(R.id.action_search).getActionView();
    SearchableInfo searchableInfo =
        searchManager.getSearchableInfo(getComponentName());
    searchView.setSearchableInfo(searchableInfo);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      Cursor cursor = PetDatabase.getInstance(this).searchPets(query);
      mAdapter.swapCursor(cursor);
    }
  }
}
