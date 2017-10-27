package com.example.android.testing.notiyActivity;

/**
 * Created by Android on 5/18/2017.
 */

public class ModelNoty {

    private String title;
    private String message;

    public ModelNoty() {
    }

    public ModelNoty(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
