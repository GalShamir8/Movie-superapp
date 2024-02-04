package superapp.common;

public interface Constants {
     String BASE_URL = "/superapp";
     String DOMAIN_URL = "/2023a.ohad.saada";
     String EMAIL_REGEX = "^\\w+[\\w._%-+#]+@[\\w.-]+\\.[\\w]{2,6}$";
     String USER_NOT_FOUND_MSG = "User not found";
     String USER_NOT_AUTHORISED = "User not authorised";
     String SUPER_APP_OBJECT_NOT_FOUND_MSG = "Object not found";
     String INVALID_EMAIL_MSG = "Invalid email";
     String DEFAULT_PER_PAGE = "10";
     String DEFAULT_PAGE = "0";
     String DEPRECATED_MESSAGE = "method is deprecated";
     String INVALID_CREATION_ENUM = "Invalid creatin enum values must be one of LAST_MINUTE, LAST_HOUR, LAST_DAY";
     String ADD_TO_FAVORITE = "addToFavorite";
     String GET_FAVORITES = "getAllFavorite";
     String REMOVE_FAVORITE = "removeFromFavorite";
     String ADD_TO_WISHLIST = "addToWishlist";
     String GET_WISHLISTS = "getAllWishlist";
     String REMOVE_WISHLIST = "removeFromWishlist";
}
