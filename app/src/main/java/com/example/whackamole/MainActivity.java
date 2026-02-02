package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    ImageView hole1;
    ImageView hole2;
    ImageView hole3;
    ImageView hole4;
    ImageView hole5;
    ImageView hole6;
    ImageView hole7;
    ImageView hole8;
    ImageView hole9;
    TextView timerText;
    Handler uiHandler;
    ConstraintLayout layout;
    Button restart;
    int seconds;
    int delayed;
    int oldRando;
    int scoreCount;
    float hBias;
    float vBias;
    boolean canGoDown;
    ArrayList<Boolean> boolList; //true if up, false if down
    ArrayList<ImageView> imageList; //parallel array to boollist
    ArrayList<ImageView> scoreTallyList; //holds all new images for the score tally


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //R.ids
        layout = findViewById(R.id.layout);
        restart = findViewById(R.id.restart_button);
        hole1 = findViewById(R.id.hole1);
        hole2 = findViewById(R.id.hole2);
        hole3 = findViewById(R.id.hole3);
        hole4 = findViewById(R.id.hole4);
        hole5 = findViewById(R.id.hole5);
        hole6 = findViewById(R.id.hole6);
        hole7 = findViewById(R.id.hole7);
        hole8 = findViewById(R.id.hole8);
        hole9 = findViewById(R.id.hole9);

        //onClickListener for all bunnies (calls animation early and adds points)
        View.OnClickListener bunnyOnClick = new View.OnClickListener()
        {
            public void onClick(View view){
                final ScaleAnimation goDOWN = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
                goDOWN.setDuration(300);

                if(boolList.get(imageList.indexOf(view))) //to avoid multiples
                {
                    Points();
                    view.setVisibility(View.GONE);
                    view.startAnimation(goDOWN);
                    boolList.set(imageList.indexOf(view), false);
                }
            }
        };

        hole1.setOnClickListener(bunnyOnClick);
        hole2.setOnClickListener(bunnyOnClick);
        hole3.setOnClickListener(bunnyOnClick);
        hole4.setOnClickListener(bunnyOnClick);
        hole5.setOnClickListener(bunnyOnClick);
        hole6.setOnClickListener(bunnyOnClick);
        hole7.setOnClickListener(bunnyOnClick);
        hole8.setOnClickListener(bunnyOnClick);
        hole9.setOnClickListener(bunnyOnClick);


        boolList = new ArrayList<Boolean>();
        for(int i = 0; i<9; i++)
        {
            boolList.add(false);
        }

        imageList = new ArrayList<ImageView>();
        imageList.add(hole1);
        imageList.add(hole2);
        imageList.add(hole3);
        imageList.add(hole4);
        imageList.add(hole5);
        imageList.add(hole6);
        imageList.add(hole7);
        imageList.add(hole8);
        imageList.add(hole9);

        scoreTallyList = new ArrayList<ImageView>();

        timerText = findViewById(R.id.time_text);

        uiHandler = new Handler();

        //start+restart button (creates new thread and clears out all lists/variables)
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimerThread myThread = new TimerThread();
                myThread.start();
                restart.setText("Restart");

                for(int i = 0; i<scoreTallyList.size(); i++)
                {
                    ((ViewGroup) scoreTallyList.get(i).getParent()).removeView(scoreTallyList.get(i));
                }
                scoreCount = 0;
                hBias = 0;
                vBias = 0;
                scoreTallyList.clear();
            }
        });

    }

    //keeps track of score and creates+adds new images to the screen
    public void Points()
    {
        scoreCount++;

        ImageView newImage = new ImageView(this);
        scoreTallyList.add(newImage);
        newImage.setId(View.generateViewId());
        newImage.setImageResource(R.drawable.curvedbuny);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(135,145);
        newImage.setLayoutParams(layoutParams);
        layout.addView(newImage);

        ConstraintSet newConstraintSet = new ConstraintSet();
        newConstraintSet.clone(layout);

        newConstraintSet.connect(newImage.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        newConstraintSet.connect(newImage.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
        newConstraintSet.connect(newImage.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        newConstraintSet.connect(newImage.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);

        newConstraintSet.setVerticalBias(newImage.getId(), vBias);
        newConstraintSet.setHorizontalBias(newImage.getId(), hBias);

        newConstraintSet.applyTo(layout);

        hBias += 0.122;
        if(scoreCount%9.0 == 0) //9 images fit per row
        {
            hBias = 0;
            vBias += 0.081f;
        }

    }

    //second thread that manages the timer and bunnies
    public class TimerThread extends Thread
    {
        @Override
        public void run() {
            timerText = findViewById(R.id.time_text);
            seconds = 31;

            //timer (runs once every second)
            while(seconds > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                seconds--;

                //handles all things that need widget access
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timerText.setText("Time: "+seconds+"");

                        canGoDown = true;
                        bunnyAppear();

                        if(delayed == 2 && canGoDown) { //disappear is delayed by a second too allow more than one bunny appear on screen
                            bunnyDisappear();
                            delayed = 0;
                        }
                        else if(delayed<2){
                            delayed++;
                        }
                        else{
                            delayed = 0;
                        }

                        if(seconds == 0) //game-end things
                        {
                            timerText.setText("GAME OVER ------------ SCORE: "+scoreCount);

                            hole1.setVisibility(View.GONE);
                            hole2.setVisibility(View.GONE);
                            hole3.setVisibility(View.GONE);
                            hole4.setVisibility(View.GONE);
                            hole5.setVisibility(View.GONE);
                            hole6.setVisibility(View.GONE);
                            hole7.setVisibility(View.GONE);
                            hole8.setVisibility(View.GONE);
                            hole9.setVisibility(View.GONE);

                           // restart.setVisibility(View.VISIBLE);

                        }

                    }
                });
            }

        }

        public void bunnyAppear()
        {
            final ScaleAnimation goUP = new ScaleAnimation(0f, 1.0f, 0f, 1f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            goUP.setDuration(300);

            boolean keepGoing = false;
            int newRando=0;

            //sees how many bunnies are on screen
            int trueCount = 0;
            for(int i=0; i < boolList.size();i++)
            {
                if(boolList.get(i))
                {
                    trueCount++;
                }
            }

            //more bunnies will appear only if there is less than 3 on the screen
            if(trueCount < 3) {

                while (!keepGoing) {
                    newRando = (int) (Math.random() * 9); //random bunny pops up (checks to make sure it isn't already up)
                    if (!boolList.get(newRando)) {
                        keepGoing = true;
                    }
                }

                imageList.get(newRando).setVisibility(View.VISIBLE);
                imageList.get(newRando).startAnimation(goUP);
                boolList.set(newRando, true);
            }
            else if(trueCount > 2)
            {
                bunnyDisappear();
                canGoDown = false;
            }

            if(trueCount < 1) //if theres too little on screen, no bunnies will disappear
            {
                canGoDown = false;
            }

            oldRando = newRando;
        }

        public void bunnyDisappear()
        {
            final ScaleAnimation goDOWN = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            goDOWN.setDuration(300);

            boolean keepGoing = false;
            int disappear=0;
            while(!keepGoing)
            {
                disappear = (int) (Math.random() * 9);
                if(boolList.get(disappear) && disappear != oldRando) //a random bunny goes down (checks to make it is on screen and didn't just appear in the last second)
                {
                    keepGoing = true;
                }
            }

            imageList.get(disappear).startAnimation(goDOWN);
            imageList.get(disappear).setVisibility(View.GONE);
            boolList.set(disappear, false);
        }
    }
}