package com.project.healthcompanion.Model;

import java.util.List;

public class DietPlan {
    private String dietPlanName;
    private Double totalCalories;
    private Double totalFats;
    private Double totalCarbs;
    private Double totalProts;
    private List<Day> days;

    public String getDietPlanName() {
        return dietPlanName;
    }

    public void setDietPlanName(String dietPlanName) {
        this.dietPlanName = dietPlanName;
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

    public void addToDays(Day day) {
        days.add(day);
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
