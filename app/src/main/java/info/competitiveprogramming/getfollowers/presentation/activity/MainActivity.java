package info.competitiveprogramming.getfollowers.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import info.competitiveprogramming.getfollowers.R;
import info.competitiveprogramming.getfollowers.data.repository.UserRepository;
import info.competitiveprogramming.getfollowers.domain.executor.UIThread;
import info.competitiveprogramming.getfollowers.domain.model.User;
import info.competitiveprogramming.getfollowers.domain.usecase.CheckUserUseCase;
import info.competitiveprogramming.getfollowers.domain.usecase.FollowerListUseCase;
import info.competitiveprogramming.getfollowers.presentation.adapter.UserAdapter;
import info.competitiveprogramming.getfollowers.presentation.presenter.ShowUserListPresenter;
import info.competitiveprogramming.getfollowers.utility.StringUtil;

public class MainActivity extends AppCompatActivity implements ShowUserListPresenter.ShowUserListView {

    public static final String TAG = MainActivity.class.getSimpleName();
    @InjectView(R.id.search_result_view)
    ListView mListView;
    @InjectView(R.id.no_result_tv)
    TextView mNoResultTv;
    @InjectView(R.id.account_et)
    EditText mAccountEt;
    @InjectView(R.id.start_search_bt)
    Button searchBt;
    @InjectView(R.id.progress)
    View mProgress;

    private UserAdapter mUserAdapter;

    ShowUserListPresenter mShowUserListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mUserAdapter = new UserAdapter(this, new ArrayList<User>());
        mListView.setAdapter(mUserAdapter);
        mAccountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        initialize();

    }

    private void initialize() {
        //DataSource Layer: RepositoryImpl
        UserRepository userRepositoryImpl = UserRepository.getRepository();

        //Domain Layer: UseCase
        FollowerListUseCase followerListUserCaseImpl = FollowerListUseCase.getUseCase(userRepositoryImpl, UIThread.getInstance());
        CheckUserUseCase checkUserUseCaseImpl = CheckUserUseCase.getUseCase(userRepositoryImpl, UIThread.getInstance());

        //Initialize Presenter
        mShowUserListPresenter = new ShowUserListPresenter(followerListUserCaseImpl, checkUserUseCaseImpl);
        mShowUserListPresenter.setShowUserListView(this);
    }

    @OnClick(R.id.start_search_bt)
    public void onClick() {
        searchBt.setFocusable(true);
        searchBt.setFocusableInTouchMode(true);
        searchBt.requestFocus();
        String text = mAccountEt.getText().toString();
        if (!StringUtil.isNullOrEmpty(text)) {
            mShowUserListPresenter.getFollowerList(text);
        }
    }

    @OnItemClick(R.id.search_result_view)
    public void onListItemClick(AdapterView<?> adapter, View view, int pos, long id) {
        User user = (User) view.getTag(R.id.list_item);
        mShowUserListPresenter.checkUser(user);
        Intent intent = UserDetailActivity.createIntent(this, user);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        mListView.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showNoResultCase() {
        mListView.setVisibility(View.GONE);
        mNoResultTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoResultCase() {
        mNoResultTv.setVisibility(View.GONE);
    }

    @Override
    public void showResult(Collection<User> usersCollection) {
        mListView.setVisibility(View.VISIBLE);
        mUserAdapter.refresh(usersCollection);
    }

    @Override
    public void showToast(String UserId) {
        Toast toast = Toast.makeText(getApplicationContext(), UserId + " is clicked before!", Toast.LENGTH_LONG);
        toast.show();
    }
}
