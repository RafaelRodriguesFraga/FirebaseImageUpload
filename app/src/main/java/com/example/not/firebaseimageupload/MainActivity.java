package com.example.not.firebaseimageupload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;


    private Uri mImageUri;
    private Button mBtnFile;
    private EditText mEtFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Button mBtnUpload;
    private TextView mTvShowUploads;


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

        mBtnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mTvShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
            mImageView.setImageURI(mImageUri);

        }
    }


}
