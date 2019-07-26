package co.ali.rickandmortyapp.service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JsonParser {
    private String TAG = JsonParser.class.getSimpleName();

    public LinkedHashMap<String, String> getPageInfo(String response) {
        JSONObject infoObj = null;
        LinkedHashMap<String, String> pageInfoMap = new LinkedHashMap<>();

        try {
            //Getting json object from the response
            JSONObject mainObj = new JSONObject(response);

            if (!mainObj.isNull("info")) {
                infoObj = new JSONObject(mainObj.getString("info"));
            }

            if (infoObj == null) {
                return null;
            }

            if (!infoObj.isNull("count")) {
                pageInfoMap.put("count", infoObj.getString("count"));
            }

            if (!infoObj.isNull("pages")) {
                pageInfoMap.put("pages", infoObj.getString("pages"));
            }

            if (!infoObj.isNull("next")) {
                pageInfoMap.put("next", infoObj.getString("next"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pageInfoMap;
    }

    public List<LinkedHashMap<String, String>> getEpisodeInfo(String response) {
        JSONArray jsonArray = null;

        try {
            //Getting json object from the response
            JSONObject mainObj = new JSONObject(response);

            if (!mainObj.isNull("results")) {
                jsonArray = mainObj.getJSONArray("results");
            }

            if (jsonArray == null) {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getEpisodeDetails(jsonArray);
    }

    private List<LinkedHashMap<String, String>> getEpisodeDetails(JSONArray jsonArray) {
        int detailsCount = jsonArray.length();
        List<LinkedHashMap<String, String>> responseList = new ArrayList<>();
        LinkedHashMap<String, String> responseMap;

        Log.i(TAG,"getResponseDetails detailsCount:"+detailsCount);

        for (int i = 0; i < detailsCount; i++) {
            try {
                responseMap = getEpisodeDetail((JSONObject) jsonArray.get(i));
                responseList.add(responseMap);

            } catch (JSONException e) {
                Log.i(TAG,"Error in Adding places");
                e.printStackTrace();
            }
        }
        return responseList;
    }

    private LinkedHashMap<String, String> getEpisodeDetail(JSONObject responseDetailJson) {
        LinkedHashMap<String, String> responseMap = new LinkedHashMap<String, String>();

        try {
            if (!responseDetailJson.isNull("id")) {
                responseMap.put("id", responseDetailJson.getString("id"));
            }

            if (!responseDetailJson.isNull("name")) {
                responseMap.put("name", responseDetailJson.getString("name"));
            }

            if (!responseDetailJson.isNull("air_date")) {
                responseMap.put("air_date", responseDetailJson.getString("air_date"));
            }

            if (!responseDetailJson.isNull("episode")) {
                responseMap.put("episode", responseDetailJson.getString("episode"));
            }
        } catch (JSONException e) {
            Log.i(TAG,"Error");
            e.printStackTrace();
        }

        return responseMap;
    }

    public LinkedHashMap<String, List<String>> getCharacterInfo(String response) {
        JSONArray resultsArray = null;
        LinkedHashMap<String, List<String>> charactersMap = new LinkedHashMap<>();

        try {
            //Getting json object from the response
            JSONObject mainObj = new JSONObject(response);

            if (!mainObj.isNull("results")) {
                resultsArray = mainObj.getJSONArray("results");
            }

            if (resultsArray == null) {
                return null;
            }

            for (int i = 0; i < resultsArray.length(); i++) {
                try {
                    JSONObject responseDetailJson = (JSONObject) resultsArray.get(i);

                    if (!responseDetailJson.isNull("id")) {
                        List<String> charactersList = getCharacters(responseDetailJson);
                        if (charactersList!= null && charactersList.size() > 0) {
                            charactersMap.put(responseDetailJson.getString("id"), charactersList);
                        }
                    }
                } catch (JSONException e) {
                    Log.i(TAG,"Error in Adding places");
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return charactersMap;
    }

    private List<String> getCharacters(JSONObject responseDetailJson) {
        JSONArray charactersArray;
        List<String> responseList = new ArrayList<>();

        try {
            if (!responseDetailJson.isNull("characters")) {
                charactersArray = responseDetailJson.getJSONArray("characters");
                if (charactersArray == null) {
                    return null;
                }

                for (int j = 0; j < charactersArray.length(); j++) {
                    responseList.add(charactersArray.get(j).toString());
                }
            }
        } catch (JSONException e) {
            Log.i(TAG,"Error");
            e.printStackTrace();
        }

        return responseList;
    }

    public LinkedHashMap<String, String> getCharacterDetails(String response) {
        LinkedHashMap<String, String> responseMap = new LinkedHashMap<String, String>();

        try {
            //Getting json object from the response
            JSONObject mainObj = new JSONObject(response);

            if (!mainObj.isNull("id")) {
                responseMap.put("id", mainObj.getString("id"));
            }

            if (!mainObj.isNull("name")) {
                responseMap.put("name", mainObj.getString("name"));
            }

            if (!mainObj.isNull("status")) {
                responseMap.put("status", mainObj.getString("status"));
            }

            if (!mainObj.isNull("species")) {
                responseMap.put("species", mainObj.getString("species"));
            }

            if (!mainObj.isNull("image")) {
                responseMap.put("image", mainObj.getString("image"));
            }

            if (!mainObj.isNull("type")) {
                responseMap.put("type", mainObj.getString("type"));
            }

            if (!mainObj.isNull("gender")) {
                responseMap.put("gender", mainObj.getString("gender"));
            }

            if (!mainObj.isNull("created")) {
                responseMap.put("created", mainObj.getString("created"));
            }

            if (!mainObj.isNull("origin")) {
                JSONObject jsonObj =  mainObj.getJSONObject("origin");
                if (!jsonObj.isNull("name")) {
                    responseMap.put("origin", jsonObj.getString("name"));
                }
            }

            if (!mainObj.isNull("location")) {
                JSONObject jsonObj =  mainObj.getJSONObject("location");
                if (!jsonObj.isNull("name")) {
                    responseMap.put("location", jsonObj.getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseMap;
    }
}
