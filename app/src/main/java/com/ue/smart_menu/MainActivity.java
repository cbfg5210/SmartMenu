package com.ue.smart_menu;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.jake.smartmenu.R;
import com.ue.smartmenu.SmartMenu;

import name.gudong.statebackground.OneDrawable;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvTest = findViewById(R.id.tvTest);
        Drawable color2 = OneDrawable.createBgColor(this, ContextCompat.getColor(this, R.color.colorAccent));
        tvTest.setBackgroundDrawable(color2);
        tvTest.setOnClickListener(v->{Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();});

        SmartMenu smartMenu = findViewById(R.id.smartMenu);
        SmartMenu smartMenu2 = findViewById(R.id.smartMenu2);
        SmartMenu smartMenu3 = findViewById(R.id.smartMenu3);

        smartMenu.setTexts(new String[]{
                "A",
                "Bbbbb",
                " 悔棋 ",
                "D",
                "E",
                "F",
        }, (AdapterView<?> parent, View view, int position, long id) -> {
            toast("item" + position);
        });

        smartMenu2.setImages(
                new int[]{
                        R.mipmap.icon_album,
                        R.mipmap.icon_comment,
                        R.mipmap.icon_comment,
                        R.mipmap.icon_comment,
                        R.mipmap.icon_draft,
                        R.mipmap.icon_like
                }
                , (AdapterView<?> parent, View view, int position, long id) -> {
                    toast("item" + position);
                });
        smartMenu2.toggle();
//        smartMenu2.setSmartListener(v -> {
//            toast("smart");
//        });

        smartMenu3.setTexts(new String[]{
                "AAA",
                "BBB",
                "C",
                "D",
                "E",
                "F",
        }, (AdapterView<?> parent, View view, int position, long id) -> {
            toast("item" + position);
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
