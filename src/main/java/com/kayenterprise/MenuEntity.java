package com.kayenterprise;

import java.awt.*;

public class MenuEntity {

    private TrayIcon trayIcon;
    private PopupMenu popupMenu;
    private SystemTray systemTray;
    private MenuItem tempItem;
    private MenuItem exitTem;

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public void setTrayIcon(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    public PopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setPopupMenu(PopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public SystemTray getSystemTray() {
        return systemTray;
    }

    public void setSystemTray(SystemTray systemTray) {
        this.systemTray = systemTray;
    }

    public MenuItem getTempItem() {
        return tempItem;
    }

    public void setTempItem(MenuItem tempItem) {
        this.tempItem = tempItem;
    }

    public MenuItem getExitTem() {
        return exitTem;
    }

    public void setExitTem(MenuItem exitTem) {
        this.exitTem = exitTem;
    }
}
