package co.ali.rickandmortyapp.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import co.ali.rickandmortyapp.MainActivity;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("onCreate %s", getClass().getSimpleName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.v("onViewCreated %s", getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.v("onStart %s", getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.v("onStop %s", getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.v("onDestroyView %s", getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void showActionBar(String title, boolean enableBackButton) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null)
            return;

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title.toUpperCase());
            actionBar.setDisplayHomeAsUpEnabled(enableBackButton);
        }
    }
}