package superapp.myMovies.models;

import java.util.List;

public class MediaGroup {

    int page ;
    int total_pages ;
    int total_results ;
    List<Movie> results;

    public MediaGroup(){}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public MediaGroup(int page, int total_pages, int total_results, List<Movie> results) {
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.results = results;
    }

    @Override
    public String toString() {
        return "MediaGroup{" +
                "page=" + page +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                ", results=" + results +
                '}';
    }
}

