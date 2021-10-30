package com.example.vocabulary.Bean;

public class Word {

    public static String TABLE_NAME = "Vocabulary";
    public static String COLUMN_NAME_WORD = "WordName";
    public static String COLUMN_NAME_MEANING = "WordMeaning";
    public static String COLUMN_NAME_SAMPLE = "WordSample";

    private String name;
    private String meaning;
    private String sample;

    public Word(){}

    public Word(String name, String meaning, String sample) {
        this.name = name;
        this.meaning = meaning;
        this.sample = sample;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }
}
