package info.competitiveprogramming.getfollowers.api;

import java.util.List;

import info.competitiveprogramming.getfollowers.model.Repos;
import info.competitiveprogramming.getfollowers.model.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GithubApi {
    @GET("/users/{user}/followers")
    List<User> listFollowers(@Path("user") String user);

    @GET("/users/{user}/followers")
    void listFollowersAsync(@Path("user") String user, Callback<List<User>> cb);

    @GET("/users/{user}/repos")
    void listReposAsync(@Path("user") String user, Callback<List<Repos>> cb);
}
