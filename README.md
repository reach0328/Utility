# My Utility
탭 레이아웃으로 앱을 제작합니다

## Google Map in Fragment
* fragment_layout.xml

레이아웃에서 SupportMapFragment 로 정의합니다.
```xml
<fragment
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:name="com.google.android.gms.maps.SupportMapFragment"
    android:id="@+id/mapView" />
```

* Fragment.java

소스코드에서 OnMapReadyCallBack 인터페이스를 구현합니다
```java
public class SubFragment extends Fragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        // Fragment 에서 mapview 호출하기
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        // Async 함수에서 Fragment 에서 구현한 OnMapReadyCallBack 이 호출된다
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap map = googleMap;
        // 신사역 좌표
        LatLng seoul = new LatLng(37.516066, 127.019361);
        map.addMarker(new MarkerOptions().position(seoul).title("Sinsa in Seoul"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,17));
    }

}
```

## ADB shell
adb shell 사용법을 익힙니다

* adb devices - device 목록보기
* adb -s 디바이스명 shell - device shell 연결
* adb -s 디바이스면 pull /디바이스디렉토리/원본파일명 최종파일명
* adb -s 디바이스명 push 원본파일명 /디바이스디렉토리/최종파일명

## Method Trace
* WRITE_EXTERNAL_STORAGE 권한설정
* Source code 에서 사용
```java
// 메써드 추적 시작 ------
Debug.startMethodTracing("결과파일");

// 메써드 추적 종료
Debug.stopMethodTracing();
```
* 결과파일 에뮬레이터에서 가져오기

adb -s 디바이스명 pull sdcard/결과파일.trace 최종파일.trace

* 결과파일 보기

traceview 결과파일.trace


###**Alert Dialog**
![](https://www.tutorialspoint.com/android/images/alert_dialog.jpg)

###**[Custom Alert Dialog](http://kitesoft.tistory.com/71)**

![](http://cfile5.uf.tistory.com/image/211C09375579431312599F)

###**[GPS 정보](http://mainia.tistory.com/1153)**
![](http://cfile1.uf.tistory.com/image/2220EF4554585CF12BC48C)
>스마트폰에 GPS 설정이 되어 있지 않을 때 팝업창을 띄워서 설정창으로 이동할 수 있는 소스도 추가가 되어 있습니다. 


###**[LayoutInflater]()**
![](http://cfile10.uf.tistory.com/image/15085B44510AAB8905FD13)
> - LayoutInflater란?
>XML에 정의된 Resource(자원) 들을 View의 형태로 반환해 줍니다. 보통 자바 코드에서 View, ViewGroup 을 사용하거나, Adpter의 getview() 또는 Dialog, Popup 구현시 배경화면이 될 Layout을 만들어 놓고 View의 형태로 반환 받아 Acitivity에서 실행 하게 됩니다.
>
> - 우리가 보통 Activity를 만들면 onCreate() 메서드에 기본으로 추가되는 setContentView(R.layout.activity_main) 메서드와 같은 원리라고 생각하시면 됩니다. 
> 이 메서드 또한 activity_main.xml 파일을 View로 만들어서 Activity 위에 보여주고 있습니다. 사용자의 화면에 보여지는 것들은 Activity 위에 있는 View라는 점을 잊지 말아 주세요~! 

###**[Context]()**

> - Context  는 크게 두 가지 역할을 수행하는 Abstract 클래스 입니다.
>  - 어플리케이션에 관하여 시스템이 관리하고 있는 정보에 접근하기 
>  - 안드로이드 시스템 서비스에서 제공하는 API 를 호출 할 수 있는 기능
>> Context 인터페이스가 제공하는 API 중, getPackageName(), getResource() 등의 메서드들이 첫 번째 역할을 수행하는 대표적인 메서드입니다. 보통 get 이라는 접두어로 시작하는 메서드들이지요. 그 외에, startActivity() 나 bindService() 와 같은 
메서드들이 두 번째 역할을 수행하기 위한 메서드라고 할 수 있습니다.

>  전역적인 어플리케이션 정보에 접근하거나 어플리케이션 연관된 시스템 기능을 수행하기 위해, 시스템 함수를 호출하는 일은 안드로이드가 아닌 다른 플랫폼에서도 늘상 일어나는 일입니다. 또, 그런 일들은 대게의 경우 (제가 아는한...) 어떠한 매개체를 거칠 필요없이, 직접적으로 시스템 API 호출하면 됩니다. 반면 안드로이드에서는 Context 라는 인스턴스화된 매개체를 통해야만 유사한 일들을 수행할 수 있습니다.



> Context를 상속한 객체 (4대 컴포넌트, 애플리케이션) 표입니다
>![](https://files.slack.com/files-tmb/T3G59BNJF-F41SQA69Y-b91e7e5f8d/pasted_image_at_2017_02_07_03_05_pm_720.png)

