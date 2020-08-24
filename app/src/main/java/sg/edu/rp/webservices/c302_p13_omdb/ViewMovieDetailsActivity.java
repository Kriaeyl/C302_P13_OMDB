package sg.edu.rp.webservices.c302_p13_omdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewMovieDetailsActivity extends AppCompatActivity {

    private EditText etTitle, etRated, etReleased, etRuntime, etGenre, etActors, etPlot, etLanguage, etPoster;
    private Button btnUpdate, btnDelete;
    private String movieId;
    private FirebaseFirestore fbfs;
    private DocumentReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie_details);

        etTitle = findViewById(R.id.etTitle);
        etRated = findViewById(R.id.etRated);
        etReleased = findViewById(R.id.etReleased);
        etRuntime = findViewById(R.id.etRuntime);
        etGenre = findViewById(R.id.etGenre);
        etActors = findViewById(R.id.etActors);
        etPlot = findViewById(R.id.etPlot);
        etLanguage = findViewById(R.id.etLanguage);
        etPoster = findViewById(R.id.etPoster);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        movieId = intent.getStringExtra("movie_id");

	//TODO: get the movie record from Firestore based on the movieId
	// set the edit fields with the details
        fbfs = FirebaseFirestore.getInstance();
        dr = fbfs.collection("movies").document(movieId);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                etTitle.setText(ds.getString("title"));
                etActors.setText(ds.getString("actors"));
                etGenre.setText(ds.getString("genre"));
                etLanguage.setText(ds.getString("language"));
                etPlot.setText(ds.getString("plot"));
                etPoster.setText(ds.getString("poster"));
                etRated.setText(ds.getString("rating"));
                etReleased.setText(ds.getString("released"));
                etRuntime.setText(ds.getString("runtime"));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpdateOnClick(v);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeleteOnClick(v);
            }
        });
    }//end onCreate

    
    private void btnUpdateOnClick(View v) {
		//TODO: create a Movie object and populate it with the values in the edit fields
		//save it into Firestore based on the movieId
        dr.set(new Movie(etTitle.getText().toString(), etRated.getText().toString(), etReleased.getText().toString(), etRuntime.getText().toString(), etGenre.getText().toString(), etActors.getText().toString(), etPlot.getText().toString(), etLanguage.getText().toString(), etPoster.getText().toString()));
        finish();
    }//end btnUpdateOnClick

    private void btnDeleteOnClick(View v) {
		//TODO: delete from Firestore based on the movieId
        dr.delete();
        finish();
    }//end btnDeleteOnClick

}//end class