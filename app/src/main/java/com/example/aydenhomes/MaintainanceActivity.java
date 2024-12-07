package com.example.aydenhomes;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class MaintainanceActivity extends AppCompatActivity {

    private EditText etDesc;
    private Spinner categorySpinner;
    private TextView btnSave;
    private ImageView btnUploadImg;

    private String [] category = {"Electrical","Plumbing","Structural","Appliance issue","Pests","Water supply","Safety","Other"};

    private ProgressDialog pd;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maintainance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        pd = new ProgressDialog(this);
        pd.setMessage("Sending ticket");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,category);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        //call activity result(starts an activity and gives back the result)
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {

                        if(o.getResultCode() == RESULT_OK){
                            Intent data = o.getData();
                            uri = data.getData();
                            btnUploadImg.setImageURI(uri);
                        }else{
                            Toast.makeText(MaintainanceActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
            }
        });
    }

    private void saveData() {

        pd.show();


        StorageReference storage = FirebaseStorage.getInstance().getReference().child("user images").child(uri.getLastPathSegment());


        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while(!uriTask.isComplete());
                    Uri uriImage = uriTask.getResult();
                    imageURL = uriImage.toString();
                    uploadData();
                    pd.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();

                Toast.makeText(MaintainanceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {

        String desc = etDesc.getText().toString();
        String category = categorySpinner.toString();

        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference tickerRef = FirebaseDatabase.getInstance().getReference("maintainance");

        String id = tickerRef.push().getKey();
        Maintainance maintainance = new Maintainance(id,desc,category,imageURL);



        tickerRef.child(userID).setValue(maintainance).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(MaintainanceActivity.this, "Ticket submitted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MaintainanceActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(MaintainanceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){

        btnUploadImg = findViewById(R.id.btnUploadImg);
        btnSave = findViewById(R.id.btnSave);

        etDesc = findViewById(R.id.etDesc);
        categorySpinner = findViewById(R.id.categorySpinner);
    }
}