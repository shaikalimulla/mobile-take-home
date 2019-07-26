package co.ali.rickandmortyapp.modal;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import co.ali.rickandmortyapp.R;
import co.ali.rickandmortyapp.data.Characters;
import co.ali.rickandmortyapp.data.Episode;

public class EpisodeListAdapter extends BaseAdapter {

    private ArrayList<Episode> episodeList;
    private LinkedHashMap<String, List<String>> charactersMap;
    private LayoutInflater inflater;

    public EpisodeListAdapter(Activity parent) {
        super();

        episodeList  = new ArrayList<>();
        charactersMap = new LinkedHashMap<>();
        inflater = parent.getLayoutInflater();
    }

    public void addEpisode(Episode episode) {
        if (isEpisodePresent(episode)) {
            return;
        }
        episodeList.add(episode);
    }

    public void addCharacters(Characters characters) {
        if (isCharacterPresent(characters)) {
            return;
        }

        charactersMap.put(characters.getId(), characters.getCharacters());
    }

    public boolean isEpisodePresent(Episode episode){
        return episodeList.contains(episode);
    }

    public boolean isCharacterPresent(Characters characters){
        return charactersMap.containsKey(characters.getId());
    }

    public Episode getEpisode(int index) {
        return episodeList.get(index);
    }

    public List<String> getCharacters(String key) {
        return charactersMap.get(key);
    }

    public void clearData() {
        episodeList.clear();
        charactersMap.clear();
    }

    @Override
    public int getCount() {
        return episodeList.size();
    }

    @Override
    public Object getItem(int position) {
        return getEpisode(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FieldReferences fields;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.episode_list_row, null);
            fields = new FieldReferences();
            fields.name =  convertView.findViewById(R.id.name);
            fields.airDate = convertView.findViewById(R.id.air_date);
            fields.episode = convertView.findViewById(R.id.episode);
            convertView.setTag(fields);
        } else {
            fields = (FieldReferences) convertView.getTag();
        }

        // set proper values into the view
        Episode episodeInfo = episodeList.get(position);
        fields.name.setText(episodeInfo.getName());
        fields.airDate.setText(episodeInfo.getAirDate());
        fields.episode.setText(episodeInfo.getEpisode());

        return convertView;
    }

    private class FieldReferences {
        TextView name;
        TextView airDate;
        TextView episode;
    }
}
