package com.example.yashw.uber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonDriver;
    private Button buttonCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCustomer=(Button)findViewById(R.id.ButtonCustomer);
        buttonDriver=(Button)findViewById(R.id.ButtonDriver);

        buttonDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DriverLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        buttonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CustomerLoginActivty.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
