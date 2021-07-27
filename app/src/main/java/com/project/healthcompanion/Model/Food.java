package com.project.healthcompanion.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.project.healthcompanion.Service.DownloadImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Food implements Serializable {
    private String food_name;
    private String id;
    private int servingQty;
    private String servingUnit;
    private Double calories;
    private Double totalFat;
    private Double cholesterol;
    private Double totalCarbohydrate;
    private Double sugars;
    private Double protein;
    private Bitmap thumbImage;
    private String thumbImageURL;
    private Bitmap hiResImage;
    private String hiResImageURL;
    private String tagName;
    private Double servingWeight;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(food_name);
        out.writeObject(id);
        out.writeInt(servingQty);
        out.writeObject(servingUnit);
        out.writeDouble(calories);
        out.writeDouble(totalFat);
        out.writeDouble(cholesterol);
        out.writeDouble(totalCarbohydrate);
        out.writeDouble(sugars);
        out.writeDouble(protein);

        if (thumbImage != null) {
            ByteArrayOutputStream thumbImageStream = new ByteArrayOutputStream();
            thumbImage.compress(Bitmap.CompressFormat.PNG, 100, thumbImageStream);
            byte[] thumbImageByteArray = thumbImageStream.toByteArray();
            out.writeInt(thumbImageByteArray.length);
            out.write(thumbImageByteArray);
        } else
            out.writeInt(-1);
        out.writeObject(thumbImageURL);

        if (hiResImage != null) {
            ByteArrayOutputStream hiResImageStream = new ByteArrayOutputStream();
            hiResImage.compress(Bitmap.CompressFormat.PNG, 100, hiResImageStream);
            byte[] hiResImageByteArray = hiResImageStream.toByteArray();
            out.writeInt(hiResImageByteArray.length);
            out.write(hiResImageByteArray);
        } else
            out.writeInt(-1);

        out.writeObject(hiResImageURL);
        out.writeObject(tagName);
        out.writeDouble(protein);

    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        food_name = (String) in.readObject();
        id = (String) in.readObject();
        servingQty = in.readInt();
        servingUnit = (String) in.readObject();
        calories = in.readDouble();
        totalFat = in.readDouble();
        cholesterol = in.readDouble();
        totalCarbohydrate = in.readDouble();
        sugars = in.readDouble();
        protein = in.readDouble();

        int thumbImageBufferLength = in.readInt();
        if (thumbImageBufferLength != -1) {
            byte[] thumbImageByteArray = new byte[thumbImageBufferLength];
            int pos = 0;
            do {
                int read = in.read(thumbImageByteArray, pos, thumbImageBufferLength - pos);

                if (read != -1) {
                    pos += read;
                } else {
                    break;
                }
            } while (pos < thumbImageBufferLength);
            thumbImage = BitmapFactory.decodeByteArray(thumbImageByteArray, 0, thumbImageBufferLength);
        }
        thumbImageURL = (String) in.readObject();

        int hiResImageBufferLength = in.readInt();
        if (hiResImageBufferLength != -1) {
            byte[] hiResImageByteArray = new byte[hiResImageBufferLength];
            int pos2 = 0;
            do {
                int read = in.read(hiResImageByteArray, pos2, hiResImageBufferLength - pos2);

                if (read != -1) {
                    pos2 += read;
                } else {
                    break;
                }
            } while (pos2 < hiResImageBufferLength);
            thumbImage = BitmapFactory.decodeByteArray(hiResImageByteArray, 0, hiResImageBufferLength);
        }
        hiResImageURL = (String) in.readObject();
        tagName = (String) in.readObject();
        protein = in.readDouble();

    }


    public Food(String food_name, String id, int servingQty, String servingUnit,
                Double calories, Double totalFat, Double cholesterol, Double totalCarbohydrate,
                Double sugars, Double protein, String thumbImageURL, String hiResImageURL, String tagName, Double servingWeight) {

        this.food_name = food_name;
        this.id = id;
        this.servingQty = servingQty;
        this.servingUnit = servingUnit;
        this.calories = calories;
        this.totalFat = totalFat;
        this.cholesterol = cholesterol;
        this.totalCarbohydrate = totalCarbohydrate;
        this.sugars = sugars;
        this.protein = protein;
        this.thumbImageURL = thumbImageURL;
        this.hiResImageURL = hiResImageURL;
        this.tagName = tagName;
        this.servingWeight = servingWeight;
    }

    public Food() {
    }

    @Override
    public String toString() {
        return "\nFood Name: " + food_name + "\n" +
                "Serving Quantity: " + servingQty + '\n' +
                "Serving Unit: " + servingUnit + '\n' +
                "Calories: " + calories + '\n' +
                "Total Fat: " + totalFat + "g" + '\n' +
                "Cholesterol: " + cholesterol + "mg" + '\n' +
                "Total Carbohydrate: " + totalCarbohydrate + "g" + '\n' +
                "Sugars: " + sugars + "g" + '\n' +
                "Protein: " + protein + "g" + '\n' + '\n';

    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        food_name = food_name.substring(0, 1).toUpperCase() + food_name.substring(1).toLowerCase();
        this.food_name = food_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getServingQty() {
        return servingQty;
    }

    public void setServingQty(int servingQty) {
        this.servingQty = servingQty;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(Double totalFat) {
        this.totalFat = totalFat;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(Double totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public Double getSugars() {
        return sugars;
    }

    public void setSugars(Double sugars) {
        this.sugars = sugars;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }


    public Bitmap getThumbImage() {
        if (this.thumbImage == null)
            this.thumbImage = getImageFromURL(thumbImageURL);
        return this.thumbImage;
    }

    public void setThumbImage(String thumbImageURL) {
        this.thumbImageURL = thumbImageURL;
    }

    public Bitmap getHiResImage() {
        if (this.hiResImage == null)
            this.hiResImage = getImageFromURL(hiResImageURL);
        return this.hiResImage;
    }

    public void setHiResImage(String hiResImageURL) {
        this.hiResImageURL = hiResImageURL;
    }


    private Bitmap getImageFromURL(String URL) {
        Bitmap image = null;
        DownloadImage downloadImage = new DownloadImage();
        try {
            image = downloadImage.execute(URL).get();
        } catch (CancellationException | ExecutionException | InterruptedException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        return image;
    }


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Double getServingWeight() {
        return servingWeight;
    }

    public void setServingWeight(Double servingWeight) {
        this.servingWeight = servingWeight;
    }
}


