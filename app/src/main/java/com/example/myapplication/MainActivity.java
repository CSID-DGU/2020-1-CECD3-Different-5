package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.myapplication.R.id.main_frame;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHome fragHome;
    private FragDash fragDash;
    private FragMypage fragMypage;

    ImageButton but_camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent_log = new Intent( getApplicationContext(), LoginActivity.class );
        startActivity(intent_log);

        but_camera = (ImageButton) findViewById(R.id.cameraButton);
        but_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.dashboard:
                        setFrag(1);
                        break;
                    case R.id.mypage:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
        fragHome = new FragHome();
        fragDash = new FragDash();
        fragMypage = new FragMypage();
        setFrag(0); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택

    }

    private void setFrag(int n) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction(); //실제적인 프레그먼트 교체에서 사용
            switch (n) {
            case 0:
                ft.replace(main_frame,fragHome);
                ft.commit(); //저장의미
                break;
            case 1:
                ft.replace(main_frame, fragDash);
                ft.commit(); //저장의미
                break;
            case 2:
                ft.replace(main_frame,fragMypage);
                ft.commit(); //저장의미
                break;
            }
    }

}
