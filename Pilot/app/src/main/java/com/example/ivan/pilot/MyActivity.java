package com.example.ivan.pilot;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;


public class MyActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_color_edit_text);


//        TextView textView = (TextView)findViewById(R.id.title_tv);

//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((TextView)view).setTextSize(34);
//            }
//        });

//        fillColorsArray();
//        runVideoPlayer();
        changeColorByHexUserInput();
    }

    private void changeColorByHexUserInput() {
        final EditText input = (EditText) findViewById(R.id.inputTxt);
        Button btn = (Button) findViewById(R.id.showBtn);
        final View screen = findViewById(R.id.screen);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromInput = input.getText().toString();
                int color = Color.parseColor(fromInput);
                screen.setBackgroundColor(color);
            }
        });


    }

    private void runVideoPlayer() {
        //resources
        Uri video = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Movies/Ronaldo.mp4"));

        // init player
        final VideoView screen = (VideoView) findViewById(R.id.screen);
        screen.setVideoURI(video);


        ImageButton prev = (ImageButton) findViewById(R.id.prev);
        ImageButton play = (ImageButton) findViewById(R.id.play);
        ImageButton next = (ImageButton) findViewById(R.id.next);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = ((ImageButton) view);

                if (screen.isPlaying()) {
                    screen.pause();
                    btn.setImageResource(R.drawable.play);
                } else {
                    screen.start();
                    btn.setImageResource(R.drawable.pause);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = ((ImageButton) view);

                int curr = screen.getCurrentPosition();
                screen.seekTo(curr - 3000);
//                screen.pause();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = ((ImageButton) view);

                int curr = screen.getCurrentPosition();
                screen.seekTo(curr + 3000);
//                screen.pause();
            }
        });
    }

    private int[] colors;
    private int currentColorIndex = 0;

    public void fillColorsArray() {
        TypedArray ta = getResources().obtainTypedArray(R.array.colors);
        colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();
    }

    //change color click event handler
    public void onClickChangeColor(View view) {
        view.setBackgroundColor(this.colors[this.currentColorIndex]);
        currentColorIndex++;
        if (currentColorIndex >= colors.length) {
            currentColorIndex = 0;
        }
    }

    @Override
    protected void onStart() {
        Log.v(MyActivity.class.getName(), "pichagata logva ot Start");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(MyActivity.class.getName(), "Logvam kato pich ot resume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(MyActivity.class.getName(), "Logvam kato pich ot resume");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(MyActivity.class.getName(), "Logvam kato pich ot stop");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(MyActivity.class.getName(), "destroyyuvam si");

        super.onDestroy();
    }
}
