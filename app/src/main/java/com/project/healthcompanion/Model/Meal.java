package com.project.healthcompanion.Model;


import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Meal {
    String mealName;
    Time startTime, endTime;
    private Double totalCalories;
    private Double totalFats;
    private Double totalCarbs;
    private Double totalProts;
    private List<Food> foods;

    Meal(String mealName) {
        this.mealName = mealName;
    }

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

    public Double getTotalCarbs() {
        return totalCarbs;
    }

    public Double getTotalFats() {
        return totalFats;
    }


    public Double getTotalProts() {
        return totalProts;
    }


    public void addFood(Food food) {
        totalCalories += food.getCalories();
        totalCarbs += food.getTotalCarbohydrate();
        totalFats += food.getTotalFat();
        totalProts += food.getProtein();
        foods.add(food);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public Map<String, Integer> generateFoodMap() {
        Map<String, Integer> foodMap = new HashMap<>();
        for (int i = 0; i < foods.size(); i++) {
            foodMap.put(foods.get(i).getFood_name(), foods.get(i).getServingQty());
        }
        return foodMap;
    }

}


