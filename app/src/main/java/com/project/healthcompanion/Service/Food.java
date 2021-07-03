package com.project.healthcompanion.Service;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Food implements Serializable {
    private String food_name;
    private String brand_name;
    private String id;
    private int serving_qty;
    private String serving_unit;
    private float calories;
    private float total_fat;
    private float saturated_fat;
    private float cholesterol;
    private float sodium;
    private float total_carbohydrate;
    private String sugars;
    private float protein;
    private float potassium;
    private Bitmap thumbImage;
    private Bitmap hiResImage;
    private boolean isBranded = false;
    private String tagName;

    public Food(String food_name, String brand_name, String id, int serving_qty, String serving_unit,
                float calories, float total_fat, float saturated_fat, float cholesterol, float sodium, float total_carbohydrate,
                String sugars, float protein, float potassium, String thumbImageURL, String HiResImageURL, boolean isBranded, String tagName) {

        this.food_name = food_name;
        this.brand_name = brand_name;
        this.id = id;
        this.serving_qty = serving_qty;
        this.serving_unit = serving_unit;
        this.calories = calories;
        this.total_fat = total_fat;
        this.saturated_fat = saturated_fat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.total_carbohydrate = total_carbohydrate;
        this.sugars = sugars;
        this.protein = protein;
        this.potassium = potassium;
        this.thumbImage = getImageFromURL(thumbImageURL);
        this.hiResImage = getImageFromURL(HiResImageURL);
        this.isBranded = isBranded;
        this.tagName = tagName;
    }

    public Food() {
        isBranded = false;
    }

    @Override
    public String toString() {
        return "\nFood Name: " + food_name + "\n" +
                "Serving Quantity: " + serving_qty + '\n' +
                "Serving Unit: " + serving_unit + '\n' +
                "Calories: " + calories + '\n' +
                "Total Fat: " + total_fat + "g" + '\n' +
                "Saturated Fat: " + saturated_fat + "g" + '\n' +
                "Cholesterol: " + cholesterol + "mg" + '\n' +
                "Sodium: " + sodium + "mg" + '\n' +
                "Total Carbohydrate: " + total_carbohydrate + "g" + '\n' +
                "Sugars: " + sugars + "g" + '\n' +
                "Protein: " + protein + "g" + '\n' +
                "Potassium: " + potassium + "mg" + '\n';

    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        food_name = food_name.substring(0, 1).toUpperCase() + food_name.substring(1).toLowerCase();
        this.food_name = food_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        brand_name = brand_name.substring(0, 1).toUpperCase() + brand_name.substring(1).toLowerCase();
        this.brand_name = brand_name;
        this.isBranded = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(int serving_qty) {
        this.serving_qty = serving_qty;
    }

    public String getServing_unit() {
        return serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getTotal_fat() {
        return total_fat;
    }

    public void setTotal_fat(float total_fat) {
        this.total_fat = total_fat;
    }

    public float getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(float saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public float getTotal_carbohydrate() {
        return total_carbohydrate;
    }

    public void setTotal_carbohydrate(float total_carbohydrate) {
        this.total_carbohydrate = total_carbohydrate;
    }

    public String getSugars() {
        return sugars;
    }

    public void setSugars(String sugars) {
        this.sugars = sugars;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getPotassium() {
        return potassium;
    }

    public void setPotassium(float potassium) {
        this.potassium = potassium;
    }

    public Bitmap getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImageURL) {
        this.thumbImage = getImageFromURL(thumbImageURL);
    }

    public Bitmap getHiResImage() {
        return hiResImage;
    }

    public void setHiResImage(String hiResImageURL) {
        this.hiResImage = getImageFromURL(hiResImageURL);
    }

    public boolean isFoodBranded() {
        return isBranded;
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


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}


