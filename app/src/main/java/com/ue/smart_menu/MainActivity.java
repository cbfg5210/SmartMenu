package com.ue.smart_menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jake.smartmenu.R;
import com.ue.smartmenu.SmartMenu;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartMenu smartMenu = findViewById(R.id.smart_menu);

        /*MenuAdapter adapter = new MenuAdapter(new int[]{
                R.mipmap.icon_album,
                R.mipmap.icon_comment,
                R.mipmap.icon_comment,
                R.mipmap.icon_comment,
                R.mipmap.icon_draft,
                R.mipmap.icon_like
        }, (AdapterView<?> parent, View view, int position, long id) -> {
            toast("item" + position);
        });
        smartMenu.setAdapter(adapter);
        */

        /*smartMenu.setImages(new int[]{
                R.mipmap.icon_album,
                R.mipmap.icon_comment,
                R.mipmap.icon_comment,
                R.mipmap.icon_comment,
                R.mipmap.icon_draft,
                R.mipmap.icon_like
        }, (AdapterView<?> parent, View view, int position, long id) -> {
            toast("item" + position);
        });*/

        smartMenu.setTexts(new String[]{
                "A",
                "B",
                "C",
                "D",
                "E",
//                "F",
        }, (AdapterView<?> parent, View view, int position, long id) -> {
            toast("item" + position);
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
