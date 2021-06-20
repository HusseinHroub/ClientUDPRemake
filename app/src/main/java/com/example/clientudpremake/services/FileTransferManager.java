package com.example.clientudpremake.services;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.clientudpremake.R;
import com.example.clientudpremake.models.FileTransferModel;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.utilites.ToastUtility;
import com.example.clientudpremake.workers.websocket.WebSocketManager;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.apache.commons.codec.binary.Base64;

import java.io.InputStream;

import lombok.RequiredArgsConstructor;

public class FileTransferManager extends WebSocketAdapter {
    private static final int BYTES_PER_SPLIT = 8 * 1024;
    private final Activity activity;
    private final Uri uri;
    private Handler handler;
    private long id;
    private ProgressBar progressBar;
    private View progressBarDialog;

    public FileTransferManager(Activity activity, Uri uri) {
        this.activity = activity;
        this.uri = uri;
        this.handler = new Handler(activity.getMainLooper());
        id = System.nanoTime();
        progressBar = activity.findViewById(R.id.progress_bar_hor);
        progressBarDialog = activity.findViewById(R.id.progress_dialog);
    }

    public void sendFileToServer() {
        try {
            FileInformation fileInformation = getFileName();
            ThreadsUtilty.getExecutorService().execute(() -> startSendingProcess(fileInformation));
        } catch (Exception e) {
            LogUtility.log("Got an exception when trying to get file information: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public FileInformation getFileName() {
//        if (name == null) {
//            name = getNameFromLastSlash();
//        }
        return getFileInformationIfFromContent();
    }

    private FileInformation getFileInformationIfFromContent() {
        String name = null;
        long size = 0;
        LogUtility.log("URI is of type: " + uri.getScheme());
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                    LogUtility.log("Fetched file information");
                }
            }
        }
        return new FileInformation(name, size);
    }

    private String getNameFromLastSlash() {
        String name;
        name = uri.getPath();
        int cut = name.lastIndexOf('/');
        if (cut != -1) {
            name = name.substring(cut + 1);
        }
        return name;
    }

    private void startSendingProcess(FileInformation fileInformation) {
        final TextView textView = activity.findViewById(R.id.sending_file_text_view);
        handler.post(() -> textView.setText("Sending File: " + fileInformation.fileName));
        WebSocketManager.INSTANCE.getWebSocket().addListener(this);
        try (InputStream inputStream = activity.getContentResolver().openInputStream(uri)) {
            handler.post(() -> progressBarDialog.setVisibility(View.VISIBLE));
            progressBar.setProgress(0);
            long fileSize = fileInformation.fileSize;
            int bytesPerSplit = BYTES_PER_SPLIT;
            long numberOfSplits = fileSize / bytesPerSplit;
            int remaining = (int) (fileSize % bytesPerSplit);
            long latestFrame = remaining == 0 ? numberOfSplits - 1 : numberOfSplits;
            for (long i = 0; i < numberOfSplits; i++) {
                byte[] buf = new byte[bytesPerSplit];
                if (inputStream.read(buf) != -1) {
                    String json = new Gson().toJson(new FileTransferModel(Base64.encodeBase64String(buf), fileInformation.fileName, i, latestFrame, id));
                    WebSocketManager.INSTANCE.sendText(json);
                } else {
                    LogUtility.log(("Finished all splits with no remaining"));
                    break;
                }
            }
            if (remaining > 0) {
                byte[] buf = new byte[remaining];
                if (inputStream.read(buf) != -1) {
                    String json = new Gson().toJson(new FileTransferModel(Base64.encodeBase64String(buf), fileInformation.fileName, latestFrame, latestFrame, id));
                    WebSocketManager.INSTANCE.sendText(json);
                    LogUtility.log(("finished all splits with remaining: " + remaining));
                } else {
                    LogUtility.log("Finished all splits with no remaining");
                }
            }
        } catch (Exception e) {
            LogUtility.log("An exception occurred while trying to send file: " + fileInformation.fileName + " to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateProgressBar(final long splitId, final long totalSplits) {
        handler.post(() -> progressBar.setProgress((int) ((splitId / (double) totalSplits) * 100)));
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
        FileTransferModel fileTransferModel = new Gson().fromJson(frame.getPayloadText(), FileTransferModel.class);
        updateProgressBar(fileTransferModel.getFrame(), fileTransferModel.getLatestFrame());
        if (fileTransferModel.getFrame() == fileTransferModel.getLatestFrame()) {
            WebSocketManager.INSTANCE.getWebSocket().removeListener(this);
            handler.post(() -> {
                progressBarDialog.setVisibility(View.GONE);
                ToastUtility.showMessage("File sent successfully", activity);
            });
            LogUtility.log("Removing listener as received last frame!");
        }
    }

    @RequiredArgsConstructor
    private static class FileInformation {
        private final String fileName;
        private final long fileSize;
    }
}
