# Movie super app
One Movie app for all your needs

- Rates

- Wishlist

- Favorites

- Watched

Our application streaming live data on movies.

Using [TMDB](https://www.themoviedb.org) Database and [open API](https://www.themoviedb.org/documentation/api)
in order to give our users the best experience.

We display rate based on our users rate and [IMDB](https://www.imdb.com).

We provide easy API to add movie to Wishlist as well as to Favorites

## Getting Started
### Prerequisite
1. Docker desktop installed on your local machine link to [download](https://www.docker.com/products/docker-desktop/)
2. Setting up `.env` file - use `.env` file for setting all required keys
3. Open terminal on the app root directory
4. Ran `docker-compose up --build`
5. You ready to go!!

### Unit Testing
1. Before we perform unit testing, We need to switch the DB configuration from postgres to h2
2. In Resource file remove '#' from the 2 h2 lines and add '#' before the postgresql line
3. In the build.gradle remove the '//' comment from the runtimeOnly 'com.h2...' and add comment to the runetime of postgresql above
4. Now we are ready for unit testing!
P.S dont forget to reverse those actions if you want to run the application as production!!!

### Client 
Clone the project from GitHub 

https://github.com/ohadsa/integrativitClient

Inside di/NetworkModule add the base url of your port. ( right now it’s http://10.0.2.2:8085/ )

Run the app and add new user