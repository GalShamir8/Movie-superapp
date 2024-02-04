package superapp.myMovies.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class UserMediaLists {
    List<String> wishList;
    List<String> favorite;
    List<String> watched;

    private ObjectMapper jacksonMapper;

    public UserMediaLists() {
        wishList = new ArrayList<>();
        favorite = new ArrayList<>();
        watched = new ArrayList<>();
        jacksonMapper = new ObjectMapper();
    }

    public UserMediaLists(List<String> wishList, List<String> favorite, List<String> watched) {
        jacksonMapper = new ObjectMapper();
        this.wishList = wishList;
        this.favorite = favorite;
        this.watched = watched;
    }


    public List<String> addToWishList(String id) {
        wishList.add(id);
        return wishList;
    }
    public List<String> getWishList() {
        return wishList;
    }

    public void setWishList(List<String> wishList) {
        this.wishList = wishList;
    }

    public List<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<String> favorite) {
        this.favorite = favorite;
    }

    public List<String> getWatched() {
        return watched;
    }

    public void setWatched(List<String> watched) {
        this.watched = watched;
    }

    public String toJson() throws JsonProcessingException {
       return jacksonMapper.writeValueAsString(this);
    }

    public List<String>  RemoveFromWishList(String objectId) {
        wishList.remove(objectId);
        return wishList;
    }

    public List<String> addToFavorite(String objectId) {
        favorite.add(objectId);
        return favorite;
    }

    public List<String>  RemoveFromFavorite(String objectId) {
        favorite.remove(objectId);
        return favorite;
    }

    public List<String> addToWatched(String objectId) {
        watched.add(objectId);
        return watched;
    }

    public List<String>  RemoveFromWatched(String objectId) {
        watched.remove(objectId);
        return watched;
    }
}
