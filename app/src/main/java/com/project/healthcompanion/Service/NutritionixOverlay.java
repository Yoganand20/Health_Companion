package com.project.healthcompanion.Service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutritionixOverlay {
    public static final String NUTRITIONIX_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    public static final String NUTRITIONIX_INSTANT_URL = "https://trackapi.nutritionix.com/v2/search/instant";
    Context context;

    public NutritionixOverlay(Context context) {
        this.context = context;
    }

    public void getFood(final String foodName, final NutrientResponse nutrientResponse) {

        String url = NUTRITIONIX_URL;

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", foodName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final List<Food> NutrientReportModel = new ArrayList<>();

        //get json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

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
                params.put("x-app-id", "761545d2");
                params.put("x-app-key", "9c446a74a908b605a2767a15e244cca3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    //implementing search,autocomplete
    public void getSearchResult(final String foodName, final SearchSuggestionResponse searchSuggestionResponse) {

        String url = NUTRITIONIX_INSTANT_URL;

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", foodName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final List<SuggestionItem> SearchSuggestionQueue = new ArrayList<>();

        //get json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("API Request", "Response Received");
                    Toast.makeText(context, "Response recieved", Toast.LENGTH_SHORT);
                    JSONArray commonFoods = response.getJSONArray("common");
                    JSONArray brandedFoods = response.getJSONArray("branded");
                    for (int i = 0; i < brandedFoods.length(); i++) {
                        SuggestionItem Food = new SuggestionItem();

                        JSONObject food = (JSONObject) brandedFoods.get(i);

                        Food.setImage(food.getString("image"));
                        Food.setFoodName(food.getString("food_name"));
                        Food.setBrandName(food.getString("brand_name"));
                        Food.setServingUnit(food.getString("serving_unit"));
                        Food.setFoodID(food.getString("nix_item_id"));
                        Food.setTotalCal((int) food.getLong("nf_calories"));
                        Food.setServingQty(food.getInt("serving_qty"));

                        SearchSuggestionQueue.add(Food);
                    }
                    for (int i = 0; i < commonFoods.length(); i++) {
                        SuggestionItem Food = new SuggestionItem();

                        JSONObject food = (JSONObject) commonFoods.get(i);

                        Food.setImage(food.getString("image"));
                        Food.setFoodName(food.getString("food_name"));
                        Food.setFoodID(food.getString("tag_id"));
                        Food.setTagName(food.getString("tag_name"));
                        SearchSuggestionQueue.add(Food);
                    }
                    searchSuggestionResponse.onSuccess(SearchSuggestionQueue);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchSuggestionResponse.onError("Error");

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

    public interface NutrientResponse {
        void onSuccess(List<Food> Foods);

        void onError(String message);
    }

    public interface SearchSuggestionResponse {
        void onSuccess(List<SuggestionItem> Suggestions);

        void onError(String message);
    }
}
