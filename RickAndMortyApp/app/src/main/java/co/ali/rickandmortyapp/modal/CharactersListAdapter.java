package co.ali.rickandmortyapp.modal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import co.ali.rickandmortyapp.R;
import co.ali.rickandmortyapp.data.CharacterDetails;
import co.ali.rickandmortyapp.data.CharacterImage;

public class CharactersListAdapter extends BaseAdapter {

    private ArrayList<CharacterDetails> characterDetailsList;
    private LinkedHashMap<String, Bitmap> charactersMap;
    private LayoutInflater inflater;

    public CharactersListAdapter(Activity parent) {
        super();
        characterDetailsList  = new ArrayList<>();
        charactersMap = new LinkedHashMap<>();
        inflater = parent.getLayoutInflater();
    }

    public void setAdapterData(ArrayList<CharacterDetails> characterDetailsList) {
        this.characterDetailsList = characterDetailsList;
    }

    public void addCharacterDetails(CharacterDetails characterDetails) {
        if (isCharacterDetailPresent(characterDetails)) {
            return;
        }
        characterDetailsList.add(characterDetails);
    }

    public boolean isCharacterDetailPresent(CharacterDetails characterDetails){
        return characterDetailsList.contains(characterDetails);
    }

    public void addCharacterImage(CharacterImage characterImage) {
        if (isCharacterImagePresent(characterImage)) {
            return;
        }

        charactersMap.put(characterImage.getId(), characterImage.getImage());
    }

    public boolean isCharacterImagePresent(CharacterImage characterImage){
        return charactersMap.containsKey(characterImage.getId());
    }

    public CharacterDetails getCharacterDetails(int index) {
        return characterDetailsList.get(index);
    }

    public int getCharacterDetailsListSize() {
        return characterDetailsList.size();
    }

    public void sortDetails() {
        Collections.sort(characterDetailsList);
    }

    public Bitmap getCharacterImage(String key) {
        return charactersMap.get(key);
    }

    public void clearData() {
        if (characterDetailsList == null || charactersMap == null)
            return;

        characterDetailsList.clear();
        charactersMap.clear();
    }

    @Override
    public int getCount() {
        return characterDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return getCharacterDetails(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FieldReferences fields;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.characters_list_row, null);
            fields = new FieldReferences();
            fields.status =  convertView.findViewById(R.id.status);
            fields.imageView = convertView.findViewById(R.id.character_image);
            convertView.setTag(fields);
        } else {
            fields = (FieldReferences) convertView.getTag();
        }

        // set proper values into the view
        CharacterDetails characterDetails = characterDetailsList.get(position);
        fields.status.setText(characterDetails.getStatus());
        fields.imageView.setImageBitmap(getCharacterImage(characterDetails.getId()));

        return convertView;
    }

    private class FieldReferences {
        TextView status;
        ImageView imageView;
    }
}
