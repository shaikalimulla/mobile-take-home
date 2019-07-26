package co.ali.rickandmortyapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import co.ali.rickandmortyapp.common.BaseFragment;
import co.ali.rickandmortyapp.data.CharacterDetails;
import co.ali.rickandmortyapp.modal.CharacterDetailsListAdapter;

public class CharacterDisplayFragment extends BaseFragment {
    public static String TAG = CharacterDisplayFragment.class.getSimpleName();
    public static String EXTRA_CHARACTER_DETAILS = "characters_list";
    public static String EXTRA_CHARACTER_IMAGE = "selected_character";

    private CharacterDetails characterDetails;
    private ProgressBar progressBar;
    private ListView charactersDetailsListView;
    private CharacterDetailsListAdapter characterDetailsListAdapter = null;
    private Bitmap characterImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CharactersFragment onCreate");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.i(TAG, "CharactersFragment onCreateView");
        View view = inflater.inflate(R.layout.character_details_list, viewGroup, false);

        if (getArguments()!= null) {
            characterDetails = getArguments().getParcelable(EXTRA_CHARACTER_DETAILS);
            characterImage = getArguments().getParcelable(EXTRA_CHARACTER_IMAGE);
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.i(TAG, "CharactersFragment onViewCreated");

        initListView(view);
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

        loadData();
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
        setProgressBarVisible(false);
    }

    private void finish() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        activity.finish();
    }

    public void updateCharactersListView() {
        Log.i(TAG, "CharactersFragment updateCharactersListView");

        characterDetailsListAdapter.notifyDataSetChanged();
        setProgressBarVisible(false);
    }

    private void initListView(View view) {
        showActionBar(getString(R.string.character_detail_view_title).toUpperCase(), true);

        progressBar = view.findViewById(R.id.progress_bar);
        setProgressBarVisible(true);

        charactersDetailsListView = view.findViewById(R.id.character_details_list_view);
        loadData();

        //Set Empty Text
        TextView emptyText = view.findViewById(android.R.id.empty);
        emptyText.setText(getString(R.string.characters_details_view_empty_text));
        charactersDetailsListView.setEmptyView(emptyText);

        ImageView imageView = view.findViewById(R.id.character_image);
        imageView.setImageBitmap(characterImage);
    }

    private void loadData() {
        Activity activity = getActivity();
        if (activity == null || !isActive())
            return;

        characterDetailsListAdapter = new CharacterDetailsListAdapter(activity);
        characterDetailsListAdapter.setAdapterData(characterDetails.getKeyList(), characterDetails.getValueList(characterDetails));
        charactersDetailsListView.setAdapter(characterDetailsListAdapter);
        updateCharactersListView();
    }

    private void clearResources() {
        Log.i(TAG, "CharactersFragment clearResources");

        if (characterDetailsListAdapter != null) {
            characterDetailsListAdapter.clearData();
        }
    }

    private boolean isActive() {
        return isAdded() && !isStateSaved();
    }

    private void setProgressBarVisible(boolean enable) {
        progressBar.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}
