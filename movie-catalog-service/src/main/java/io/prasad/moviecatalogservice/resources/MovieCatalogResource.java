package io.prasad.moviecatalogservice.resources;

import io.prasad.moviecatalogservice.models.CatalogItem;
import io.prasad.moviecatalogservice.models.Movie;
import io.prasad.moviecatalogservice.models.Rating;
import io.prasad.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        Rating[] userRating = restTemplate.getForObject("http://localhost:8084/ratingsdata/user/" + userId,
                Rating[].class);

        //Array rat = Arrays.asList(userRating);

        return Arrays.asList(userRating).stream().map(rating -> {
            // For Each movie ID, call movie info service and get details
            Movie movie = restTemplate.getForObject("http://localhost:8083/movies/" + rating.getMovieId(), Movie.class);
            // Put them all together
            return new CatalogItem(movie.getName(), "Action Movie", rating.getRating());
        }).collect(Collectors.toList());

    }
}
