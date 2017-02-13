package com.android.jh.myutility;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.app.Activity.RESULT_OK;

public class FiveFragment extends Fragment {
    // 카메라 요청 코드
    private final int REQ_CAMERA = 101;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    Button btnCamera;
    RelativeLayout imgLayout;
    ImageView imgView;
    View view;
    MyItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public FiveFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FiveFragment newInstance(int columnCount) {
        FiveFragment fragment = new FiveFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void init() {
        //프로그램 실행
        // 권한 처리가 통과 되었을 때 만 버튼을 활성화 시켜준다
        buttonEnable();
    }

    private void buttonEnable() {
        btnCamera.setEnabled(true);
    }

    private void buttonDisable() {
        btnCamera.setEnabled(false);
    }

    Uri fileUri = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        btnCamera = (Button) view.findViewById(R.id.btnCamera);
        imgLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
        imgView = (ImageView) view.findViewById(R.id.imgDetail);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 롤리팝 이상 버전에서는 코드를 반영해야 한다.
                // --- 카메라 촬영 후 미디어 컨텐트 uri 를 생성해서 외부저장소에 저장한다 ---
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ContentValues values = new ContentValues(1);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    fileUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                // --- 여기 까지 컨텐트 uri 강제세팅 ---
                startActivityForResult(intent, REQ_CAMERA);

            }
        });
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            adapter = new MyItemRecyclerViewAdapter(getContext(),view);
        if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Camera", "resultCode===============================" + resultCode);
        switch (requestCode) {
            case REQ_CAMERA :
                if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) { // 사진 확인처리됨 RESULT_OK = -1
                    // 롤리팝 체크
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        Log.i("Camera", "data.getData()===============================" + data.getData());
                        fileUri = data.getData();
                    }
                    Log.i("Camera", "fileUri===============================" + fileUri);
                    if (fileUri != null) {
                        // 글라이드로 이미지 세팅하면 자동으로 사이즈 조절
                        detailImg();
                        Glide.with(this)
                                .load(fileUri)
                                .into(imgView);
                        adapter.addToList(fileUri.toString());
                    } else {
                        Toast.makeText(getContext(), "사진파일이 없습니다", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // resultCode 가 0이고 사진이 찍혔으면 uri 가 남는데
                    // uri 가 있을 경우 삭제처리...
                }
                break;
        }
    }
    public void detailImg() {
        imgLayout.setVisibility(View.VISIBLE);
        btnCamera.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean goBack() {
        if(imgLayout.getVisibility()==View.VISIBLE) {
            imgLayout.setVisibility(View.GONE);
            btnCamera.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        } else {
            return false;
        }
        //줄여서 사용할땐
        // webView.goBack();
        // return webView.canGoBack();
    }

}
