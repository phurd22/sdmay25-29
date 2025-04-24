package com.example.abcbuddyappbase2read;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.p2p.*;
import android.net.wifi.p2p.WifiP2pManager.*;
import android.util.Log;

import java.io.*;
import java.net.*;

public class WiFiDirectReceiver {

    private static final String TAG = "WiFiDirectReceiver";
    private final WifiP2pManager manager;
    private final Channel channel;
    private final Context context;

    public WiFiDirectReceiver(Context context, WifiP2pManager manager, Channel channel) {
        this.context = context;
        this.manager = manager;
        this.channel = channel;
    }

    @SuppressLint("MissingPermission")
    public void discoverAndConnect() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Group removed before discovery");
                startDiscovery();
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Group remove failed: " + reason);
                startDiscovery(); // Still try
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void startDiscovery() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Discovery started");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Discovery failed: " + reason);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public PeerListListener getPeerListListener() {
        return peers -> {
            for (WifiP2pDevice device : peers.getDeviceList()) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                manager.connect(channel, config, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Connected to " + device.deviceName);
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG, "Connection failed: " + reason);
                    }
                });
                break;
            }
        };
    }

    public ConnectionInfoListener getConnectionListener() {
        return info -> {
            if (info.groupFormed) {
                Log.d(TAG, "Group formed. Am I Group Owner? " + info.isGroupOwner);
            } else {
                Log.d(TAG, "Group not formed yet.");
            }
            if (info.groupFormed && info.isGroupOwner) {
                new ServerThread().start();
            }
        };
    }

    private static class ServerThread extends Thread {
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(5050)) {
                Log.d(TAG, "Server started, waiting for client...");
                Socket client = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d(TAG, "Received: " + line);
                }
                client.close();
            } catch (Exception e) {
                Log.e(TAG, "Receive error", e);
            }
        }
    }
}

