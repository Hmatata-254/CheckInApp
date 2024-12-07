package com.example.aydenhomes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private Context mContext;
    private List<Maintainance> ticketList;
    private DatabaseReference ticketRef;

    public TicketAdapter(Context mContext, List<Maintainance> ticketList, DatabaseReference ticketRef) {
        this.mContext = mContext;
        this.ticketList = ticketList;
        this.ticketRef = FirebaseDatabase.getInstance().getReference("maintainance");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.ticket_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Maintainance maintainance = ticketList.get(position);

        Glide.with(mContext)
                .load(maintainance.getImageUrl())
                .into(holder.recImage);

        holder.txtTicketId.setText("Ticket ID: " + maintainance.getId());
        holder.txtDescription.setText("Description: " + maintainance.getDescription());

        holder.btnCheckOut.getText().toString();


    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView recImage;
        private TextView txtTicketId , txtDescription, btnCheckOut;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recImage = itemView.findViewById(R.id.recImage);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);


            txtTicketId = itemView.findViewById(R.id.txtTicketId);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
