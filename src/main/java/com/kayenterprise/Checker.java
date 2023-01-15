package com.kayenterprise;

import com.kayenterprise.exceptions.ImageNotFoundException;

import java.awt.*;
import java.awt.event.ActionListener;

import static com.kayenterprise.XmlDomParserReader.getGPUsWithNvidiaCommand;

public class Checker {

    private static final String ICO_PATH = "puzzle-blue.gif";

    private static boolean isRunning = Boolean.TRUE;

    public static void main(String[] args) {
        final TrayIcon trayIcon = createAndShowGUI();
        while (isRunning) {
            updateGUI(trayIcon);
        }
        System.exit(0);
    }

    private static TrayIcon createAndShowGUI() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            System.exit(0);
        }

        SystemTray tray = SystemTray.getSystemTray();

        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        TrayIcon trayIcon = new TrayIcon(createImage(ICO_PATH), "", popup);
        trayIcon.setImageAutoSize(true);

        ActionListener exitListener = e -> {
            SystemTray.getSystemTray().remove(trayIcon);
            isRunning = Boolean.FALSE;
        };

        exitItem.addActionListener(exitListener);
        popup.add(exitItem);


        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }

        return trayIcon;
    }

    private static void updateGUI(TrayIcon trayIcon) {
        String cardsAndTemperatures = fetchTemperatures();
        String mouseBattery = fetchMouseBattery();
        String headSetBattery = fetchHeadsetBattery();
        trayIcon.setToolTip(cardsAndTemperatures + "\n" + mouseBattery + "\n" + headSetBattery);
    }

    private static Image createImage(String path) {
        Image image = Toolkit.getDefaultToolkit().getImage(path);

        if (image == null) {
            System.err.println("Resource not found: " + path);
            throw new ImageNotFoundException(path);
        } else {
            return image;
        }
    }

    private static String fetchTemperatures() {
        return getGPUsWithNvidiaCommand();
    }

    private static String fetchMouseBattery() {
        return "Mouse: 0";
    }

    private static String fetchHeadsetBattery() {
        return "Headset: 0";
    }
}
