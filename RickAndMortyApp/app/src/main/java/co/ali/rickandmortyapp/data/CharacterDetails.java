package co.ali.rickandmortyapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CharacterDetails implements Parcelable, Comparable<CharacterDetails> {
    String id, name, status, species, type, gender, origin, location, created;

    public CharacterDetails(String id, String name, String status, String species,
                            String type, String gender, String origin, String location, String created) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.origin = origin;
        this.location = location;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }

    public String getOrigin() {
        return origin;
    }

    public String getLocation() {
        return location;
    }

    public String getCreated() {
        return created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<String> getKeyList() {
        ArrayList<String> keyList = new ArrayList<>();

        keyList.add("Id");
        keyList.add("Name");
        keyList.add("Status");
        keyList.add("Species");
        keyList.add("Type");
        keyList.add("Gender");
        keyList.add("Origin");
        keyList.add("Location");
        keyList.add("Created");

        return keyList;
    }

    public ArrayList<String> getValueList(final CharacterDetails characterDetails) {
        ArrayList<String> valueList = new ArrayList<>();

        valueList.add(characterDetails.getId());
        valueList.add(characterDetails.getName());
        valueList.add(characterDetails.getStatus());
        valueList.add(characterDetails.getSpecies());
        valueList.add(characterDetails.getType());
        valueList.add(characterDetails.getGender());
        valueList.add(characterDetails.getOrigin());
        valueList.add(characterDetails.getLocation());
        valueList.add(characterDetails.getCreated());

        return valueList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(species);
        dest.writeString(type);
        dest.writeString(gender);
        dest.writeString(origin);
        dest.writeString(location);
        dest.writeString(created);
    }

    public static final Creator<CharacterDetails> CREATOR = new Creator<CharacterDetails>()
    {
        @Override
        public CharacterDetails createFromParcel(Parcel source) {
            return new CharacterDetails(source.readString(), source.readString(), source.readString(), source.readString(),
                    source.readString(), source.readString(), source.readString(), source.readString(), source.readString());
        }

        @Override
        public CharacterDetails[] newArray(int size) {
            return new CharacterDetails[size];
        }
    };

    @Override
    public int compareTo(CharacterDetails o) {
        return this.getStatus().compareTo(o.getStatus());
    }
}
