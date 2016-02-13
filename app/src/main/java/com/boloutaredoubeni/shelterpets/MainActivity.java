package com.boloutaredoubeni.shelterpets;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private GridView mGridView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mGridView = (GridView) findViewById(R.id.grid_view);

    ArrayList<String> colors = new ArrayList<String>() {
      {
        add("Blue");
        add("Green");
        add("Black");
        add("Red");
        add("White");
        add("Yellow");
        add("Gray");
      }
    };

    ArrayAdapter<String> adapter = new ImageAdapter(this, colors);
    mGridView.setAdapter(adapter);
  }



  class ImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    @LayoutRes private final int mLayout;

    public ImageAdapter(Context context, List<String> objects) {
      super(context, R.layout.grid_item, objects);
      mContext =context;
      mLayout = R.layout.grid_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      String colorName = getItem(position);
      View view  = getLayoutInflater().inflate(mLayout, parent, false);
      SquareImageView imgView = (SquareImageView)view.findViewById(R.id.card_image);
      TextView txtView = (TextView)view.findViewById(R.id.item_txt);
      int color = getColor(colorName);
      imgView.setBackgroundColor(color);
      txtView.setText(colorName);
      return view;
    }

    private int getColor(String color) {
      switch (color.toUpperCase()) {
        case "BLUE":
          return Color.BLUE;
        case "RED":
          return  Color.RED;
        case "BLACK":
          return Color.BLACK;
        case "YELLOW":
          return Color.YELLOW;
        case "GREEN":
          return Color.GREEN;
        case "GRAY":
        case "GREY":
          return Color.GRAY;
        default:
          return Color.DKGRAY;
      }
    }
  }
}
