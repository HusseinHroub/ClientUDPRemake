package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;
import com.github.chrisbanes.photoview.PhotoView;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScreenShotCommand implements Command {

    private final byte[] binaryImage;

    @Override
    public void apply(Activity activity) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(binaryImage, 0, binaryImage.length);
        PhotoView photoView = activity.findViewById(R.id.photo_view);
        photoView.setImageBitmap(bitmap);
        AnimationUtils.fadeIn(activity.findViewById(R.id.image_container));
        LogUtility.log("Received image and viewed it..");
    }
}
