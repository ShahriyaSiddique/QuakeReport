package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    EarthquakeAdapter adapter;
    ImageView emptyImage;
    ProgressBar progressBar;

    /** Tag for the log messages */
    public static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    /** URL to query the USGS dataset for earthquake information */
    private static final String SAMPLE_JSON_RESPONSE =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        emptyImage = (ImageView) findViewById(R.id.empty_state);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        earthquakeListView.setEmptyView(emptyImage);


        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!= null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        }
        else {

            progressBar.setVisibility(View.GONE);
            emptyImage.setImageResource(R.drawable.nointernet);


        }

        // Find a reference to the {@link ListView} in the layout

        //earthquakeListView.setEmptyView(textView);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter (this,new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake earthquake = adapter.getItem(position);
                /**
                 * Earthquake earthquake = earthquakes.get(position);
                 */


                String url = earthquake.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, SAMPLE_JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        emptyImage.setImageResource(R.drawable.emptystate);
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty()) {
           adapter.addAll(earthquakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();
    }


    }
