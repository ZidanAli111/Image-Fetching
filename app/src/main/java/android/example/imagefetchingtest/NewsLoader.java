package android.example.imagefetchingtest;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {


        private String mSearchUrl;

        private List<News>mData;

    public NewsLoader(Context context,String url) {
        super(context);
        mSearchUrl=url;
    }
        /** Explicitly making the loader make HTTP request and begin loading data from content provider */

        @Override
        protected void onStartLoading() {

        if(mData!=null){
            deliverResult(mData);
        }
        else{
            Log.i("INDICATE","TEST:  onStartLoading called... ");

            forceLoad();
        }
    }
        /**
         * This method is called in a background thread and takes care of the heavy lifting generating
         * new data from the API
         */
        public List<News> loadInBackground() {

        Log.i("INDICATE","TEST: loadInBackground()  called... ");


        if (mSearchUrl == null) {
            return null;
        }
        // Returns the list of books matching search criteria from Google books
        // after performing network request, parsing input stream, and extracting a list of books

        return Utils.fetchBooks(mSearchUrl);
    }
        @Override
        public void deliverResult(List<News> data) {
        mData = data; // Cache data
        super.deliverResult(data);
    }

    }





