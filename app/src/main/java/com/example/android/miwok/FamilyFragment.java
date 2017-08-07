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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {
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

    public FamilyFragment() {
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
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        //Create an adapter of word list
        WordAdapter itemList = new WordAdapter(getActivity(), words, R.color.category_family);

        //Find list view
        ListView listView = (ListView)rootView.findViewById(R.id.list);

        //Implement word adapter with list view
        listView.setAdapter(itemList);

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
