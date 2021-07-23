package com.project.healthcompanion.Service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.Model.SuggestionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutritionixOverlay {
    Context context;
    public static final String NUTRITIONIX_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    public static final String INSTANT_URL = "https://trackapi.nutritionix.com/v2/search/instant";
    private final RequestQueue queue;


    public NutritionixOverlay(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void getResults(String query, final SearchSuggestionResponse searchSuggestionResponse) {
        String URL = INSTANT_URL + "?query=" + query;

        ArrayList<SuggestionItem> SearchSuggestion = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray commonFoods = jsonObject.getJSONArray("common");
                    for (int i = 0; i < commonFoods.length(); i++) {
                        SuggestionItem food = new SuggestionItem();

                        JSONObject foodObject = (JSONObject) commonFoods.get(i);

                        food.setImage(foodObject.getJSONObject("photo").getString("thumb"));
                        food.setFoodName(foodObject.getString("food_name"));

                        SearchSuggestion.add(food);

                    }
                    searchSuggestionResponse.onSuccess(SearchSuggestion);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchSuggestionResponse.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "1440d43b");
                params.put("x-app-key", "b44fbd24ce80dffd91f4ada61316f4c3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getFood(String query, final NutrientResponse nutrientResponse) {
        String URL = NUTRITIONIX_URL;

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", query);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Food foodItem = new Food();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray foods = response.getJSONArray("foods");

                    JSONObject food = foods.getJSONObject(0);


                    foodItem.setThumbImage(food.getJSONObject("photo").getString("thumb"));
                    foodItem.setFood_name(food.getString("food_name"));
                    foodItem.setHiResImage(food.getJSONObject("photo").getString("highres"));
                    foodItem.setServingQty(food.getInt("serving_qty"));
                    foodItem.setServingUnit(food.getString("serving_unit"));
                    foodItem.setCalories(food.getDouble("nf_calories"));
                    foodItem.setTotalFat(food.getDouble("nf_total_fat"));
                    foodItem.setCholesterol(food.getDouble("nf_cholesterol"));
                    foodItem.setSugars(food.getDouble("nf_sugars"));
                    foodItem.setTotalCarbohydrate(food.getDouble("nf_total_carbohydrate"));
                    foodItem.setProtein(food.getDouble("nf_protein"));
                    foodItem.setServingWeight(food.getDouble("serving_weight_grams"));


                    nutrientResponse.onSuccess(foodItem);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nutrientResponse.onError(error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "1440d43b");
                params.put("x-app-key", "b44fbd24ce80dffd91f4ada61316f4c3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getFoods(String query, final NutrientArrayResponse nutrientArrayResponse) {
        String URL = NUTRITIONIX_URL;

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", query);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Food> foods = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray foodsJSON = response.getJSONArray("foods");

                    for (int i = 0; i < foodsJSON.length(); i++) {
                        JSONObject food = foodsJSON.getJSONObject(i);

                        Food foodItem = new Food();

                        foodItem.setThumbImage(food.getJSONObject("photo").getString("thumb"));
                        foodItem.setFood_name(food.getString("food_name"));
                        foodItem.setHiResImage(food.getJSONObject("photo").getString("highres"));
                        foodItem.setServingQty(food.getInt("serving_qty"));
                        foodItem.setServingUnit(food.getString("serving_unit"));
                        foodItem.setCalories(food.getDouble("nf_calories"));
                        foodItem.setTotalFat(food.getDouble("nf_total_fat"));
                        foodItem.setCholesterol(food.getDouble("nf_cholesterol"));
                        foodItem.setSugars(food.getDouble("nf_sugars"));
                        foodItem.setTotalCarbohydrate(food.getDouble("nf_total_carbohydrate"));
                        foodItem.setProtein(food.getDouble("nf_protein"));
                        foodItem.setServingWeight(food.getDouble("serving_weight_grams"));

                        foods.add(foodItem);
                    }
                    nutrientArrayResponse.onSuccess(foods);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nutrientArrayResponse.onError(error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "1440d43b");
                params.put("x-app-key", "b44fbd24ce80dffd91f4ada61316f4c3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public interface NutrientResponse {
        void onSuccess(Food food);

        void onError(String message);
    }

    public interface NutrientArrayResponse {
        void onSuccess(List<Food> foods);

        void onError(String message);
    }

    public interface SearchSuggestionResponse {
        void onSuccess(ArrayList<SuggestionItem> Suggestions);

        void onError(String message);
    }

/*
    public void getFood(final String foodName, final NutrientResponse nutrientResponse) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("query", foodName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

       final List<Food> NutrientReportModel = new ArrayList<>();

        //get json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, NUTRITIONIX_URL, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("tag","inside method");
                    JSONArray foods = response.getJSONArray("foods");
                    for (int i = 0; i < foods.length(); i++) {
                        Food Food = new Food();
                        int sum = 0;

                        JSONObject firstFood = (JSONObject) foods.get(i);
                        sum += firstFood.getInt("nf_calories");

                        Food.setThumbImage(firstFood.getJSONObject("photo").getString("thumb"));
                        Food.setFood_name(firstFood.getString("food_name"));
                        Food.setServing_qty(firstFood.getInt("serving_qty"));
                        Food.setServing_unit(firstFood.getString("serving_unit"));
                        Food.setCalories(firstFood.getLong("nf_calories"));
                        Food.setTotal_fat(firstFood.getLong("nf_total_fat"));
                        Food.setSaturated_fat(firstFood.getLong("nf_saturated_fat"));
                        Food.setCholesterol(firstFood.getLong("nf_cholesterol"));
                        Food.setSodium(firstFood.getLong("nf_sodium"));
                        Food.setTotal_carbohydrate(firstFood.getLong("nf_total_carbohydrate"));
                        Food.setSugars(firstFood.getString("nf_sugars"));
                        Food.setProtein(firstFood.getLong("nf_protein"));
                        Food.setPotassium(firstFood.getLong("nf_potassium"));

                        NutrientReportModel.add(Food);
                    }
                    nutrientResponse.onSuccess(NutrientReportModel);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nutrientResponse.onError("Error");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "1440d43b");
                params.put("x-app-key", "b44fbd24ce80dffd91f4ada61316f4c3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public void getResults(String query,final SearchSuggestionResponse searchSuggestionResponse){
        String URL=INSTANT_URL+"?query="+query;

        ArrayList<SuggestionItem> SearchSuggestion = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray commonFoods = jsonObject.getJSONArray("common");
                    for (int i = 0; i < commonFoods.length(); i++) {
                        SuggestionItem Food = new SuggestionItem();

                        JSONObject food = (JSONObject) commonFoods.get(i);

                        Food.setImage(food.getJSONObject("photo").getString("thumb"));
                        Food.setFoodName(food.getString("food_name"));
                        SearchSuggestion.add(Food);
                    }
                    //Branded food
                    /*
                    JSONArray brandedFoods = jsonObject.getJSONArray("branded");
                    for (int i = 0; i < brandedFoods.length(); i++) {
                        SuggestionItem Food = new SuggestionItem();

                        JSONObject food = (JSONObject) brandedFoods.get(i);

                       // Food.setImage(food.getJSONObject("photo").getString("thumb"));
                        Food.setFoodName(food.getString("food_name"));
                        Food.setBrandName(food.getString("brand_name"));
                        Food.setServingUnit(food.getString("serving_unit"));
                        Food.setFoodID(food.getString("nix_item_id"));
                        Food.setTotalCal((int) food.getLong("nf_calories"));
                        Food.setServingQty(food.getInt("serving_qty"));

                        SearchSuggestion.add(Food);
                    }//*/
            /*
                    searchSuggestionResponse.onSuccess(SearchSuggestion);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchSuggestionResponse.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "1440d43b");
                params.put("x-app-key", "b44fbd24ce80dffd91f4ada61316f4c3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }*/


}
