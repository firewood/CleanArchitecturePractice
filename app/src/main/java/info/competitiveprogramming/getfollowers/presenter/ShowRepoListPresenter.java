package info.competitiveprogramming.getfollowers.presenter;

import java.util.Collection;

import info.competitiveprogramming.getfollowers.model.Repos;
import info.competitiveprogramming.getfollowers.usecase.ReposUseCase;

public class ShowRepoListPresenter extends Presenter implements ReposUseCase.ReposUserCaseCallback {

    private ReposUseCase mReposUseCase;
    private ShowReposView mView;

    public ShowRepoListPresenter(ReposUseCase reposUseCase) {
        mReposUseCase = reposUseCase;
    }

    public void setShowReposView(ShowReposView view) {
        mView = view;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void resume() {
        mReposUseCase.setCallback(this);
    }

    @Override
    public void pause() {
        mReposUseCase.removeCallback();
    }

    @Override
    public void destroy() {

    }

    public void getRepos(String user) {
        mReposUseCase.execute(user, this);
    }

    @Override
    public void onReposListLoaded(Collection<Repos> reposCollection) {
        mView.showResult(reposCollection);
    }

    @Override
    public void onError() {

    }

    public interface ShowReposView {
        void showResult(Collection<Repos> reposes);
    }
}
