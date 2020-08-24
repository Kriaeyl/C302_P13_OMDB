package sg.edu.rp.webservices.c302_p13_omdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Movie> list;
    private MovieAdapter adapter;
    FirebaseFirestore fbfs;
    CollectionReference cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewMovies);
        list = new ArrayList<Movie>();
        adapter = new MovieAdapter(this, R.layout.movie_row, list);
        listView.setAdapter(adapter);

		//TODO: retrieve all documents from the "movies" collection in Firestore (realtime)
		//populate the movie objects into the ListView
        fbfs = FirebaseFirestore.getInstance();
        cr = fbfs.collection("movies");

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                list.clear();
                for (QueryDocumentSnapshot doc : value) {
                    list.add(new Movie(doc.getId(), doc.getString("title"), doc.getString("rating"), doc.getString("released"), doc.getString("runtime"), doc.getString("genre"), doc.getString("actors"), doc.getString("plot"), doc.getString("language"), doc.getString("poster")));
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie selectedContact = list.get(position);
                Intent i = new Intent(getBaseContext(), ViewMovieDetailsActivity.class);
                i.putExtra("movie_id", selectedContact.getMovieId());
                startActivity(i);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            Intent intent = new Intent(getApplicationContext(), CreateMovieActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}