package com.example.aydenhomes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private Context mContext;
    private List<String> imageUrls;

    public ViewPagerAdapter(Context mContext, List<String> imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imageUrl = imageUrls.get(position);

        Glide.with(mContext)
                .load(imageUrl).into(holder.btnImages);

    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView btnImages;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnImages = itemView.findViewById(R.id.btnImages);
        }
    }
}
