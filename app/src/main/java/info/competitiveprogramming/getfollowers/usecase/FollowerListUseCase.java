package info.competitiveprogramming.getfollowers.usecase;

import java.util.Collection;

import info.competitiveprogramming.getfollowers.executor.PostExecutionThread;
import info.competitiveprogramming.getfollowers.model.User;
import info.competitiveprogramming.getfollowers.repository.UserRepository;
import info.competitiveprogramming.getfollowers.utility.StringUtil;

public class FollowerListUseCase extends UseCase<String> implements UserRepository.UserRepositoryCallback {
    public interface FollowerListUseCaseCallback {
        void onFollowerListLoaded(Collection<User> usersCollection);
        void onError();
    }

    private static FollowerListUseCase sUseCase;
    private final UserRepository mUserRepository;
    private PostExecutionThread mPostExecutionThread;
    private FollowerListUseCaseCallback mCallback;

    public static FollowerListUseCase getUseCase(UserRepository userRepository, PostExecutionThread postExecutionThread) {
        if (sUseCase == null) {
            sUseCase = new FollowerListUseCase(userRepository, postExecutionThread);
        }
        return sUseCase;
    }

    public FollowerListUseCase(UserRepository userRepository, PostExecutionThread postExecutionThread) {
        mUserRepository = userRepository;
        mPostExecutionThread = postExecutionThread;
    }

    public void execute(String user, FollowerListUseCaseCallback callback) {
        mCallback = callback;
        this.start(user);
    }

    @Override
    protected void call(String user) {
        //validation
        if (validate(user)) {
            //access repository
            mUserRepository.getFollowers(user, this);
        }
    }

    public void setCallback(FollowerListUseCaseCallback callback) {
        mCallback = callback;
    }

    public void removeCallback() {
        mCallback = null;
    }

    private boolean validate(String user) {
        return !StringUtil.isNullOrEmpty(user);
    }

    @Override
    public void onUserListLoaded(final Collection<User> usersCollection) {
        //return to UIthread
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onFollowerListLoaded(usersCollection);
                }
            }
        });
    }

    @Override
    public void onUserLoaded(User user) {

    }

    @Override
    public void onError() {
        //return to UIthread
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }
}
