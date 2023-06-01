package com.mohamedhamza.foodplanner.view.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mohamed Hamza on 5/30/2023.
 */

public class IconManager {

    private Context context;

    public IconManager(Context context) {
        this.context = context;
    }

    public Drawable getCategoryIcon(String categoryName) {
        try {
            InputStream inputStream = context.getAssets().open("icons/categories/" + categoryName + ".png");
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            // Return a default icon or handle the error accordingly
            return null;
        }
    }

    public Drawable getAreaIcon(String areaName) {
        try {
            InputStream inputStream = context.getAssets().open("icons/areas/" + areaName + ".png");
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            // Return a default icon or handle the error accordingly
            return null;
        }
    }
}
