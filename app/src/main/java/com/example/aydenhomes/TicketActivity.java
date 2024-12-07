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

public class TicketActivity extends AppCompatActivity {

    private RecyclerView recView;
    private List<Maintainance> ticketList;
    private TextView txtAlert;
    private TicketAdapter adapter;
    private DatabaseReference ticketRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        recView.setLayoutManager(new LinearLayoutManager(this));
        ticketList = new ArrayList<>();

        adapter = new TicketAdapter(this,ticketList,ticketRef);
        recView.setAdapter(adapter);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();





        ticketRef = FirebaseDatabase.getInstance().getReference("maintainance");

        ticketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ticketList.clear();

               for(DataSnapshot snap : snapshot.getChildren()){
                   Maintainance maintainance = snap.getValue(Maintainance.class);
                   ticketList.add(maintainance);
               }
               adapter.notifyDataSetChanged();

                if(ticketList.isEmpty()){
                    txtAlert.setVisibility(View.VISIBLE);
                }else{
                    txtAlert.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(TicketActivity.this, "Error loading tickets", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {

        recView = findViewById(R.id.recView);

        txtAlert = findViewById(R.id.txtAlert);
    }
}