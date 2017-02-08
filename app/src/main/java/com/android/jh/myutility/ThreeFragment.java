package com.android.jh.myutility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ThreeFragment extends Fragment {
    // WebView
    WebView webView;
    // 일종의 view holder : 성능이 개선될 여지가 많다.
    View view;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Holder 처리
        if( view != null )
            return view;

        view =inflater.inflate(R.layout.fragment_three, container, false);
        // 1. 사용할 위젯을 가져온다.
        webView = (WebView) view.findViewById(R.id.web);

        // script 사용 설정 (필수)
        webView.getSettings().setJavaScriptEnabled(true);
        // 줌사용
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        // 3. 웹뷰 클라이언트를 지정... (안하면 내장 웹브라우저가 팝업된다.)
        webView.setWebViewClient(new WebViewClient());
        // 3.1 둘다 세팅할것 : 프로토콜에 따라 클라이언트가 선택되는것으로 파악됨...
        webView.setWebChromeClient(new WebChromeClient());
        // 최초 로드시 google.com 이동
        webView.loadUrl("https://google.com");

        return view;
    }

    public boolean goBack() {
        if(webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
        //줄여서 사용할땐
        // webView.goBack();
        // return webView.canGoBack();
    }
}