package info.competitiveprogramming.getfollowers.repository;

import java.util.Collection;
import java.util.List;

import info.competitiveprogramming.getfollowers.api.ApiFactory;
import info.competitiveprogramming.getfollowers.api.GithubApi;
import info.competitiveprogramming.getfollowers.model.Repos;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReposRepository {

    public interface ReposListCallback {
        void onReposListLoaded(Collection<Repos> reposCollection);
        void onError();
    }

    private static ReposRepository sReposRepository;
    private GithubApi mApi;

    public ReposRepository() {
        mApi = ApiFactory.createGithubApi();
    }

    public static ReposRepository getRepository() {
        if (sReposRepository == null) {
            sReposRepository = new ReposRepository();
        }
        return sReposRepository;
    }

    public void getRepos(String user, final ReposListCallback cb) {
        mApi.listReposAsync(user, new Callback<List<Repos>>() {
            @Override
            public void success(List<Repos> reposes, Response response) {
                cb.onReposListLoaded(reposes);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
