package co.ali.rickandmortyapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import co.ali.rickandmortyapp.common.BaseFragment;
import co.ali.rickandmortyapp.data.CharacterDetails;
import co.ali.rickandmortyapp.data.CharacterImage;
import co.ali.rickandmortyapp.modal.CharactersListAdapter;
import co.ali.rickandmortyapp.service.JsonParser;
import co.ali.rickandmortyapp.service.JsonService;
import co.ali.rickandmortyapp.util.Utils;

import static co.ali.rickandmortyapp.CharacterDisplayFragment.EXTRA_CHARACTER_DETAILS;
import static co.ali.rickandmortyapp.CharacterDisplayFragment.EXTRA_CHARACTER_IMAGE;
import static co.ali.rickandmortyapp.util.Utils.clearAlertDialog;
import static co.ali.rickandmortyapp.util.Utils.createAlertDialog;

public class CharactersFragment extends BaseFragment {
    public static String TAG = CharactersFragment.class.getSimpleName();
    public static String EXTRA_CHARACTERS_LIST = "characters_list";
    public static String EXTRA_SELECTED_EPISODE = "selected_episode";

    private ArrayList<String> charactersList;
    private ResponseReceiver responseReceiver;

    private ProgressBar progressBar;
    private GridView charactersListView;
    private CharactersListAdapter charactersListAdapter = null;
    private String selectedEpisode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CharactersFragment onCreate");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.i(TAG, "CharactersFragment onCreateView");
        View view = inflater.inflate(R.layout.characters_list, viewGroup, false);

        if (getArguments()!= null) {
            charactersList = getArguments().getStringArrayList(EXTRA_CHARACTERS_LIST);
            selectedEpisode = getArguments().getString(EXTRA_SELECTED_EPISODE);
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.i(TAG, "CharactersFragment onViewCreated");

        initListView(view);
        registerNotifications();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_exit) {
            finish();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager == null)
                return super.onOptionsItemSelected(item);

            fragmentManager.popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkForConnection();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "onStop called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        clearResources();

