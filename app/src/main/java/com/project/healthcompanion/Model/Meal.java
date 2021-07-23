package com.project.healthcompanion.Model;


import java.sql.Time;
import java.util.List;

public class Meal {
    String mealName;
    Time startTime, endTime;
    private Double totalCalories;
    private Double totalFats;
    private Double totalCarbs;
    private Double totalProts;
    private List<Food> foods;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setMealTime(Time startTime, Time endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Time[] getMealTime() {
        return new Time[]{startTime, endTime};
    }

    public Double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public Double getTotalCarbs() {
        return totalCarbs;
    }

    public void setTotalCarbs(Double totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public Double getTotalFats() {
        return totalFats;
    }

    public void setTotalFats(Double totalFats) {
        this.totalFats = totalFats;
    }

    public Double getTotalProts() {
        return totalProts;
    }

    public void setTotalProts(Double totalProts) {
        this.totalProts = totalProts;
    }

    public void addToFoods(Food food) {
        foods.add(food);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

}
