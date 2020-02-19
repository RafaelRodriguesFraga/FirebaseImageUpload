package com.example.not.firebaseimageupload;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.not.firebaseimageupload.Adapter.ImageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;


    private Uri mImageUri;
    private Button mBtnFile;
    private EditText mEtFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Button mBtnUpload;
    private TextView mTvShowUploads;
    private RecyclerView mRecyclerView;


    //Firebase Variables
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private StorageTask mStorageTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnFile = (Button) findViewById(R.id.btnFile);
        mEtFileName = (EditText) findViewById(R.id.etFileName);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mBtnUpload = (Button) findViewById(R.id.btnUpload);
        mTvShowUploads = (TextView) findViewById(R.id.tvShowUploads);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this);
//        ImageAdapter adapter = new ImageAdapter(this, mUploads);
//        mRecyclerView.setAdapter(adapter);


        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        mBtnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStorageTask != null && mStorageTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "Upload in progress...", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        mTvShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }

    private void openImagesActivity() {
        Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
        startActivity(intent);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mStorageTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            delayProgressBar();

                            Toast.makeText(MainActivity.this, "Uploaded Successfully",
                                    Toast.LENGTH_SHORT).show();

                            Upload upload = new Upload(mEtFileName.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString());

                            //Create a simple entry in the database with unique id
                            String uploadId = mDatabaseReference.push().getKey();

                            //Take this unique id and set it's data to the uploaded file
                            mDatabaseReference.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //(100 * total bytes uploaded so far) / total bytes to upload
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());

                            mProgressBar.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    //Delay ProgressBar for 0.55s
    private void delayProgressBar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(0);
            }
        }, 500);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Method called when we pick our file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();
           Picasso.get().load(mImageUri).into(mImageView);
        }
    }


}
