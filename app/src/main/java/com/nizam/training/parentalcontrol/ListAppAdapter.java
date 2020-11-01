//ListAppAdapter.java
package com.nizam.training.parentalcontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.MyBuilder> {

    private ArrayList<String> strings;
    private ArrayList<String> packages;
    private ArrayList<Drawable> drawables;
    private Context ct;

    ListAppAdapter(Context ct, ArrayList<String> strings, ArrayList<Drawable> drawables, ArrayList<String> packages) {
        this.strings = strings;
        this.ct = ct;
        this.packages = packages;
        this.drawables = drawables;
    }

    @NonNull
    @Override
    public MyBuilder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.myrow, parent, false);
        return new MyBuilder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MyBuilder holder, final int positions) {
        final int position = holder.getAdapterPosition();
        holder.textView.setText(strings.get(position));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap;
                try {
                    bitmap = getBitmapFromDrawable(drawables.get(position));
                    Intent intent = new Intent(ct, AppSettingActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("label", strings.get(position));
                    intent.putExtra("package", packages.get(position));
                    intent.putExtra("picture", bitmap);
                    intent.putExtra("position", position);
                    ct.startActivity(intent);
                } catch (Exception e) {
                    //       bitmap = (Bitmap) drawables.get(position);
                    Log.i("HHH", e.getMessage());
                    Toast.makeText(ct, ct.getString(R.string.openmsg), Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imageView.setImageDrawable(drawables.get(position));
        Animation fadein = AnimationUtils.loadAnimation(ct, R.anim.animation_popup_enter);
        holder.linearLayout.setAnimation(fadein);

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @NonNull
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    class MyBuilder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        private MyBuilder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.rowview);
            textView = itemView.findViewById(R.id.appName);
            imageView = itemView.findViewById(R.id.appIcon);
        }
    }
}
