package com.project.matam.tetris.gamecore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.project.matam.tetris.R;

import java.util.ArrayList;

/**
 * Created by matam on 19/05/2017.
 * GridAdapter recupéré sur internet et adapté à mes besoins
 */



/**
 * Custom Base Adapter use to create custom gridview
 */
class GameGridAdapter extends BaseAdapter{

    public void setItems(ArrayList mItems) {
        this.mItems = mItems;
    }

    private ArrayList mItems = new ArrayList();
    private Context mContext;

    public GameGridAdapter(Context context, ArrayList items) {
        this.mItems = items;
        this.mContext = context;
    }

    /**
     * returns the total number of items to be displayed
     * @return size of array
     */
    @Override
    public int getCount() {
        return this.mItems.size();
    }

    /**
     * Get the data item associated with the specified position in the data set
     * @param position
     * @return the corresponding data of the specific location in the collection of data items
     */
    @Override
    public Object getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mItems.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gameView = convertView;
        ImageView iv = null;

        if(gameView == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            //set layout for displaying items
            gameView = inflater.inflate(R.layout.items, parent,false);
            //get id for image view
            iv = (ImageView) gameView.findViewById(R.id.item);
            //Create memories for views
            gameView.setTag(iv);
        }
        else
        {
            iv = (ImageView)gameView.getTag();
        }

        iv.setImageBitmap((Bitmap)this.mItems.get(position));


        return gameView;
    }
}
