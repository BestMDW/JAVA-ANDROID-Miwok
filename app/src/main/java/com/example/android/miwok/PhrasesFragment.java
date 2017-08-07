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
public class PhrasesFragment extends Fragment {
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

    public PhrasesFragment() {
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
        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        //Create a word adapter
        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        //Find list view
        ListView listView = (ListView)rootView.findViewById(R.id.list);

        //Implement adapter with list view
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
