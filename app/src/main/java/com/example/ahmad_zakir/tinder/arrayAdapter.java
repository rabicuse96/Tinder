package com.example.ahmad_zakir.tinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ahmad_zakir on 2/23/2018.
 */

public class arrayAdapter extends ArrayAdapter<cards>{
    Context context;

    public arrayAdapter(Context context, int resourceid, List<cards> items){
        super(context, resourceid, items);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    public View getView(int position , View convertView, ViewGroup parent){
        // Get the {@link AndroidFlavor} object located at this position in the list
        cards cards_item = getItem(position);
        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.name);
        ImageView imageView =(ImageView) convertView.findViewById(R.id.image);

        name.setText(cards_item.getName());
        imageView.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }
}
