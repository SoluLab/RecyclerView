package solulab.demo.recyclerview.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import solulab.demo.recyclerview.R;
import solulab.demo.recyclerview.adapters.GridAdapter;
import solulab.demo.recyclerview.adapters.ListAdapter;
import solulab.demo.recyclerview.adapters.StaggeredAdapter;
import solulab.demo.recyclerview.dialogs.ProgressDialog;
import solulab.demo.recyclerview.models.ImageModel;
import solulab.demo.recyclerview.network.JSONCallback;
import solulab.demo.recyclerview.network.Retrofit;
import solulab.demo.recyclerview.utility.NullStringToEmptyAdapterFactory;

public class MainActivity extends AppCompatActivity {
    private List<ImageModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private ListAdapter listAdapter;
    private GridAdapter gridAdapter;
    private StaggeredAdapter staggeredAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        //Initially we are working with Linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(this, list);
        gridAdapter = new GridAdapter(this, list);
        staggeredAdapter = new StaggeredAdapter(this, list);
        recyclerView.setAdapter(listAdapter);

        API_GET_IMAGES();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.types, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionList:
                setLayoutManager(new LinearLayoutManager(this), listAdapter);
                return true;
            case R.id.actionGrid:
                setLayoutManager(new GridLayoutManager(this, 2), gridAdapter);
                return true;
            case R.id.actionStaggered:
                setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), staggeredAdapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLayoutManager(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void API_GET_IMAGES() {
        Retrofit.with(this)
                .getAPIInterface()
                .getMethod("10073051-6c84d6a1a86eb8b3649e0bd04")
                .enqueue(new JSONCallback(this, getProgressBar()) {
                    @Override
                    protected void onSuccess(int statusCode, JSONObject jsonObject) {
                        hideProgressBar();
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                .create();

                        List<ImageModel> array = gson.fromJson(jsonObject.optJSONArray("hits").toString(),
                                new TypeToken<List<ImageModel>>() {
                                }.getType());

                        list.addAll(array);
                        listAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onFailed(int statusCode, String message) {
                        hideProgressBar();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public ProgressDialog getProgressBar() {
        return getProgressBar(null);
    }

    public ProgressDialog getProgressBar(@Nullable String message) {
        if (dialog == null) dialog = new ProgressDialog(this, message);
        return dialog;
    }

    public void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
