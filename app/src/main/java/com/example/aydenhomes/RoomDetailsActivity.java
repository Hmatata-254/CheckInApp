package com.example.aydenhomes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TextView txtHseId, txtPrice , txtDesc , btnRentRoom;

    private DatabaseReference roomRef;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialise views
        initViews();

        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        pd = new ProgressDialog(this);
        pd.setMessage("Processing");


        //get the current house id
        String id = getIntent().getStringExtra("id");



        roomRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Room room = snapshot.getValue(Room.class);

                    txtHseId.setText("Room No:" + room.getId());
                    txtPrice.setText(room.getRentPrice() + " KSH");
                    txtDesc.setText(room.getDescription());


                    List<String> imageUrls = new ArrayList<>(room.getImages().values());
                    ViewPagerAdapter adapter = new ViewPagerAdapter(RoomDetailsActivity.this,imageUrls);
                    viewPager.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRentRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();

                //get user id
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //GET HOUSE ID
                String id = getIntent().getStringExtra("id");


                DatabaseReference houseRef = FirebaseDatabase.getInstance().getReference("rooms").child(id);






                //tie the viewed house to the current user
                houseRef.child("rentedBy").setValue(userId);

                //change the status of the viewed house to rented
                houseRef.child("status").setValue("rented").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RoomDetailsActivity.this, "House rented", Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                            Intent intent = new Intent(RoomDetailsActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(RoomDetailsActivity.this, "Failed to rent room,please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });


    }

    public void initViews(){

        viewPager = findViewById(R.id.viewPager);

        btnRentRoom = findViewById(R.id.btnRentRoom);

        txtHseId = findViewById(R.id.txtHseId);
        txtPrice = findViewById(R.id.txtPrice);
        txtDesc = findViewById(R.id.txtDesc);
    }
}