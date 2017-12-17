package com.codingdojoangola.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codingdojoangola.R;
import com.codingdojoangola.models.news.New;
import com.codingdojoangola.ui.news.adapter.AdapterNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by a2x on 16-Dec-17.
 */

public class NewViewBind implements AdapterView.OnItemClickListener{

    private List<New> news;
    private FirebaseUser user;
    private Button button_post_new;
    private ProgressBar progressBar2;
    private Activity activity;

    private DatabaseReference databaseReference;

    private ListView list_view_news;

    public NewViewBind(Activity activity, View view) {

        this.activity = activity;
        list_view_news = (ListView) view.findViewById(R.id.list_view_news);
        button_post_new = (Button) view.findViewById(R.id.button_post_new);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);


        user = FirebaseAuth.getInstance().getCurrentUser();
        list_view_news.setOnItemClickListener(this);

        progressBar2.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(activity.getString(R.string.database_reference_news));
        databaseReference.orderByChild("publicationDate");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                news = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    news.add(shot.getValue(New.class));
                }

                AdapterNews adapterNews = new AdapterNews(activity, news);
                list_view_news.setAdapter(adapterNews);
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        activity.startActivity(new Intent(activity,NewsDetailsActivity.class).putExtra(activity.getString(R.string.news_id),news.get(i).getId()));
    }


}
