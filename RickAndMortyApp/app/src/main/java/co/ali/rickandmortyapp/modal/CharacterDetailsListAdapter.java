package co.ali.rickandmortyapp.modal;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.ali.rickandmortyapp.R;

public class CharacterDetailsListAdapter extends BaseAdapter {
    private ArrayList<String> keyList, valueList;
    private LayoutInflater inflater;

    public CharacterDetailsListAdapter(Activity parent) {
        super();
        inflater = parent.getLayoutInflater();

        keyList = new ArrayList<>();
        valueList = new ArrayList<>();
    }

    public void setAdapterData(ArrayList<String> keyList, ArrayList<String> valueList) {
        this.keyList = keyList;
        this.valueList = valueList;
    }

    public void clearData() {
        keyList.clear();
        valueList.clear();
    }

    @Override
    public int getCount() {
        return keyList.size();
    }

    @Override
    public Object getItem(int position) {
        return keyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FieldReferences fields;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.character_details_list_row, null);
            fields = new FieldReferences();
            fields.key =  convertView.findViewById(R.id.key);
            fields.value = convertView.findViewById(R.id.value);
            convertView.setTag(fields);
        } else {
            fields = (FieldReferences) convertView.getTag();
        }

        // set proper values into the view
        fields.key.setText(keyList.get(position));
        fields.value.setText(valueList.get(position));

        return convertView;
    }

    private class FieldReferences {
        TextView key;
        TextView value;
    }
}
