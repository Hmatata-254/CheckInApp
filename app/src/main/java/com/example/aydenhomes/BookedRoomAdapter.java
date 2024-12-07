package com.example.aydenhomes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookedRoomAdapter extends RecyclerView.Adapter<BookedRoomAdapter.ViewHolder> {

    private Context mContext;
    private List<Room> bookedRooms;
    private DatabaseReference houseRef;

    public BookedRoomAdapter(Context mContext, List<Room> bookedRooms, DatabaseReference houseRef) {
        this.mContext = mContext;
        this.bookedRooms = bookedRooms;
        this.houseRef = FirebaseDatabase.getInstance().getReference("rooms");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.current_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Room room = bookedRooms.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(room.getImages().get("bedRoom")).into(holder.recImage);

        holder.txtRoomId.setText("Room No: " + room.getId());
        holder.txtRoomType.setText(room.getType());


        //check out user
        //return the status of the room to available so the it appears back in the list
        holder.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               houseRef.child(room.getId()).child("status").setValue("available");
               houseRef.child(room.getId()).child("rentedBy").setValue(null);

                Toast.makeText(mContext, "Checked out successfully", Toast.LENGTH_SHORT).show();

                bookedRooms.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookedRooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView recImage;
        private TextView txtRoomId, txtRoomType , btnCheckOut;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recImage = itemView.findViewById(R.id.recImage);
            txtRoomId = itemView.findViewById(R.id.txtRoomId);
            txtRoomType = itemView.findViewById(R.id.txtRoomType);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
        }
    }
}
