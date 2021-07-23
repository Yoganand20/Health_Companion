package com.project.healthcompanion.Model;

import java.time.DayOfWeek;
import java.util.List;

public class Day {
    DayOfWeek dayOfWeek;
    private Double totalCalories;
    private Double totalFats;
    private Double totalCarbs;
    private Double totalProts;
    private List<Meal> meals;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public void addToMeals(Meal meal) {
        meals.add(meal);
    }

    public List<Meal> getDays() {
        return meals;
    }

    public void setDays(List<Meal> meals) {
        this.meals = meals;
    }
}
