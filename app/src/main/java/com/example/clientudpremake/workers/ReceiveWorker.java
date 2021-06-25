package com.example.clientudpremake.workers;

import android.os.Handler;
import android.os.Looper;

import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.utilites.LogUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveWorker implements Runnable {

    private static final int PORT = 9622;
    private DatagramSocket datagramSocket;
    private Handler handler;
    private final Receiver receiveCommand;

    public ReceiveWorker(Receiver receiveCommand) throws SocketException {
        handler = new Handler(Looper.getMainLooper());
        this.receiveCommand = receiveCommand;
        initSocket();
    }


    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                datagramSocket.receive(datagramPacket);
                String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                handler.post(() -> receiveCommand.receive(new UDPMessage(message, datagramPacket.getAddress())));
                LogUtility.log("Received message from UDP with content: " + message);
            } catch (IOException e) {
                LogUtility.log("An error occurred while trying to receive from UDP connection");
                e.printStackTrace();
            }

        }
    }

    private void initSocket() throws SocketException {
        if (datagramSocket == null) {
            LogUtility.log("UDP Socket initialized");
            datagramSocket = new DatagramSocket(PORT);
        }
    }


}