        unRegisterNotifications();
    }

    private void finish() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        activity.finish();
    }

    public void updateCharactersListView() {
        Log.i(TAG, "CharactersFragment updateCharactersListView");

        charactersListAdapter.notifyDataSetChanged();
    }

    private void initListView(View view) {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        showActionBar(getString(R.string.characters_view_title).toUpperCase(), true);

        progressBar = view.findViewById(R.id.progress_bar);
        setProgressBarVisible(true);

        charactersListView = view.findViewById(R.id.characters_list_view);
        clearResources();
        unRegisterNotifications();
        charactersListAdapter = new CharactersListAdapter(activity);
        charactersListView.setAdapter(charactersListAdapter);

        //Set Empty Text
        TextView emptyText = view.findViewById(android.R.id.empty);
        emptyText.setText(Html.fromHtml(getString(R.string.characters_view_empty_text, selectedEpisode)));
        charactersListView.setEmptyView(emptyText);

        charactersListView.setOnItemClickListener(listClickListener);
    }

    private void checkForConnection() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        if (Utils.checkNetworkConnectivity(activity)) {
            Log.i(TAG, "Internet Connection Available");
            for (String character: charactersList) {
                startService(character, JsonService.ACTION_CHARACTERS_RESP);
            }
        }
        else{
            Log.i(TAG, "CharactersFragment offline");
            createAlertDialog(activity);
        }
    }

    private void registerNotifications() {
        responseReceiver = new ResponseReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JsonService.ACTION_CHARACTERS_RESP);
        intentFilter.addAction(JsonService.ACTION_IMAGE_RESP);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        activity.registerReceiver(responseReceiver, intentFilter);
    }

    private void unRegisterNotifications() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        if (responseReceiver!= null) {
            activity.unregisterReceiver(responseReceiver);
        }
        setProgressBarVisible(false);
    }

    private void startService(String requestURL, String actionType) {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        if (Utils.isEmpty(requestURL)){
            showErrorMessage(getString(R.string.no_characters_error));
            return;
        }

        Intent serviceIntent = new Intent(activity, JsonService.class);
        serviceIntent.putExtra(JsonService.PARAM_REQUEST, requestURL);
        serviceIntent.putExtra(JsonService.PARAM_ACTION_TYPE, actionType);
        activity.startService(serviceIntent);
    }

    private void startService(String requestURL, String actionType, String characterId) {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        if (Utils.isEmpty(requestURL)){
            showErrorMessage(getString(R.string.no_characters_error));
            return;
        }

        Intent serviceIntent = new Intent(activity, JsonService.class);
        serviceIntent.putExtra(JsonService.PARAM_REQUEST, requestURL);
        serviceIntent.putExtra(JsonService.PARAM_CHARACTER_ID, characterId);
        serviceIntent.putExtra(JsonService.PARAM_ACTION_TYPE, actionType);
        activity.startService(serviceIntent);
    }

    private void clearResources() {
        Log.i(TAG, "CharactersFragment clearResources");

        clearAlertDialog();
        if (charactersListAdapter != null) {
            charactersListAdapter.clearData();
        }
    }

    private boolean isActive() {
        return isAdded() && !isStateSaved();
    }

    private void showErrorMessage(String message) {
        if (getActivity() == null || !isActive())
            return;

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        finish();
    }

    private void setProgressBarVisible(boolean enable) {
        progressBar.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    /* user has selected one of the episode */
    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final CharacterDetails characterDetails = charactersListAdapter.getCharacterDetails(position);
            if (characterDetails == null) {
                showErrorMessage(getString(R.string.no_data_error));
                return;
            }

            showCharactersFragment(characterDetails, charactersListAdapter.getCharacterImage(characterDetails.getId()));
        }
    };

    private void showCharactersFragment(CharacterDetails characterDetails, Bitmap characterImage) {
        if (getActivity() == null || !isActive())
            return;

        CharacterDisplayFragment characterDisplayFragment = new CharacterDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CHARACTER_DETAILS, characterDetails);
        bundle.putParcelable(EXTRA_CHARACTER_IMAGE, characterImage);
        characterDisplayFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, characterDisplayFragment, CharacterDisplayFragment.TAG)
                .addToBackStack(CharactersFragment.TAG)
                .commit();
    }

    public class ResponseReceiver extends BroadcastReceiver {
        JsonParser dataParser = new JsonParser();

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;

            if (JsonService.ACTION_CHARACTERS_RESP.equals(action)) {
                parseCharactersResponse(intent);
            } else if (JsonService.ACTION_IMAGE_RESP.equals(action)) {
                parseImageResponse(intent);
            }

            if(charactersList.size() == charactersListAdapter.getCharacterDetailsListSize()) {
                charactersListAdapter.sortDetails();
                updateCharactersListView();
            }
        }

        private void parseCharactersResponse(Intent intent) {
            if (intent == null)
                return;

            String jsonResult = intent.getStringExtra(JsonService.PARAM_RESULT);

            if(!Utils.isEmpty(jsonResult)) {
                //Parse result and fetch episode details
                LinkedHashMap<String, String> characterDetailsMap = getCharacterDetails(jsonResult);
                if (characterDetailsMap == null || characterDetailsMap.size() == 0) {
                    showErrorMessage(getString(R.string.no_episodes_error));
                    unRegisterNotifications();
                    return;
                }

                String id = characterDetailsMap.get("id");
                String name = characterDetailsMap.get("name");
                String status = characterDetailsMap.get("status");
                String species = characterDetailsMap.get("species");
                String imageURL = characterDetailsMap.get("image");
                String type = characterDetailsMap.get("type");
                String gender = characterDetailsMap.get("gender");
                String origin = characterDetailsMap.get("origin");
                String location = characterDetailsMap.get("location");
                String created = characterDetailsMap.get("created");
                CharacterDetails characterDetails = new CharacterDetails(id, name, status, species, type,
                        gender, origin, location, created);

                charactersListAdapter.addCharacterDetails(characterDetails);
                updateCharactersListView();

                //Fetch character image
                if (!Utils.isEmpty(imageURL)) {
                    startService(imageURL, JsonService.ACTION_IMAGE_RESP, id);
                    return;
                }
            }
            else {
                showErrorMessage(getString(R.string.no_data_error));
            }

            unRegisterNotifications();
        }

        private void parseImageResponse(Intent intent) {
            if (intent == null)
                return;

            String characterId = null;
            Bitmap imageBitmap = intent.getParcelableExtra(JsonService.PARAM_RESULT);

            if (imageBitmap == null) {
                showErrorMessage(getString(R.string.unexpected_error));
                return;
            }

            if (intent.hasExtra(JsonService.PARAM_CHARACTER_ID)) {
                characterId = intent.getStringExtra(JsonService.PARAM_CHARACTER_ID);
            }

            if (characterId == null) {
                showErrorMessage(getString(R.string.unexpected_error));
                return;
            }

            CharacterImage characterImageObj = new CharacterImage(characterId, imageBitmap);
            charactersListAdapter.addCharacterImage(characterImageObj);
            updateCharactersListView();
        }

        @Nullable
        private LinkedHashMap<String, String> getCharacterDetails(String jsonResult) {
            LinkedHashMap<String, String> charactersDetails;

            charactersDetails = dataParser.getCharacterDetails(jsonResult);

            return charactersDetails;
        }
    }
}
