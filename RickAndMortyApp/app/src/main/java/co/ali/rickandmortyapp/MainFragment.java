package co.ali.rickandmortyapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.ali.rickandmortyapp.common.BaseFragment;
import co.ali.rickandmortyapp.data.Characters;
import co.ali.rickandmortyapp.data.Episode;
import co.ali.rickandmortyapp.modal.EpisodeListAdapter;
import co.ali.rickandmortyapp.service.JsonParser;
import co.ali.rickandmortyapp.service.JsonService;
import co.ali.rickandmortyapp.util.Utils;

import static co.ali.rickandmortyapp.CharactersFragment.EXTRA_CHARACTERS_LIST;
import static co.ali.rickandmortyapp.CharactersFragment.EXTRA_SELECTED_EPISODE;
import static co.ali.rickandmortyapp.util.Utils.clearAlertDialog;
import static co.ali.rickandmortyapp.util.Utils.createAlertDialog;

public class MainFragment extends BaseFragment {
    private static String TAG = MainFragment.class.getSimpleName();

    private ResponseReceiver responseReceiver;

    private ProgressBar progressBar;
    private ListView dataListView;
    private EpisodeListAdapter episodeListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainFragment onCreate");

        registerNotifications();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.i(TAG, "MainFragment onCreateView");
        View view = inflater.inflate(R.layout.episode_list, viewGroup, false);

        return view;
    }
    

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.i(TAG, "MainFragment onViewCreated");

        initListView(view);
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

    public void updateEpisodeListView() {
        Log.i(TAG, "MainFragment updateEpisodeListView");

        episodeListAdapter.notifyDataSetChanged();
    }

    private void initListView(View view) {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        showActionBar(getString(R.string.episode_view_title).toUpperCase(), false);

        progressBar = view.findViewById(R.id.progress_bar);
        setProgressBarVisible(true);

        dataListView = view.findViewById(R.id.episode_list_view);
        episodeListAdapter = new EpisodeListAdapter(activity);
        episodeListAdapter.clearData();
        dataListView.setAdapter(episodeListAdapter);

        //Set Empty Text
        TextView emptyText = view.findViewById(android.R.id.empty);
        emptyText.setText(getString(R.string.episodes_view_empty_text));
        dataListView.setEmptyView(emptyText);

        dataListView.setOnItemClickListener(listClickListener);
    }

    private void checkForConnection() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        if (Utils.checkNetworkConnectivity(activity)) {
            Log.i(TAG, "Internet Connection Available");
            startService(null);
        }
        else{
            Log.i(TAG, "MainFragment offline");
            createAlertDialog(activity);
        }
    }

    private void registerNotifications() {
        responseReceiver = new ResponseReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JsonService.ACTION_EPISODES_RESP);
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

    private void startService(String requestURL) {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        if (Utils.isEmpty(requestURL)){
            requestURL = JsonService.BASE_URL;
        }

        Intent serviceIntent = new Intent(activity, JsonService.class);
        serviceIntent.putExtra(JsonService.PARAM_REQUEST, requestURL);
        serviceIntent.putExtra(JsonService.PARAM_ACTION_TYPE, JsonService.ACTION_EPISODES_RESP);
        activity.startService(serviceIntent);
    }

    private void clearResources() {
        Log.i(TAG, "MainFragment clearResources");
        
        clearAlertDialog();
        if (episodeListAdapter != null) {
            episodeListAdapter.clearData();
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

    private void finish() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        activity.finish();
    }

    private void setProgressBarVisible(boolean enable) {
        progressBar.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    /* user has selected one of the episode */
    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final Episode episode = episodeListAdapter.getEpisode(position);
            if (episode == null) {
                showErrorMessage(getString(R.string.no_data_error));
                return;
            }

            List<String> charactersList = episodeListAdapter.getCharacters(episode.getId());

            showCharactersFragment(episode.getEpisode(), charactersList);
        }
    };

    private void showCharactersFragment(String selectedEpisode, List<String> charactersList) {
        if (getActivity() == null || !isActive())
            return;

        CharactersFragment charactersFragment = new CharactersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SELECTED_EPISODE, selectedEpisode);
        bundle.putStringArrayList(EXTRA_CHARACTERS_LIST, (ArrayList<String>) charactersList);
        charactersFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, charactersFragment, CharactersFragment.TAG)
                .addToBackStack(MainFragment.TAG)
                .commit();
    }

    public class ResponseReceiver extends BroadcastReceiver {
        JsonParser dataParser = new JsonParser();

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;

            if (JsonService.ACTION_EPISODES_RESP.equals(action)) {
                parseEpisodeResponse(intent);
            }
        }

        private void parseEpisodeResponse(Intent intent) {
            if (intent == null)
                return;

            String jsonResult = intent.getStringExtra(JsonService.PARAM_RESULT);

            if(!Utils.isEmpty(jsonResult)) {
                //Parse result and fetch episode details
                List<LinkedHashMap<String, String>> episodesList = getEpisodes(jsonResult);
                if (episodesList == null || episodesList.size() == 0) {
                    showErrorMessage(getString(R.string.no_episodes_error));
                    unRegisterNotifications();
                    return;
                }

                for (LinkedHashMap<String, String> currEpisode : episodesList) {
                    String id = currEpisode.get("id");
                    String name = currEpisode.get("name");
                    String airDate = currEpisode.get("air_date");
                    String episode = currEpisode.get("episode");
                    Episode episodeObj = new Episode(id, name, airDate, episode);
                    episodeListAdapter.addEpisode(episodeObj);
                    updateEpisodeListView();
                }

                //Parse result and fetch characters details
                LinkedHashMap<String, List<String>> charactersMap = getCharacters(jsonResult);
                if (charactersMap == null || charactersMap.size() == 0) {
                    showErrorMessage(getString(R.string.no_characters_error));
                    unRegisterNotifications();
                    return;
                }
                for (Map.Entry<String, List<String>> characterEntry : charactersMap.entrySet()) {
                    Characters character = new Characters(characterEntry.getKey(), characterEntry.getValue());
                    episodeListAdapter.addCharacters(character);
                }

                //Fetch next page details if exist
                String nextPageURL = getNextPageURL(jsonResult);
                if (!Utils.isEmpty(nextPageURL)) {
                    startService(nextPageURL);
                    return;
                }
            }
            else {
                showErrorMessage(getString(R.string.no_data_error));
            }

            unRegisterNotifications();
        }

        private String getNextPageURL(String jsonResult) {
            LinkedHashMap<String, String> pageInfoMap = dataParser.getPageInfo(jsonResult);
            String episodeCount = pageInfoMap.get("count");
            String pagesCount = pageInfoMap.get("pages");
            String next = pageInfoMap.get("next");

            return next;
        }

        @Nullable
        private List<LinkedHashMap<String, String>> getEpisodes(String jsonResult) {
            List<LinkedHashMap<String, String>> episodesList;

            episodesList = dataParser.getEpisodeInfo(jsonResult);

            return episodesList;
        }

        @Nullable
        private LinkedHashMap<String, List<String>> getCharacters(String jsonResult) {
            LinkedHashMap<String, List<String>> charactersMap;

            charactersMap = dataParser.getCharacterInfo(jsonResult);

            return charactersMap;
        }
    }
}

