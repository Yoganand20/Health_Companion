package com.project.healthcompanion.Model;

import android.graphics.Bitmap;
import android.util.Log;

import com.project.healthcompanion.Service.DownloadImage;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class SuggestionItem {
    private String imageURL;
    private Bitmap image;
    private String foodID;
    private String foodName;
    private String tagName;
    private Double calories;
    private String servingUnit;
    private Integer servingQty;

    public SuggestionItem() {
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(String imageURL) {
        if (imageURL != null)
            this.image = getImageFromURL(imageURL);
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public Integer getServingQty() {
        return servingQty;
    }

    public void setServingQty(Integer servingQty) {
        this.servingQty = servingQty;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    private Bitmap getImageFromURL(String URL) {
        Bitmap image = null;
        DownloadImage downloadImage = new DownloadImage();
        try {
            image = downloadImage.execute(URL).get();
        } catch (CancellationException ce) {
            Log.e(getClass().getName(), "Image Download Cancelled");
        } catch (ExecutionException ee) {
            Log.e(getClass().getName(), "Image Download Failed");
        } catch (InterruptedException ie) {
            Log.e(getClass().getName(), "Image Download Interrupted");
        }
        return image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }
}
