package android.example.imagefetchingtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter  extends ArrayAdapter<News> {


    ImageView NewsImage;

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_news, parent, false);

        }
        News currentNews = getItem(position);

        NewsImage = listItemView.findViewById(R.id.news_image);

        if (currentNews.getmImage() != null) {
            NewsImage.setImageBitmap(currentNews.getmImage());

        }else {
            NewsImage.setImageResource(R.drawable.ic_launcher_background);
        }
        return listItemView;
    }
}