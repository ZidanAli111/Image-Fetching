package android.example.imagefetchingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<News>> {


    /** URL for books data from the Google books API */
    private String REQUEST_URL = "https://content.guardianapis.com/search?";

    private static final String API_KEY = "db3ccbf8-c13e-438c-8407-e1f38fda331e";

    private NewsAdapter newsAdapter;


    /** Constant value for the earthquake loader ID */
    private static final int EARTHQUAKE_LOADER_ID = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        GridView bookListView = (GridView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(newsAdapter);

        REQUEST_URL +="api-key="+ API_KEY+"&show-fields=thumbnail";
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the loader manager in order to interact with the loaders
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader manager. Pass in the constant declared above as the ID of the
            // loader manager and pass in null for the bundle parameter. Finally, also pass in the
            // context of the application since this application implements the LoaderCallbacks interface
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this);
        }
    }







    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        // Clear the adapter of previous data
        newsAdapter.clear();

        // Add valid list of books to the adapter
        if(data!=null&&!data.isEmpty()){
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
newsAdapter.clear();
    }
}

