package co.ali.rickandmortyapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import org.json.JSONObject;

import co.ali.rickandmortyapp.R;

public class JsonService extends IntentService {
    public static final String BASE_URL = "https://rickandmortyapi.com/api/episode";
    public static final String PARAM_REQUEST = "api_request";
    public static final String PARAM_RESULT = "api_result";
    public static final String PARAM_ACTION_TYPE = "action_type";
    public static final String PARAM_CHARACTER_ID = "character_id";
    public static final String ACTION_EPISODES_RESP = "co.ali.rickandmortyapp.episodes_response";
    public static final String ACTION_CHARACTERS_RESP = "co.ali.rickandmortyapp.characters_response";
    public static final String ACTION_IMAGE_RESP = "co.ali.rickandmortyapp.image_response";

    public JsonService() {
        super("JsonService");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (intent == null || intent.getExtras() == null) {
                displayExceptionMessage(getString(R.string.unexpected_error));
                return;
            }

            String requestUrl = null, actionType = null;
            if (intent.hasExtra(PARAM_REQUEST))
                requestUrl = intent.getStringExtra(PARAM_REQUEST);

            if (requestUrl == null) {
                displayExceptionMessage(getString(R.string.unexpected_error));
                return;
            }

            Intent broadcastIntent = new Intent();
            if (intent.hasExtra(PARAM_ACTION_TYPE)) {
                actionType = intent.getStringExtra(PARAM_ACTION_TYPE);
            }

            if (actionType == null) {
                displayExceptionMessage(getString(R.string.unexpected_error));
                return;
            }

            ExceptionHandler exceptionHandler = new ExceptionHandler() {
                @Override
                public void displayExceptionMessage(String message) {
                    Toast.makeText(JsonService.this, message, Toast.LENGTH_LONG).show();
                }
            };

            JsonDownloadData jsonDownload = new JsonDownloadData(exceptionHandler);

            broadcastIntent.setAction(actionType);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(PARAM_REQUEST, requestUrl);

            if (actionType.equals(ACTION_IMAGE_RESP)) {
                Bitmap imageBitmap = jsonDownload.downloadImageFromUrl(requestUrl);
                String characterId = null;

                if (intent.hasExtra(JsonService.PARAM_CHARACTER_ID)) {
                    characterId = intent.getStringExtra(JsonService.PARAM_CHARACTER_ID);
                }

                if (characterId == null) {
                    displayExceptionMessage(getString(R.string.unexpected_error));
                    return;
                }

                broadcastIntent.putExtra(PARAM_RESULT, imageBitmap);
                broadcastIntent.putExtra(PARAM_CHARACTER_ID, characterId);
            } else {
                JSONObject json = jsonDownload.getJSONFromUrl(requestUrl);

                if(json == null)
                    broadcastIntent.putExtra(PARAM_RESULT, "");
                else
                    broadcastIntent.putExtra(PARAM_RESULT, json.toString());
            }

            sendBroadcast(broadcastIntent);
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }
    }

    private void displayExceptionMessage(String message) {
        Toast.makeText(JsonService.this, message, Toast.LENGTH_LONG).show();
    }
}
