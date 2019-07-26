package co.ali.rickandmortyapp.data;

import android.graphics.Bitmap;

public class CharacterImage {
    String id;
    Bitmap image;

    public CharacterImage(String id, Bitmap image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }
}
