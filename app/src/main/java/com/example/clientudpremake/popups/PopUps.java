package com.example.clientudpremake.popups;

import android.view.View;

public abstract class PopUps {
    protected final View view;

    public PopUps(View view) {
        this.view = view;
    }

    public boolean isVisible() {
        return view.getVisibility() == View.VISIBLE;
    }

    abstract public void removeVisibility();

}
