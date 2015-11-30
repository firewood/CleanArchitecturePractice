package info.competitiveprogramming.getfollowers.presenter;

import java.util.Collection;

import info.competitiveprogramming.getfollowers.model.User;
import info.competitiveprogramming.getfollowers.usecase.CheckUserUseCase;
import info.competitiveprogramming.getfollowers.usecase.FollowerListUseCase;

public class ShowUserListPresenter extends Presenter
        implements FollowerListUseCase.FollowerListUseCaseCallback, CheckUserUseCase.CheckUserUseCaseCallback {

    private FollowerListUseCase mFollowerListUseCase;
    private CheckUserUseCase mCheckUserUseCase;
    private ShowUserListView mShowUserListView;

    public ShowUserListPresenter(FollowerListUseCase followerListUseCase, CheckUserUseCase checkUserUseCase) {
        mFollowerListUseCase = followerListUseCase;
        mCheckUserUseCase = checkUserUseCase;
    }

    public void setShowUserListView(ShowUserListView view) {
        mShowUserListView = view;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void resume() {
        mFollowerListUseCase.setCallback(this);
    }

    @Override
    public void pause() {
        mFollowerListUseCase.removeCallback();
    }

    @Override
    public void destroy() {

    }

    public void getFollowerList(String user) {
        mShowUserListView.showLoading();
        mFollowerListUseCase.execute(user, this);
    }

    public void checkUser(User user) {
        mCheckUserUseCase.execute(user, this);
    }

    @Override
    public void onFollowerListLoaded(Collection<User> usersCollection) {
        mShowUserListView.hideLoading();
        mShowUserListView.hideNoResultCase();
        mShowUserListView.showResult(usersCollection);
    }

    @Override
    public void onChecked(User user) {
        mShowUserListView.showToast(user.login);
    }

    @Override
    public void onError() {
        mShowUserListView.hideLoading();
        mShowUserListView.showNoResultCase();
    }

    public interface ShowUserListView {
        void showLoading();
        void hideLoading();
        void showNoResultCase();
        void hideNoResultCase();
        void showResult(Collection<User> usersCollection);
        void showToast(String UserId);
    }
}
