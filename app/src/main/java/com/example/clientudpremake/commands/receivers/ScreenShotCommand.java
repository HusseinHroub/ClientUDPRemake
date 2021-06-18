package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ToastUtility;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Base64;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScreenShotCommand implements Command {

    private final String binaryImage;

    @Override
    public void apply(Activity activity) {
        byte[] byteArray = getImageInBytes(activity);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        PhotoView photoView = activity.findViewById(R.id.photo_view);
        photoView.setImageBitmap(bitmap);
        AnimationUtils.fadeIn(activity.findViewById(R.id.image_container));
        LogUtility.log("Received image and viewed it..");
    }

    private byte[] getImageInBytes(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getDecoder().decode(binaryImage);
        } else {
            ToastUtility.showMessage("Can't show image because your andoird is old brother", activity);
            return new byte[0];
        }
    }
}
