package com.softserve.actent.service.impl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LoadImage {
    public static void main(String[] args) {
        try {
            BufferedImage img = ImageIO.read(new URL("https://maps.googleapis.com/maps/api/staticmap?" +
                    "zoom=13&size=200x200&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&key=AIzaSyBv-OXv1r8uXiUBXosLfN9xgrYB3Anr2Lg"));

            File outputfile = new File("map.jpg");
            ImageIO.write(img, "jpg", outputfile);
            System.out.println("Saved!");
        } catch (IOException ex) {
            System.out.println("Error!" + ex);
        }
    }
}
