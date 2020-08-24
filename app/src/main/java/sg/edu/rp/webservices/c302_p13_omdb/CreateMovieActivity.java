package sg.edu.rp.webservices.c302_p13_omdb;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CreateMovieActivity extends AppCompatActivity {

    private EditText etTitle, etRated, etReleased, etRuntime, etGenre, etActors, etPlot, etLanguage, etPoster;
    private Button btnCreate, btnSearch;
    private ImageButton btnCamera;
    private String apikey;
    FirebaseFirestore fbfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_movie);

        etTitle = findViewById(R.id.etTitle);
        etRated = findViewById(R.id.etRated);
        etReleased = findViewById(R.id.etReleased);
        etRuntime = findViewById(R.id.etRuntime);
        etGenre = findViewById(R.id.etGenre);
        etActors = findViewById(R.id.etActors);
        etPlot = findViewById(R.id.etPlot);
        etLanguage = findViewById(R.id.etLanguage);
        etPoster = findViewById(R.id.etPoster);
        btnCreate = findViewById(R.id.btnCreate);
        btnSearch = findViewById(R.id.btnSearch);
        btnCamera = findViewById(R.id.btnCamera);
        fbfs = FirebaseFirestore.getInstance();


        //TODO: Retrieve the apikey from SharedPreferences
        //If apikey is empty, redirect back to LoginActivity
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        apikey = sharedPreferences.getString("apikey", "");
        if (apikey.isEmpty()) {
            Intent i = new Intent(CreateMovieActivity.this, LoginActivity.class);
            startActivity(i);
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCreateOnClick(v);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearchOnClick(v);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCameraOnClick(v);
            }
        });

    }//end onCreate

    //TODO: extract the fields and populate into a new instance of Movie class
    // Add the new movie into Firestore
    private void btnCreateOnClick(View v) {
        fbfs.collection("movies").document().set(new Movie(etTitle.getText().toString(), etRated.getText().toString(), etReleased.getText().toString(), etRuntime.getText().toString(), etGenre.getText().toString(), etActors.getText().toString(), etPlot.getText().toString(), etLanguage.getText().toString(), etPoster.getText().toString()));
        finish();
    }

    //TODO: Call www.omdbapi.com passing the title and apikey as parameters
    // extract from JSON response and set into the edit fields
    private void btnSearchOnClick(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.omdbapi.com/?apikey=" + apikey + "&t=" + etTitle.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    etRated.setText(response.getString("Rated"));
                    etReleased.setText(response.getString("Released"));
                    etRuntime.setText(response.getString("Runtime"));
                    etGenre.setText(response.getString("Genre"));
                    etActors.setText(response.getString("Actors"));
                    etPlot.setText(response.getString("Plot"));
                    etLanguage.setText(response.getString("Language"));
                    etPoster.setText(response.getString("Poster"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void btnCameraOnClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //TODO: feed imageBitmap into FirebaseVisionImage for text recognizing
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    // Task completed successfully
                                    // ...
                                    String msg = "";
                                    for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()) {
                                        String blocktext = block.getText();
                                        msg += blocktext;
                                    }
                                    etTitle.setText(msg);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            e.printStackTrace();
                                        }
                                    });
        }
    }
}