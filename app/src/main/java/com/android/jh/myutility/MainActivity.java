package com.android.jh.myutility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Stack;

/*
 *  GPS 사용 순서
 *  1. manifest에 FINE,COARSE 권한 추가
 *  2. Runtime permission 소스코드에 추가
 *  3. GPS가 켜져있는지 확인, 꺼져있다면 GPS 화면으로 이동
 *  4. GPS Location Listener 정의
 *  5. Listener 등록
 *  6. Listener 실행
 *  7. Listener 해제
 */
public class MainActivity extends AppCompatActivity {
    final int TAB_COUNT = 4;
    //현재 페이지
    private int page_position = 0;
    ViewPager viewPager;
    OneFragment oneFragment;
    TwoFragment twoFragment;
    ThreeFragment threeFragment;
    FourFragment fourFragment;
    boolean backPress = false;
    //위치정보 관리자
    private LocationManager manager;
    Stack<Integer> viewOrder= new Stack<>();
    public LocationManager getLocationManager() {
        return manager;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //메서드 추적 시작 -----
//        Debug.startMethodTracing("trace_result");

        //프래그먼트 init
        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        threeFragment = new ThreeFragment();
        fourFragment = new FourFragment();

        //탭 layout 정의
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        // 탭 생성 및 타이틀 입력
        tabLayout.addTab(tabLayout.newTab().setText("계산기"));
        tabLayout.addTab(tabLayout.newTab().setText("단위"));
        tabLayout.addTab(tabLayout.newTab().setText("검색"));
        tabLayout.addTab(tabLayout.newTab().setText("지도"));

        // fragment pager 작성
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        // adapter 생성
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 페이저 리스너 :  페이저가 변경 되었을대 탭을 바꿔주는 리스너
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // 탭 리스너 :  탭이 변경 되었을대 페이지를 바꿔주는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        //첫페이지 추가
        viewOrder.push(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //스택 배열에 포지션을 저장해 놓는다
                // 뒤로가기를 했을때는 스택 포지션을 쌓으면 안된다.
                if(!backPress) {
                    viewOrder.push(position);
                } else {
                    backPress = false;
                }
                page_position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //버전 체크해서 마시멜로우(6.0)보다 낮으면 런타임 권한 체크를 하지않는다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermssion();
        } else {
            init();
        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment =null;
            switch (position){
                case 0 : fragment = oneFragment; break;
                case 1 : fragment = twoFragment; break;
                case 2 : fragment = threeFragment; break;
                case 3 : fragment = fourFragment; break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
    private final int REQ_CODE = 100;
    //권한 체크

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermssion() {
        // 1.1 런타임 권한체크
        if( checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                )
        {
            // 1.2 요청할 권한 목록 작성
            String permArr[] = {android.Manifest.permission.ACCESS_FINE_LOCATION
                    , android.Manifest.permission.ACCESS_COARSE_LOCATION
            };
            // 1.3 시스템에 권한요청
            requestPermissions(permArr, REQ_CODE);
        } else{
            init();
        }
    }



    //권한체크 후 콜백< 사용자가 확인후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE){
            //배열에 넘긴 런타임 권한을 체크해서 승인이 됐으면
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                init();
            } else {
                Toast.makeText(this,"권한을 사용하지 않으시면 프로그램을 실행시킬수 없습니다",Toast.LENGTH_SHORT).show();
                // 선택 1.종료, 2. 권한체크 다시물어보기
                checkPermssion();
            }
        }
    }

    private void init() {
        //LocationManager 객체를 얻어온다
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //GPS 센서가 켜져있는지 확인
        //꺼져있다면 GPS를 켜는 페이지로 이동
        if(!gpsCheck()) {
            // - 팝업창 만들기
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // 팝업창 제목
            alertDialog.setTitle("GPS 켜기");
            // 팝업 메시지
            alertDialog.setMessage("GPS 꺼져있습니다. \n 설정창으로 이동하시겠습니까?");
            // YES 버튼 기능
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            // NO 버튼 기능
            alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alertDialog.show();
        }
    }
    //GPS 가 꺼져있는지 체크 롤리팝 이하 버전
    private boolean gpsCheck() {
        // 롤리팝 이상 버전에서는 LocationManager로 GPS 꺼짐 여부 체크
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 롤리팝 이하버전에서는 LOCATION_PROVIDERS_ALLOWED로 체크
        } else {
            String gps = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(gps.matches(".*gps.*")) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        switch (page_position){
            //webview 페이지에서 뒤로가기가 가능하면 아무런 동작하지 않는다
            case 2:
                if(threeFragment.goBack()) {
                    //뒤로가기가 안되면 앱을 닫는다
                } else {
                    BackPage();
                }
                break;
            // 위의 조건에 해당하지 않는 모든 케이스
            default:
                BackPage();
                break;
        }
    }
    public void BackPage() {
        if(viewOrder.size()!=0){
            backPress = true;
             viewPager.setCurrentItem(viewOrder.pop());
        }else {
            super.onBackPressed();
        }
    }
}
