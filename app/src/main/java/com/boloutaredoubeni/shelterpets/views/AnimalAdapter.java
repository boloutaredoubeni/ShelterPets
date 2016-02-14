package com.boloutaredoubeni.shelterpets.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boloutaredoubeni.shelterpets.R;
import com.boloutaredoubeni.shelterpets.ShelterPetsApplication;
import com.boloutaredoubeni.shelterpets.activities.MainActivity;

public class AnimalAdapter extends ArrayAdapter<String> {
  private Context mContext;

  public AnimalAdapter(Context context) {
    super(context, R.layout.grid_item, ShelterPetsApplication.getAnimals());

    mContext = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    String colorName = getItem(position);
    View view = ((MainActivity)mContext)
                    .getLayoutInflater()
                    .inflate(R.layout.grid_item, parent, false);
    SquareImageView imgView =
        (SquareImageView)view.findViewById(R.id.card_image);
    TextView txtView = (TextView)view.findViewById(R.id.item_txt);
    int color = getColor(colorName);
    imgView.setBackgroundColor(color);
    txtView.setText(colorName);
    return view;
  }

  private int getColor(String color) {
    switch (color.toUpperCase()) {
    case "DOGS":
      return Color.BLUE;
    case "CATS":
      return Color.RED;
    case "HORSES":
      return Color.BLACK;
    case "BARNYARD":
      return Color.YELLOW;
    case "REPTILES":
      return Color.GREEN;
    case "BIRDS":
      return Color.GRAY;
    case "SMALL AND FURRY":
      return Color.DKGRAY;
    case "PIGS":
      return Color.LTGRAY;
    default:
      return Color.TRANSPARENT;
    }
  }
}