package com.example.aydenhomes;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentBookingActivity extends AppCompatActivity {

    private RecyclerView recView1;
    private List<Room> bookedRooms;
    private TextView txtAlarm;
    private BookedRoomAdapter adapter;
    private DatabaseReference roomRef;
    private String currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_current_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        recView1.setLayoutManager(new LinearLayoutManager(this));
        bookedRooms = new ArrayList<>();

       adapter = new BookedRoomAdapter(this,bookedRooms,roomRef);
        recView1.setAdapter(adapter);

        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadRentedHouses();

    }

    private void loadRentedHouses() {

        //show the rooms rented by the current user only
        roomRef.orderByChild("rentedBy").equalTo(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bookedRooms.clear();

                for(DataSnapshot snap : snapshot.getChildren()){
                    Room room = snap.getValue(Room.class);
                    if(room != null){
                        bookedRooms.add(room);
                    }
                }
                adapter.notifyDataSetChanged();

                //show no rooms in the list alert if user hasn't rented any rooms
                if(bookedRooms.isEmpty()){
                    txtAlarm.setVisibility(View.VISIBLE);
                }else{
                    txtAlarm.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CurrentBookingActivity.this, "Can't load rented rooms", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {

        recView1 = findViewById(R.id.recView1);

        txtAlarm = findViewById(R.id.txtAlarm);
    }

}