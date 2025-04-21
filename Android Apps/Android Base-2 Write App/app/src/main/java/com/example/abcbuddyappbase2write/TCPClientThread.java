package com.example.abcbuddyappbase2write;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClientThread extends Thread {
    private static final String TAG = "TCPClient";
    private String serverIp;
    private static final int SERVER_PORT = 12345;
    private String message;

    public TCPClientThread(String serverIp, String messageToSend) {
        this.serverIp = serverIp;
        this.message = messageToSend;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIp, SERVER_PORT), 5000);

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(message);
            out.flush();

            Log.d(TAG, "Message sent: " + message);

        } catch (IOException e) {
            Log.e(TAG, "Error in client: " + e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    Log.d(TAG, "Socket closed");
                } catch (IOException e) {
                    Log.e(TAG, "Error closing socket: " + e.getMessage(), e);
                }
            }
        }
    }
}