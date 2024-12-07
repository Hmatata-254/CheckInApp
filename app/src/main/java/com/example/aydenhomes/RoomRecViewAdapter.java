package com.example.aydenhomes;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RoomRecViewAdapter extends RecyclerView.Adapter<RoomRecViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Room> roomList;

    public RoomRecViewAdapter(Context mContext, List<Room> roomList) {
        this.mContext = mContext;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.rooms_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Room room = roomList.get(position);

        Glide.with(mContext)
                .load(roomList.get(position).getImages().get("bedRoom")).into(holder.imgFrontView);


        holder.txtHouseID.setText("Room No: " + room.getId());
        holder.txtType.setText(room.getType());


        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext, RoomDetailsActivity.class);
                intent.putExtra("id",room.getId());
                intent.putExtra("rentPrice",room.getRentPrice());
                intent.putExtra("status",room.getStatus());
                intent.putExtra("description",room.getDescription());
                mContext.startActivity(intent);

                Toast.makeText(mContext, "Scroll right to view room images", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFrontView;
        private TextView txtHouseID ,txtType , btnDetails ,txtDescript;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFrontView = itemView.findViewById(R.id.imgFrontView);
            txtHouseID = itemView.findViewById(R.id.txtHouseID);
            txtType = itemView.findViewById(R.id.txtType);
            btnDetails = itemView.findViewById(R.id.btnDetails);

            txtDescript = itemView.findViewById(R.id.txtDescript);
        }
    }
}
