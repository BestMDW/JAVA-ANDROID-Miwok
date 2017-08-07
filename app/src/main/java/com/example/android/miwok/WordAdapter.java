package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link WordAdapter} is and {@link ArrayAdapter} that can provide layout for each list
 * based on a data source, which is a list of {@link Word} objects.
 */
public class WordAdapter extends ArrayAdapter<Word> {
    //Color resource ID for words items background
    private int mColorResourceID = NO_COLOR_PROVIDED;

    private static final int NO_COLOR_PROVIDED = -1;

    /**
     * This is or own custom constructor.
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the list.
     *
     * @param context   The current context. Used to inflate the layout file.
     * @param words     A List of Word objects to display in a list
     */
    public WordAdapter(Activity context, ArrayList<Word> words)
    {
        super(context, 0, words);
    }

    /**
     * This is or own custom constructor.
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the list.
     *
     * @param context   The current context. Used to inflate the layout file.
     * @param words     A List of Word objects to display in a list
     * @param colorResourceID The color resource ID for words items background.
     */
    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceID)
    {
        super(context, 0, words);
        mColorResourceID = colorResourceID;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position  The position in the list of data that should be displayed in the
     *                  list item view.
     * @param convertView The recycled view to populated.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        //Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        //Find the TextView in the list_item.xml layout with the ID miwok_text_view
        TextView miwokTextView = (TextView)listItemView.findViewById(R.id.miwok_text_view);
        //Get the Miwok translation from the current Word object and
        //set this text on the TextView
        miwokTextView.setText(currentWord.getMiwokTranslation());

        //Find the TextView in the list_item.xml layout with the ID default_text_view
        TextView defaultTextView = (TextView)listItemView.findViewById(R.id.default_text_view);
        //Get the default translation from the current Word object and
        //set this text on the TextView
        defaultTextView.setText(currentWord.getDefaultTranslation());

        //Find the ImageView in the list_item.xml layout with the ID image
        ImageView image = (ImageView)listItemView.findViewById(R.id.image_view);

        if (currentWord.hasImage())
        {
            //Get the Image resource ID from the current Word object and
            //set this image on the ImageView
            image.setImageResource(currentWord.getImageResourceID());

            //Make sure the view is visible
            image.setVisibility(View.VISIBLE);
        }
        else
        {
            //Otherwise hide the ImageView (set visibility to GONE)
            image.setVisibility(View.GONE);
        }

        if (hasBackgroundColor())
        {
            //Set the theme color for the list item
            View textContainer = listItemView.findViewById(R.id.text_container);
            //Set the background color of the text container View
            textContainer.setBackgroundResource(mColorResourceID);
        }

        //Return the whole list item layout (containing 2 TextViews and 1 ImageView)
        //so that it can be shown in the ListView
        return listItemView;
    }

    /**
     * Returns true if background color is provided, false otherwise.
     */
    public boolean hasBackgroundColor()
    {
        return mColorResourceID != NO_COLOR_PROVIDED;
    }
}
