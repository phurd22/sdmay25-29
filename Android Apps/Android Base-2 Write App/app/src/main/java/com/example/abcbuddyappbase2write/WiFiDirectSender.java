package com.example.abcbuddyappbase2write;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.*;
import android.net.wifi.p2p.WifiP2pManager.*;
import android.util.Log;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

public class WiFiDirectSender {

    private static final String TAG = "WiFiDirectSender";
    private final WifiP2pManager manager;
    private final Channel channel;
    private final Context context;
    private WifiP2pInfo wifiP2pInfo;
    private boolean hasAttemptedConnection = false;

    public WiFiDirectSender(Context context, WifiP2pManager manager, Channel channel) {
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
            if (peers.getDeviceList().isEmpty()) {
                Log.d(TAG, "No devices found");
                return;
            }

            // Only try to connect ONCE to the right type of device (filter by name or MAC optionally)
            for (WifiP2pDevice device : peers.getDeviceList()) {
                Log.d(TAG, "Found device: " + device.deviceName);
                Log.d(TAG, "Device MAC: " + device.deviceAddress);

                // Example: only connect to devices with "Receiver" in the name
                if (!device.deviceAddress.equals("40:c4:14:03:46:95") || hasAttemptedConnection) continue;
                hasAttemptedConnection = true;

                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;

                manager.connect(channel, config, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Connecting to " + device.deviceName);
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG, "Connection failed: " + reason);
                    }
                });

                break; // Only connect to one device!
            }
        };
    }

    public ConnectionInfoListener getConnectionListener(String message) {
        return info -> {
            wifiP2pInfo = info;
            if (info.groupFormed && !info.isGroupOwner) {
                Log.d(TAG, "Group formed, I am client");
                new ClientThread(info.groupOwnerAddress, message).start();
            }
        };
    }

    private static class ClientThread extends Thread {
        private final InetAddress hostAddress;
        private final String message;

        ClientThread(InetAddress hostAddress, String message) {
            this.hostAddress = hostAddress;
            this.message = message;
        }

        public void run() {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(hostAddress, 5050), 5000);
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message);
                Log.d(TAG, "Message sent: " + message);
            } catch (Exception e) {
                Log.e(TAG, "Send error", e);
            }
        }
    }
}

