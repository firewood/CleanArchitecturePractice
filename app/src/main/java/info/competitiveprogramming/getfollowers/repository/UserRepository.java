package info.competitiveprogramming.getfollowers.repository;

import java.util.Collection;
import java.util.List;

import info.competitiveprogramming.getfollowers.api.ApiFactory;
import info.competitiveprogramming.getfollowers.api.GithubApi;
import info.competitiveprogramming.getfollowers.cache.UserMemoryCache;
import info.competitiveprogramming.getfollowers.model.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserRepository {
    public interface UserRepositoryCallback {
        void onUserListLoaded(Collection<User> usersCollection);
        void onUserLoaded(User user);
        void onError();
    }

    private static UserRepository sUserRepository;
    private GithubApi mApi;

    public UserRepository() {
        mApi = ApiFactory.createGithubApi();
    }

    public static UserRepository getRepository() {
        if (sUserRepository == null) {
            sUserRepository = new UserRepository();
        }
        return sUserRepository;
    }

    public void getFollowers(String userId, final UserRepositoryCallback userRepositoryCallback) {
        mApi.listFollowersAsync(userId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                userRepositoryCallback.onUserListLoaded(users);
            }

            @Override
            public void failure(RetrofitError error) {
                userRepositoryCallback.onError();
            }
        });
    }

    public void getUser(String userId, UserRepositoryCallback userRepositoryCallback) {
        User user = UserMemoryCache.getInstance().getUser(userId);
        userRepositoryCallback.onUserLoaded(user);
    }

    public void putUser(User user) {
        UserMemoryCache.getInstance().put(user.login, user);
    }
}
