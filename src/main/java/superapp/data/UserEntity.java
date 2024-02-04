package superapp.data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    private String id;
    private String username;
    private String avatar;
    private UserRole role;
    private String email;
    private Date createdAt;

    private Set<SuperAppObjectEntity> favorites;
    private Set<SuperAppObjectEntity> wishlist;

    public UserEntity() {
    }

    public UserEntity(String id, String username, String avatar, UserRole role, String email, Date createdAt) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.role = role;
        this.email = email;
        this.createdAt = createdAt;
        this.favorites = new HashSet<>();
        this.wishlist = new HashSet<>();
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String firstName) {
        this.username = firstName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Enumerated(EnumType.STRING)
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade =  CascadeType.PERSIST)
    public Set<SuperAppObjectEntity> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<SuperAppObjectEntity> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(SuperAppObjectEntity movie){
        favorites.add(movie);
        movie.addUser(this);
    }

    public void deleteAllLFavorites(){
        for (SuperAppObjectEntity fav: favorites) {
            fav.getUsers().remove(this);
        }
        favorites.clear();
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade =  CascadeType.PERSIST)
    public Set<SuperAppObjectEntity> getWishlist() {
        return wishlist;
    }

    public void setWishlist(Set<SuperAppObjectEntity> wishlist) {
        this.wishlist = wishlist;
    }

    public void addToWishlist(SuperAppObjectEntity movie) {
       this.wishlist.add(movie);
        movie.addUser(this);
    }

    public void deleteAllLWishlist(){
        for (SuperAppObjectEntity movie: wishlist) {
            System.out.println("DEBUG: delete " + this.getId() + "from wishlist");
            movie.getUsers().remove(this);
        }
        wishlist.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        return id.equals(((UserEntity) obj).getId());
    }

}
