package com.android.jh.myutility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<String> datas = new ArrayList<>();
    View imgLayout;

    public MyItemRecyclerViewAdapter(Context context,View view) {
        imgLayout = view;
        this.context = context;
        // 폰에서 이미지를 가져와 datas에 세팅한다.
        ContentResolver resolver = context.getContentResolver();
        // 데이터 uri 정의
        Uri target = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // projection
        String projection[] = { //image 경로가 있는 컬럼명
                MediaStore.Images.Media.DATA
        };
        // 데이터 가져오기
        Cursor cursor = resolver.query(target, projection, null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()) {
                String uriString = cursor.getString(0);
                datas.add(uriString);
            }
        }
        cursor.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imgUri = datas.get(position);
        Glide.with(context)
                .load(holder.imgUri)
                .into(holder.imgView);
        holder.posit = position;
    }

    public void addToList(String fileUri) {
        datas.add(fileUri);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public String imgUri;
        RecyclerView recyclerView;
        ImageView popupimg;
        RelativeLayout popupLayout;
        Button btnCamera;
        int posit;

        public ViewHolder(View view) {
            super(view);
            imgView = (ImageView) view.findViewById(R.id.img);
            imgUri = null;
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupimg = (ImageView)imgLayout.findViewById(R.id.imgDetail);
                    popupLayout = (RelativeLayout) imgLayout.findViewById(R.id.itemLayout);
                    btnCamera = (Button) imgLayout.findViewById(R.id.btnCamera);
                    recyclerView = (RecyclerView) imgLayout.findViewById(R.id.list);
                    popupLayout.setVisibility(View.VISIBLE);
                    btnCamera.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    imgUri = datas.get(posit);
                    Glide.with(context).load(imgUri).into(popupimg);
                }
            });
        }
    }
}
