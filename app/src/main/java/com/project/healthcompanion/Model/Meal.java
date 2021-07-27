package com.project.healthcompanion.Model;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.util.ArrayList;
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

    public Meal(String mealName) {
        this.mealName = mealName;
        totalCalories = 0.0;
        totalFats = 0.0;
        totalCarbs = 0.0;
        totalProts = 0.0;
        foods = new ArrayList<>();
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

    public Float getTotalCalories() {
        return roundTo2Decs(totalCalories);
    }

    public Float getTotalCarbs() {
        return roundTo2Decs(totalCarbs);
    }

    public Float getTotalFats() {
        return roundTo2Decs(totalFats);
    }


    public Float getTotalProts() {
        return roundTo2Decs(totalProts);
    }


    public int foodExists(String foodName) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getFood_name().equals(foodName))
                return i;
        }
        return -1;
    }

    public void addFood(Food food) {
        totalCalories += food.getCalories() * food.getServingQty();
        totalCarbs += food.getTotalCarbohydrate() * food.getServingQty();
        totalFats += food.getTotalFat() * food.getServingQty();
        totalProts += food.getProtein() * food.getServingQty();

        //food doesnt exists in list
        int index = foodExists(food.getFood_name());
        if (index == -1) {
            foods.add(food);
        } else {//food is there in list
            foods.get(index).setServingQty(foods.get(index).getServingQty() + food.getServingQty());
        }
    }

    public void removeFood(int position) {
        Food food = foods.get(position);
        totalCalories -= food.getCalories() * food.getServingQty();
        totalCarbs -= food.getTotalCarbohydrate() * food.getServingQty();
        totalFats -= food.getTotalFat() * food.getServingQty();
        totalProts -= food.getProtein() * food.getServingQty();
        foods.remove(position);
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

    private float roundTo2Decs(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

}


