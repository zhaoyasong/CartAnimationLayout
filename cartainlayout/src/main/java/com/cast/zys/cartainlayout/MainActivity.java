package com.cast.zys.cartainlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView cart;
    private CartAnimLayout cartAnimLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化控件
        button = (Button) findViewById(R.id.add);
        cart = (ImageView) findViewById(R.id.cart);
        cartAnimLayout = (CartAnimLayout) findViewById(R.id.cartAnimLayout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartAnimLayout.startCartAnim(button, cart, R.layout.move_view);
            }
        });
    }
}
