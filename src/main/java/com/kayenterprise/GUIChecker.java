package com.kayenterprise;

import com.kayenterprise.exceptions.ImageNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import static com.kayenterprise.XmlDomParserReader.getGPUsWithNvidiaCommand;

public class GUIChecker {

    private static final String ICO_PATH = "/pictures/cpu_gpu_temp.ico";

    private static boolean isRunning = Boolean.TRUE;

    public static void main(String[] args) {
        MenuEntity menu = new MenuEntity();

        createAndShowGUI(menu);

        while(isRunning) {
            updateGUI(menu);
        }
        System.exit(0);
    }

    private static void createAndShowGUI(MenuEntity menu) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        menu.setTrayIcon(new TrayIcon(createImage()));
        menu.setPopupMenu(new PopupMenu());
        menu.setSystemTray(SystemTray.getSystemTray());
        menu.setTempItem(new MenuItem("Temperatures"));
        menu.setExitTem(new MenuItem("Exit"));

        //Add components to popup menu
        menu.getPopupMenu().add(menu.getTempItem());
        menu.getPopupMenu().addSeparator();
        menu.getPopupMenu().add(menu.getExitTem());

        menu.getTrayIcon().setPopupMenu(menu.getPopupMenu());

        try {
            menu.getSystemTray().add(menu.getTrayIcon());
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        menu.getTrayIcon().addActionListener(e -> JOptionPane.showMessageDialog(null,
                "This dialog box is run from System Tray"));

        menu.getExitTem().addActionListener(e -> {
            menu.getSystemTray().remove(menu.getTrayIcon());
            isRunning = Boolean.FALSE;
        });

        menu.getTempItem().setEnabled(false);
    }

    private static void updateGUI(MenuEntity menu) {
        String cardsAndTemperatures = fetchTemperatures();
        menu.getTrayIcon().setToolTip(cardsAndTemperatures);
        menu.getTempItem().setLabel(cardsAndTemperatures);
    }

    protected static Image createImage() {
        URL urlIcon = GUIChecker.class.getResource(GUIChecker.ICO_PATH);

        if (urlIcon == null) {
            System.err.println("Resource not found: " + GUIChecker.ICO_PATH);
            throw new ImageNotFoundException(GUIChecker.ICO_PATH);
        } else {
            return (new ImageIcon(String.valueOf(urlIcon), "tray icon")).getImage();
        }
    }

    protected static String fetchTemperatures() {
        return getGPUsWithNvidiaCommand();
    }
}
