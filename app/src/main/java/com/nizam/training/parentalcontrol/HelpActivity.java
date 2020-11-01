package com.nizam.training.parentalcontrol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    ImageView imageView;
    Button prev, next;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        imageView = findViewById(R.id.helpimg);
        textView = findViewById(R.id.helptext);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        final int[] imageArray = new int[]{R.mipmap.scr1, R.mipmap.scr2, R.mipmap.scr3, R.mipmap.scr4, R.mipmap.scr5, R.mipmap.scr6,
                R.mipmap.scr7, R.mipmap.scr8, R.mipmap.scr9, R.mipmap.scr10, R.mipmap.scr11};
        final int[] helpText = new int[]{R.string.h1, R.string.h2, R.string.h3, R.string.h4, R.string.h5, R.string.h6, R.string.h7, R.string.h8, R.string.h9, R.string.h10, R.string.h11};
        imageView.setImageResource(R.mipmap.scr1);
        textView.setText(helpText[0]);
        final SharedPreferences sf = getSharedPreferences("help", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sf.edit();
        editor.putInt("inc", sf.getInt("inc", 1) - 1).apply();

        try {
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        editor.putInt("inc", sf.getInt("inc", 1) - 1).apply();
                        imageView.setImageResource(imageArray[sf.getInt("inc", 1) - 1]);
                        textView.setText(helpText[sf.getInt("inc", 1) - 1]);

                    } catch (Exception e) {
                        editor.putInt("inc", 10).apply();
                        imageView.setImageResource(imageArray[10]);
                        textView.setText(helpText[10]);

                    }
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        editor.putInt("inc", sf.getInt("inc", 1) + 1).apply();
                        imageView.setImageResource(imageArray[sf.getInt("inc", 1)]);
                        textView.setText(helpText[sf.getInt("inc", 1)]);

                    } catch (Exception e) {
                        editor.putInt("inc", 0).apply();
                        imageView.setImageResource(imageArray[0]);
                        textView.setText(helpText[0]);
                    }
                }
            });
        } catch (Exception e) {
            editor.putInt("inc", 1).apply();
        }

    }
}
