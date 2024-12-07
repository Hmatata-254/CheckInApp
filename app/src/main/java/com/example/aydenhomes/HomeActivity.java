package com.example.aydenhomes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recView;

    private ImageView btnMenu;
    private LinearLayout btnCurrentBook,btnMaintain ,btnHistory;
    private TextView txtUser;
    private ArrayList<Room> roomList;
    private DatabaseReference roomRef;
    private RoomRecViewAdapter adapter;
    private DatabaseReference userRef;

    private ProgressDialog pd;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        recView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        roomList = new ArrayList<>();
        adapter = new RoomRecViewAdapter(this,roomList);
        recView.setAdapter(adapter);

        pd = new ProgressDialog(this);
        pd.setMessage("Logging out");




        roomRef = FirebaseDatabase.getInstance().getReference("rooms");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
            //proceed with authenticated user
        } else {
            //handle unauthenticated state
            Log.e("HomeActivity", "No authenticated user found!");
            // Redirect to login screen
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }




        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String fName = snapshot.child("firstName").getValue(String.class);

                    if(fName != null){
                        txtUser.setText("Hello " + fName + "!");
                    }
                    else {
                        txtUser.setText("Hello user!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(HomeActivity.this, "Failed to get first name", Toast.LENGTH_SHORT).show();
            }
        });

        //only show houses that are available(not rented) in the list

        roomRef.orderByChild("status").equalTo("available").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                roomList.clear();

                for(DataSnapshot snap : snapshot.getChildren()){
                    Room room = snap.getValue(Room.class);
                    roomList.add(room);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(HomeActivity.this, "Kuna shida mahali", Toast.LENGTH_SHORT).show();
            }
        });

        btnCurrentBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CurrentBookingActivity.class);
                //intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        //take user to maintainance activity
        btnMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, MaintainanceActivity.class);
                startActivity(intent);
            }
        });


        //take user to ticket activity
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketActivity.class);
                startActivity(intent);
            }
        });


        //log out user(ask user if they are sure of the proccess)
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                builder.setCancelable(false);

                builder.setMessage("Are you sure you want to sign out?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pd.show();
                        FirebaseAuth.getInstance().signOut();

                        pd.dismiss();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.dismiss();

                    }
                });

                builder.create().show();

            }
        });


    }

    private void initViews() {

        recView = findViewById(R.id.recView);
        txtUser = findViewById(R.id.txtUser);

        btnCurrentBook = findViewById(R.id.btnCurrentBook);
        btnMenu = findViewById(R.id.btnMenu);
        btnMaintain = findViewById(R.id.btnMaintain);
        btnHistory = findViewById(R.id.btnHistory);
    }
}