package com.example.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterGame extends BaseAdapter {
    Context context;
    ArrayList<Game> games;
    LayoutInflater inflater;

    public AdapterGame(Context context, ArrayList<Game> games) {
        this.context = context;
        this.games = games;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    Game getGame(int pos){
        return (Game) getItem(pos);
    }

    public Bitmap getImageBitmap(String image){
        if(image != null){
            byte[] bytes = Base64.decode(image,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }else {
            return null;
        }
    }


    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int i) {
        return games.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.item_layout,viewGroup,false);
        }
        Game game = getGame(i);

        ((TextView) view.findViewById(R.id.txt_title)).setText(game.title);
        ((TextView) view.findViewById(R.id.txt_cost)).setText(game.cost+"");
        ((TextView) view.findViewById(R.id.txt_stock)).setText(game.stockAvailability+"");
        ((TextView) view.findViewById(R.id.txt_store)).setText(game.availabilityInTheStore+"");
        ((TextView) view.findViewById(R.id.txt_description)).setText(game.description);
        ((TextView) view.findViewById(R.id.txt_rewiews)).setText(game.rewiews);
        if (game.image != null){
            ((ImageView) view.findViewById(R.id.imageGame)).setImageBitmap(getImageBitmap(game.image));
        }else {
            ((ImageView) view.findViewById(R.id.imageGame)).setImageResource(R.drawable.ic_baseline_person_add_24);
        }
        return view;
    }
}
