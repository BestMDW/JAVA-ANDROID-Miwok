package com.example.android.miwok;

import static android.R.string.no;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 */
public class Word {
    //Default translation for the word
    private String mDefaultTranslation;

    //Miwok translation for the word
    private String mMiwokTranslation;

    //Image resource ID for the word
    private int mImageResourceID = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    //Audio resource ID for the word
    private int mAudioResourceID = NO_AUDIO_PROVIDED;
    private static final int NO_AUDIO_PROVIDED = -1;

    /**
     * Create a new Word object.
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     */
    public Word(String defaultTranslation, String miwokTranslation)
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
    }

    /**
     * Create a new Word object with image.
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * @param audioResourceID is the raw resource ID for the audio associate with word
     */
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceID)
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceID = audioResourceID;
    }

    /**
     * Create a new Word object with image.
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * @param imageResourceID is the drawable resource ID for the image associate with word
     * @param audioResourceID is the raw resource ID for the audio associate with word
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceID, int audioResourceID)
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceID = imageResourceID;
        mAudioResourceID = audioResourceID;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation()
    {
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation()
    {
        return mMiwokTranslation;
    }

    /**
     * Return the image resource ID of the word.
     */
    public int getImageResourceID() { return mImageResourceID; }

    /**
     * Returns whether or not there is an image of the word.
     */
    public boolean hasImage()
    {
        return mImageResourceID != NO_IMAGE_PROVIDED;
    }

    /**
     * Returns the audio resource ID of the word.
     */
    public int getAudioResourceID()
    {
        return mAudioResourceID;
    }

    /**
     * Return whether or not there is an audio for the word.
     */
    public boolean hasAudio()
    {
        return mAudioResourceID != NO_AUDIO_PROVIDED;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceID=" + mImageResourceID +
                ", mAudioResourceID=" + mAudioResourceID +
                '}';
    }
}
