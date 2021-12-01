package com.project.plantappui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class NetworkActivity extends AppCompatActivity {
    private Button btnUpload, btnSave;
    private ImageView imageView;

    FirebaseStorage storage;
    StorageReference storageReference;
    private final int IMG_REQUEST_ID = 10;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        FirebaseApp.initializeApp(NetworkActivity.this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnUpload =findViewById(R.id.btn_upload);
        btnSave =findViewById(R.id.btn_save);
        imageView =findViewById(R.id.img_view);

        btnSave.setEnabled(false);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInFirebase();
            }
        });
    }

    private void requestImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select_Picture"), IMG_REQUEST_ID);
    }

    private void saveInFirebase(){
        if(imgUri != null){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();

            StorageReference reference = storageReference.child(UUID.randomUUID().toString());

            try {
                reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(NetworkActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NetworkActivity.this, "Error Occurred" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        btnUpload.setEnabled(true);
                        btnSave.setEnabled(false);

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null ){

            imgUri = data.getData();

            try {
                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                imageView.setImageBitmap(bitmapImg);
                btnUpload.setEnabled(false);
                btnSave.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


}