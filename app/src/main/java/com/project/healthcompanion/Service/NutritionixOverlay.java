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

    // private static final String x_app_id="1440d43b";
    //private static final String x_app_key="b44fbd24ce80dffd91f4ada61316f4c3";
    // private static final String x_remote_user_id="0";

    private static final String x_app_id = "761545d2";
    private static final String x_app_key = "9c446a74a908b605a2767a15e244cca3";
    private static final String x_remote_user_id = "0";

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
                params.put("x-app-id", x_app_id);
                params.put("x-app-key", x_app_key);
                params.put("x-remote-user-id", x_remote_user_id);
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
                params.put("x-app-id", x_app_id);
                params.put("x-app-key", x_app_key);
                params.put("x-remote-user-id", x_remote_user_id);
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
                params.put("x-app-id", x_app_id);
                params.put("x-app-key", x_app_key);
                params.put("x-remote-user-id", x_remote_user_id);
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

}