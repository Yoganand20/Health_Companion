package com.project.healthcompanion;

import android.graphics.Bitmap;

import java.util.Date;

public class UserProfile {
    public static class personalInfo {
        private Bitmap profilePic;
        private String firstName, lastName, gender;
        private Date dateOfBirth;

        public void setProfilePic(Bitmap profilePic) {
            this.profilePic = profilePic;
        }

        public Bitmap getProfilePic() {
            return profilePic;
        }

        public void setUserName(String fname, String lname) {
            firstName = fname;
            lastName = lname;
        }

        public String[] getUserName() {
            return new String[]{firstName, lastName};
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGender() {
            return gender;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }
    }

    public static class userPhysiqueInfo {
        private Float TDEE, PAL, BMI, weight, height;

        public void setTDEE(Float TDEE) {
            this.TDEE = TDEE;
        }

        public Float getTDEE() {
            return TDEE;
        }

        public void setBMI(Float BMI) {
            this.BMI = BMI;
        }

        public Float getBMI() {
            return BMI;
        }

        public void setPAL(Float PAL) {
            this.PAL = PAL;
        }

        public Float getPAL() {
            return PAL;
        }

        public void setHeight(Float height) {
            this.height = height;
        }

        public Float getHeight() {
            return height;
        }

        public void setWeight(Float weight) {
            this.weight = weight;
        }

        public Float getWeight() {
            return weight;
        }
    }

}
