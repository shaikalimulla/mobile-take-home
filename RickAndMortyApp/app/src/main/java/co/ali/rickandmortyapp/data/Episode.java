package co.ali.rickandmortyapp.data;

public class Episode {
    String id, name, airDate, episode;

    public Episode(String id, String name, String airDate, String episode) {
        this.id = id;
        this.name = name;
        this.airDate = airDate;
        this.episode = episode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAirDate() {
        return airDate;
    }

    public String getEpisode() {
        return episode;
    }
}
