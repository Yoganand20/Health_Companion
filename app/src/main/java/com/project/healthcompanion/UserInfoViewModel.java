package com.project.healthcompanion;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class UserInfoViewModel extends ViewModel {
    private final MutableLiveData<String> firstName = new MutableLiveData<String>();
    private final MutableLiveData<String> lastName = new MutableLiveData<String>();
    private final MutableLiveData<String> gender = new MutableLiveData<String>();
    private final MutableLiveData<Date> dateOfBirth = new MutableLiveData<Date>();
    private final MutableLiveData<Bitmap> profilePic = new MutableLiveData<Bitmap>();
    private final MutableLiveData<Float> TDEE = new MutableLiveData<Float>();
    private final MutableLiveData<Float> PAL = new MutableLiveData<Float>();
    private final MutableLiveData<Float> BMI = new MutableLiveData<Float>();
    private final MutableLiveData<Float> weight = new MutableLiveData<Float>();
    private final MutableLiveData<Float> height = new MutableLiveData<Float>();
    private final MutableLiveData<Integer> activityLevel = new MutableLiveData<Integer>();

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic.setValue(profilePic);
    }

    public LiveData<Bitmap> getProfilePic() {
        return profilePic;
    }

    public void setFirstName(String fname) {
        firstName.setValue(fname);
    }

    public LiveData<String> getFirstName() {
        return firstName;
    }

    public void setLastName(String lname) {
        lastName.setValue(lname);
    }

    public LiveData<String> getLastName() {
        return firstName;
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public LiveData<String> getGender() {
        return gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth.setValue(dateOfBirth);
    }

    public LiveData<Date> getDateOfBirth() {
        return dateOfBirth;
    }


    public void setTDEE(Float TDEE) {
        this.TDEE.setValue(TDEE);
    }

    public LiveData<Float> getTDEE() {
        return TDEE;
    }

    public void setPAL(Float PAL) {
        this.PAL.setValue(PAL);
    }

    public LiveData<Float> getPAL() {
        return PAL;
    }

    public void setBMI(Float BMI) {
        this.BMI.setValue(BMI);
    }

    public LiveData<Float> getBMI() {
        return BMI;
    }

    public void setWeight(String weight) {
        if (weight != null && !weight.isEmpty())
            this.weight.setValue(Float.parseFloat(weight));
    }

    public LiveData<Float> getWeight() {
        return weight;
    }

    public void setHeight(String height) {
        if (height != null && !height.isEmpty())
            this.height.setValue(Float.parseFloat(height));
    }

    public LiveData<Float> getHeight() {
        return height;
    }

    public void setActivityLevel(String ActivityLevel) {
        if (ActivityLevel.equals("light_activity_lifestyle")) {
            activityLevel.setValue(0);
        }
        if (ActivityLevel.equals("moderate_activity_lifestyle")) {
            activityLevel.setValue(1);
        }
        if (ActivityLevel.equals("vigorous_activity_lifestyle")) {
            activityLevel.setValue(2);
        }

    }

    public LiveData<Integer> getActivityLevel() {
        return activityLevel;
    }

}

