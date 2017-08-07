package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {
    //Instance variable for audio player
    private MediaPlayer mAudioPlayer = null;

    //This listener gets triggered when the {@link MediaPlayer} has completed playing the audio file.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            releaseMediaPlayer();
        }
    };

    //Instance variable for audio manager
    private AudioManager mAudioManager;

    //Assign global variable for audio manager focus change listener
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange)
            {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mAudioPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mAudioPlayer.pause();
                    mAudioPlayer.seekTo(0);
                    break;
            }
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //Create and setup the {@link AudioManager} to request audio focus.
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Create an array of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        //Create an {@link WordAdapter}, whose data source is a list of {@link Word}s objects.
        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        //Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        //There should be a {@link ListView} with the view ID called list, which is declared in the
        //word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        //Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        //{@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(itemsAdapter);

        //Create onItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Current word
                Word word = words.get(position);

                //Release media player resources.
                releaseMediaPlayer();

                //If word has assigned audio file, load and play the file.
                if (word.hasAudio()) {

                    //Request audio focus for playback
                    int audioFocusResult = mAudioManager.requestAudioFocus(
                            mOnAudioFocusChangeListener,
                            //Use the music stream.
                            AudioManager.STREAM_MUSIC,
                            //Request permanent focus.
                            AudioManager.AUDIOFOCUS_GAIN
                    );

                    //When audio focus is on our app, start playing
                    if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                    {
                        //Assign audio file from the resource variable.
                        mAudioPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceID());

                        //Start playing word
                        mAudioPlayer.start();

                        //Release audio player after the sound file has finished playing
                        mAudioPlayer.setOnCompletionListener(mCompletionListener);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer()
    {
        //If audio player is not null, then it may be currently playing a sound.
        if (mAudioPlayer != null)
        {
            //Release audio player resources.
            mAudioPlayer.release();

            //Set variable back to null.
            mAudioPlayer = null;

            //Abandon audio focus when playback complete.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
