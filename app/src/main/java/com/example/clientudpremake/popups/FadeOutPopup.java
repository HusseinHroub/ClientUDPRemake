package com.example.clientudpremake.popups;

import android.view.View;

import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;

public class FadeOutPopup extends PopUps {

    public FadeOutPopup(View view) {
        super(view);
    }

    @Override
    public void removeVisibility() {
        LogUtility.log("A popup is visible, fading it out!");
        AnimationUtils.fadeOut(view);
    }
}
