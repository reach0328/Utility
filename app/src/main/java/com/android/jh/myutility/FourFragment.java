package com.android.jh.myutility;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class FourFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    MainActivity mainActivity;
    SupportMapFragment supportMapFragment;
    LocationManager manager;


    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        manager = mainActivity.getLocationManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        //fragment에서 mapview를 호출 하는 방법
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 리스너 등록
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //GPS 제공자의 정보가 바뀌면 콜백 하도록 리스너 등록
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치 제공자
                    3000, // 통지사이의 최소 시간 간격(milisecond) // 업데이트 간격
                    10, // 통지 사이의 최소 거리 반경거리
                    locationListener);

           // 정보 제공자 네트워크 프로바이더 등록 // 정확도가GPS에 비해서 안좋다
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치 제공자
                3000, // 통지사이의 최소 시간 간격(milisecond) // 업데이트 간격
                10, // 통지 사이의 최소 거리 반경거리
                locationListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        // 리스너 해제
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        manager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // 신사역 좌표
        LatLng sydney = new LatLng(37.516066, 127.019361);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자

            //내 위치
            LatLng myPosition = new LatLng(latitude,longitude);
                                            //위도 , 경도
            mMap.addMarker(new MarkerOptions().position(myPosition).title("I am Here"));
                                                        //내위치           //마커 타이틀
            // 화면 위치를 내위치로 이동 시키는 함수
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,10));
                                                        //내 위치

        }

        @Override   // provider의 상태변경 시 호출
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override   // gps가 사용할 수 없었다가 다시 사용 할 수 있을 때
        public void onProviderEnabled(String s) {

        }

        @Override // gps가 사용할 수 없을 때 호출
        public void onProviderDisabled(String s) {

        }
    };
}
