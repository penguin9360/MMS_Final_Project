package com.project.plantappui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class NetworkActivity extends AppCompatActivity {
    private Button btnUpload, btnSave;
    private ImageView imageView;
    private TextView name_text_view;
    private TextView description_text_view;

    FirebaseStorage storage;
    StorageReference upload_reference;
    StorageReference return_reference;
    private final int IMG_REQUEST_ID = 10;
    private Uri imgUri;
    public static String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        FirebaseApp.initializeApp(NetworkActivity.this);

        storage = FirebaseStorage.getInstance();
        upload_reference = storage.getReference();

        btnUpload =findViewById(R.id.btn_upload);
        btnSave =findViewById(R.id.btn_save);
        imageView =findViewById(R.id.img_view);
        name_text_view = findViewById(R.id.name_text_view);
        description_text_view = findViewById(R.id.description_text_view);

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

            filePath = UUID.randomUUID().toString();
            StorageReference reference = upload_reference.child(filePath);

            try {
                reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(NetworkActivity.this, "uploaded Successfully", Toast.LENGTH_SHORT).show();
                        // getReseults();
                        downloadFile();
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
                        progressDialog.setMessage("Saved" + (int) progress + "%");
                        btnUpload.setEnabled(true);
                        btnSave.setEnabled(false);

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void downloadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("/" + filePath + ".json");
        System.out.println("storageRef: | " + storageRef.toString());

        File rootPath = new File(Environment.getExternalStorageDirectory(), "download_data");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
            // System.out.println("rootPath is :   | " + rootPath.toString());
        }

        final File localFile = new File(rootPath, filePath + ".json");

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ", ";local tem file created  created " + localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());
            }
        });
    }

    private void getReseults() {
        return_reference = FirebaseStorage.getInstance().getReference().child(filePath);
        try {
            final File localFile = File.createTempFile(filePath, ".json");

            return_reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(NetworkActivity.this, "Retrieved Successfully", Toast.LENGTH_SHORT).show();

                    JSONParser parser = new JSONParser();
                    JSONObject jsonObj = null;
//                    try {
//                        jsonObj = (JSONObject) parser.parse(new FileReader(localFile.getAbsolutePath()));
//                    } catch (IOException | ParseException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("local file path: " + localFile.getAbsolutePath());
//                    String plant_name = null;
//                    String plant_description = null;
//                    try {
//                        plant_name = jsonObj.getString("name");
//                        name_text_view.setText(plant_name);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        plant_description = jsonObj.getString("discription");
//                        description_text_view.setText(plant_description);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    try (BufferedReader br = new BufferedReader(new FileReader(localFile.getAbsolutePath()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Getting JSON Array node
//                    JSONArray plant_name = null;
//                    try {
//                        plant_name = jsonObj.getJSONArray("name");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    // looping through All Contacts
//                    for (int i = 0; i < plant_name.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//                        String id = c.getString("id");
//                        String name = c.getString("name");
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");
//                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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