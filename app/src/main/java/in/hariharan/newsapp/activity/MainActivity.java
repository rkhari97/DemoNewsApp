package in.hariharan.newsapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.hariharan.newsapp.R;
import in.hariharan.newsapp.adapter.MainArticleAdapter;
import in.hariharan.newsapp.model.Article;
import in.hariharan.newsapp.model.ResponseModel;
import in.hariharan.newsapp.rests.APIInterface;
import in.hariharan.newsapp.rests.ApiClient;
import in.hariharan.newsapp.utils.OnRecyclerViewItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {
    private static final String API_KEY = "0eac7e98a6884800b86125cea377d60d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainRecycler.setLayoutManager(linearLayoutManager);
        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("techcrunch", API_KEY);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);
                        mainRecycler.setAdapter(mainArticleAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });
    }

    @Override
    public void onItemClick(int position, View view) {
        switch (view.getId()) {
            case R.id.article_adapter_ll_parent:
                Article article = (Article) view.getTag();
                if (!TextUtils.isEmpty(article.getUrl())) {
                    Log.e("clicked url", article.getUrl());
//                    Intent webActivity = new Intent(this, WebActivity.class);
//                    webActivity.putExtra("url", article.getUrl());
//                    startActivity(webActivity);
                }
                break;
        }
    }
}