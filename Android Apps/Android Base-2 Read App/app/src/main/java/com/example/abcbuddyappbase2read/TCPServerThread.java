package com.example.abcbuddyappbase2read;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerThread extends Thread {
    public interface OnMessageReceived {
        void onMessage(String message);
    }

    private ServerSocket serverSocket;
    private boolean running = true;
    private static final int SERVER_PORT = 12345;
    private OnMessageReceived listener;

    public TCPServerThread(OnMessageReceived listener) {
        this.listener = listener;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            Log.d("TCP", "Server started on port " + SERVER_PORT);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                Log.d("TCP", "Client connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    Log.d("TCP", "Received: " + message);
                    if (listener != null) {
                        listener.onMessage(message); // send to MainActivity
                    }
                }

                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

