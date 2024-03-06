package superapp.models;

import java.util.*;

public class TheaterData {
    // Define a map to store theaters by region
    private Map<String, Set<Theater>> theatersByRegion;

    public TheaterData() {
        theatersByRegion = new HashMap<>();
    }

    // Method to add a theater to the data
    public void addTheater(Theater theater) {
        theatersByRegion.computeIfAbsent(theater.getRegion(), k -> new HashSet<>()).add(theater);
    }

    // Method to find theaters displaying a movie in a given region
    public Set<Theater> findTheatersByMovieAndRegion(String movieId, String region) {
        Set<Theater> theaters = theatersByRegion.getOrDefault(region, Collections.emptySet());
        Set<Theater> theatersWithMovie = new HashSet<>();
        for (Theater theater : theaters) {
            if (theater.getMovies().contains(movieId)) {
                theatersWithMovie.add(theater);
            }
        }
        return theatersWithMovie;
    }

    // Mock method to generate some sample data
    public void generateSampleData() {
        Theater theater1 = new Theater("Theater 1", "Region A", Set.of("Movie1", "Movie2", "Movie3"));
        Theater theater2 = new Theater("Theater 2", "Region A", Set.of("Movie1", "Movie4", "Movie5"));
        Theater theater3 = new Theater("Theater 3", "Region B", Set.of("Movie2", "Movie3", "Movie6"));

        addTheater(theater1);
        addTheater(theater2);
        addTheater(theater3);
    }
}
