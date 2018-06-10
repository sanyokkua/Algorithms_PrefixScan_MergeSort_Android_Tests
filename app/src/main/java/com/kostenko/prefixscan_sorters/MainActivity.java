package com.kostenko.prefixscan_sorters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kostenko.TestSuit;
import com.kostenko.mergesort.TestSorters;
import com.kostenko.scan.TestScans;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private List<String> outList;
    private TestSuit prefixScan;
    private TestSuit sorters;
    private AsyncTask<Void, Void, Void> task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        outList = new ArrayList<>();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(false);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvAdapter = new RVAdapter(outList);
        recyclerView.setAdapter(rvAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefixScan = new TestScans();
        sorters = new TestSorters();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (task != null) {
            task.cancel(true);
            while (!task.isCancelled()){
                //wait
            }
            task = null;
        }

        outList.clear();
        rvAdapter.notifyDataSetChanged();
        new Handler().postDelayed(() -> recyclerView.smoothScrollToPosition(outList.size()), 1);

        if (id == R.id.nav_prefixscan) {
            task = new TestAsyncTask(prefixScan, rvAdapter, recyclerView, outList);
        } else if (id == R.id.nav_sorters) {
            task = new TestAsyncTask(sorters, rvAdapter, recyclerView, outList);
        }

        if (task != null) {
            task.execute();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private List<String> dataSet;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(TextView textView) {
                super(textView);
                this.textView = textView;
            }
        }

        public RVAdapter(List<String> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(dataSet.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    private static class TestAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TestSuit testSuit;
        private final RecyclerView.Adapter rvAdapter;
        private final RecyclerView recyclerView;
        private final List<String> outList;

        public TestAsyncTask(TestSuit testSuit, RecyclerView.Adapter rvAdapter, RecyclerView recyclerView, List<String> outList) {
            this.testSuit = testSuit;
            this.rvAdapter = rvAdapter;
            this.recyclerView = recyclerView;
            this.outList = outList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            testSuit.runAllTests(this::print);
            return null;
        }

        private void print(String text) {
            outList.add(text);
            publishProgress();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            rvAdapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> recyclerView.smoothScrollToPosition(outList.size()), 1);
        }
    }
}
