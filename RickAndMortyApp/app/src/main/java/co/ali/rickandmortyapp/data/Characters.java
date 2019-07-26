package co.ali.rickandmortyapp.data;

import java.util.List;

public class Characters {
    String id;
    List<String> characters;

    public Characters(String id, List<String> characters) {
        this.id = id;
        this.characters = characters;
    }

    public String getId() {
        return id;
    }

    public List<String> getCharacters() {
        return characters;
    }
}
