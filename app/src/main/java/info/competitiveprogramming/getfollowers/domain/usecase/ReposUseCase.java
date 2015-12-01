package info.competitiveprogramming.getfollowers.domain.usecase;

import java.util.Collection;

import info.competitiveprogramming.getfollowers.data.repository.ReposRepository;
import info.competitiveprogramming.getfollowers.domain.executor.PostExecutionThread;
import info.competitiveprogramming.getfollowers.domain.model.Repos;

public class ReposUseCase extends UseCase<String> implements ReposRepository.ReposListCallback {
    public interface ReposUserCaseCallback {
        void onReposListLoaded(Collection<Repos> reposCollection);
        void onError();
    }

    private static ReposUseCase sReposUseCase;
    private ReposRepository mReposRepository;
    private PostExecutionThread mThread;
    private ReposUseCase.ReposUserCaseCallback mCallback;

    public ReposUseCase(ReposRepository reposRepository, PostExecutionThread thread) {
        mReposRepository = reposRepository;
        mThread = thread;
    }

    public static ReposUseCase getUseCase(ReposRepository reposRepository, PostExecutionThread thread) {
        if (sReposUseCase == null) {
            sReposUseCase = new ReposUseCase(reposRepository, thread);
        }
        return sReposUseCase;
    }

    public void execute(String user, ReposUserCaseCallback callback) {
        mCallback = callback;
        this.start(user);
    }

    @Override
    protected void call(String params) {
        mReposRepository.getRepos(params,this);
    }

    public void setCallback(ReposUserCaseCallback callback) {
        mCallback = callback;
    }

    public void removeCallback() {
        mCallback = null;
    }

    @Override
    public void onReposListLoaded(final Collection<Repos> reposCollection) {
        mThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onReposListLoaded(reposCollection);
            }
        });

    }

    @Override
    public void onError() {

    }
}
