package com.project.healthcompanion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityLevelViewModel extends ViewModel {
    private final MutableLiveData<String> activityLevel = new MutableLiveData<>();

    public LiveData<String> getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel.setValue(activityLevel);
    }
}

