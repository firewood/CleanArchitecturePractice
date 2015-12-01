package info.competitiveprogramming.getfollowers.data.api;

import retrofit.RestAdapter;

public class ApiFactory {
    public static GithubApi createGithubApi() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(
                "https://api.github.com/")
                .setLogLevel(RestAdapter.LogLevel.FULL);
        return builder.build().create(GithubApi.class);
    }
}
