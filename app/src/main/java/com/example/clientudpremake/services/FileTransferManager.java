package com.example.clientudpremake.services;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startSendingProcess(FileInformation fileInformation) {
        final TextView textView = activity.findViewById(R.id.sending_file_text_view);
        handler.post(() -> textView.setText("Sending File: " + fileInformation.fileName));
        WebSocketManager.INSTANCE.getWebSocket().addListener(this);
        try (InputStream inputStream = activity.getContentResolver().openInputStream(uri)) {
            long startTime = System.currentTimeMillis();
            handler.post(() -> progressBarDialog.setVisibility(View.VISIBLE));
            progressBar.setProgress(0);
            long fileSize = fileInformation.fileSize;
            int bytesPerSplit = BYTES_PER_SPLIT;
            long numberOfSplits = fileSize / bytesPerSplit;
            int remaining = (int) (fileSize % bytesPerSplit);
            long latestFrame = remaining == 0 ? numberOfSplits - 1 : numberOfSplits;
            for (long i = 0; i < numberOfSplits; i++) {
                String json = new Gson().toJson(new FileTransferModel(fileInformation.fileName, i, latestFrame, id));
                byte[] buf = new byte[Integer.BYTES + json.length() + bytesPerSplit];
                if (inputStream.read(buf, Integer.BYTES + json.length(), bytesPerSplit) != -1) {
                    byte[] jsonArray = json.getBytes(StandardCharsets.UTF_8);
                    byte[] jsonArrayLength = getByteFromInteger(jsonArray.length);
                    System.arraycopy(jsonArrayLength, 0, buf, 0, jsonArrayLength.length);
                    System.arraycopy(jsonArray, 0, buf, jsonArrayLength.length, jsonArray.length);
                    WebSocketManager.INSTANCE.sendBinary(buf);
                } else {
                    LogUtility.log(("Finished all splits with no remaining"));
                    break;
                }
            }
            LogUtility.log("Finished splitting: " + numberOfSplits);
            if (remaining > 0) {
                String json = new Gson().toJson(new FileTransferModel(fileInformation.fileName, latestFrame, latestFrame, id));
                byte[] buf = new byte[Integer.BYTES + json.length() + remaining];
                if (inputStream.read(buf, Integer.BYTES + json.length(), remaining) != -1) {
                    byte[] jsonArray = json.getBytes(StandardCharsets.UTF_8);
                    byte[] jsonArrayLength = getByteFromInteger(jsonArray.length);
                    System.arraycopy(jsonArrayLength, 0, buf, 0, jsonArrayLength.length);
                    System.arraycopy(jsonArray, 0, buf, jsonArrayLength.length, jsonArray.length);
                    WebSocketManager.INSTANCE.sendBinary(buf);
                    LogUtility.log(("finished all splits with remaining: " + remaining));
                } else {
                    LogUtility.log("Finished all splits with no remaining");
                }
            }
            LogUtility.log("Ended in: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
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
        String jsonString = getJsonString(frame);
        FileTransferModel fileTransferModel = new Gson().fromJson(jsonString, FileTransferModel.class);
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

    private String getJsonString(WebSocketFrame frame) {
        byte[] payload = frame.getPayload();
        int sizeToRead = getSizeToRead(payload);
        return new String(Arrays.copyOfRange(payload, Integer.BYTES, sizeToRead + Integer.BYTES));
    }

    private int getSizeToRead(byte[] payload) {
        byte[] integerArray = new byte[Integer.BYTES];
        System.arraycopy(payload, 0, integerArray, 0, integerArray.length);
        return bytesToInteger(integerArray);
    }

    @RequiredArgsConstructor
    private static class FileInformation {
        private final String fileName;
        private final long fileSize;
    }

    private byte[] getByteFromInteger(int integer) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(integer);
        return buffer.array();
    }

    public static int bytesToInteger(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }
}
