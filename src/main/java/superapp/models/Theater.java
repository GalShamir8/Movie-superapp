package superapp.models;

import java.util.Set;

class Theater {
    private String name;
    private String region;
    private Set<String> movies;

    public Theater(String name, String region, Set<String> movies) {
        this.name = name;
        this.region = region;
        this.movies = movies;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public Set<String> getMovies() {
        return movies;
    }
}
