package info.competitiveprogramming.getfollowers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.competitiveprogramming.getfollowers.R;
import info.competitiveprogramming.getfollowers.adapter.RepoAdapter;
import info.competitiveprogramming.getfollowers.api.GithubApi;
import info.competitiveprogramming.getfollowers.constant.S;
import info.competitiveprogramming.getfollowers.executor.UIThread;
import info.competitiveprogramming.getfollowers.model.Repos;
import info.competitiveprogramming.getfollowers.model.User;
import info.competitiveprogramming.getfollowers.presenter.ShowRepoListPresenter;
import info.competitiveprogramming.getfollowers.repository.ReposRepository;
import info.competitiveprogramming.getfollowers.usecase.ReposUseCase;

public class UserDetailActivity extends AppCompatActivity implements ShowRepoListPresenter.ShowReposView{

    @InjectView(R.id.detail_name_tv)
    TextView mDetailNameTv;
    @InjectView(R.id.detail_photo_riv)
    RoundedImageView mDetailPhotoIv;
    @InjectView(R.id.detail_repos_result_lv)
    ListView mDetailLv;

    private ShowRepoListPresenter mShowReposListPresenter;
    private RepoAdapter mRepoAdapter;
    private GithubApi mApi;
    private User mUser;

    public static Intent createIntent(Context context, User user){
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(S.user, user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        mUser = (User)getIntent().getSerializableExtra(S.user);
        ButterKnife.inject(this);
        Glide.with(this).load(mUser.avatar_url).into(mDetailPhotoIv);
        mDetailNameTv.setText(mUser.login);
        mRepoAdapter = new RepoAdapter(this, new ArrayList<Repos>());
        mDetailLv.setAdapter(mRepoAdapter);
        initialize();
    }

    private void initialize(){

        //DataSource Layer: RepositoryImpl
        ReposRepository reposRepository = ReposRepository.getRepository();

        //Domain Layer: UseCase
        ReposUseCase reposUseCase = ReposUseCase.getUseCase(reposRepository, UIThread.getInstance());

        mShowReposListPresenter = new ShowRepoListPresenter(reposUseCase);
        mShowReposListPresenter.setShowReposView(this);
        mShowReposListPresenter.getRepos(mUser.login);
    }

    @Override
    public void showResult(Collection<Repos> reposes) {
        mRepoAdapter.addAll(reposes);
        mRepoAdapter.notifyDataSetChanged();
    }
}
