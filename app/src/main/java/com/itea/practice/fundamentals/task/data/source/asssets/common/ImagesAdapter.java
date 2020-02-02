package com.itea.practice.fundamentals.task.data.source.asssets.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.fundamentals.R;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.Holder> {
    private Context context;
    private List<Bitmap> bitmaps;

    public ImagesAdapter(Context context) {
        this(context, new ArrayList<Bitmap>());
    }

    public ImagesAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    public void addImage(Bitmap img) {
        bitmaps.add(img);
        notifyItemInserted(getItemCount() - 1);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_img, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.img.setImageBitmap(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private ImageView img;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView;
        }
    }
}