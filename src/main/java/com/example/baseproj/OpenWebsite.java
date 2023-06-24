package com.example.baseproj;

import java.awt.Desktop;
import java.net.URI;

public class OpenWebsite {
    public static void main(String[] args) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI("https://www.youtube.com/watch?v=fGlTwlryXSM&t=73s");
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}