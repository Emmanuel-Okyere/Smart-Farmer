package com.example.smartpro;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity4 extends AppCompatActivity {
    private EditText ipxx1;
    private static Button led;

    public static String texr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        led=(Button) findViewById(R.id.click);
        ipxx1=(EditText) findViewById(R.id.ipadd);

        led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texr=ipxx1.getText().toString();
                Intent ht1 = new Intent(MainActivity4.this,MainActivity2.class);
                startActivity(ht1);
            }
        });
    }
}
