package info.competitiveprogramming.getfollowers.domain.usecase;

import java.util.Collection;

import info.competitiveprogramming.getfollowers.data.repository.UserRepository;
import info.competitiveprogramming.getfollowers.domain.executor.PostExecutionThread;
import info.competitiveprogramming.getfollowers.domain.model.User;

public class CheckUserUseCase extends UseCase<User> implements UserRepository.UserRepositoryCallback {
    public interface CheckUserUseCaseCallback {
        void onChecked(User user);
        void onError();
    }

    private static CheckUserUseCase sUseCase;
    private final UserRepository mUserRepository;
    private PostExecutionThread mPostExecutionThread;
    private CheckUserUseCase.CheckUserUseCaseCallback mCallback;
    private User mUser;

    public static CheckUserUseCase getUseCase(UserRepository userRepository, PostExecutionThread postExecutionThread) {
        if (sUseCase == null) {
            sUseCase = new CheckUserUseCase(userRepository, postExecutionThread);
        }
        return sUseCase;
    }

    public CheckUserUseCase(UserRepository userRepository, PostExecutionThread postExecutionThread) {
        mUserRepository = userRepository;
        mPostExecutionThread = postExecutionThread;
    }

    public void execute(User user, CheckUserUseCase.CheckUserUseCaseCallback callback) {
        mCallback = callback;
        mUser = user;
        this.start(user);
    }

    @Override
    protected void call(User user) {
        mUserRepository.getUser(user.login,this);
    }

    @Override
    public void onUserListLoaded(Collection<User> usersCollection) {

    }

    @Override
    public void onUserLoaded(final User user) {

        final boolean isUserChecked = user != null;
        if (!isUserChecked) {
            //User is not cached in memory
            mUserRepository.putUser(mUser);
            return;
        }
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onChecked(user);
            }
        });
    }

    @Override
    public void onError() {

    }
}
