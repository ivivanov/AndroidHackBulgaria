package com.example.ivan.touchandgestures;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class PuzzleActivity extends Activity {
    private final int rowsCount = 4;
    private final int colsCount = 4;
    private Toast toast;
    private TypedArray puzzlePieces;
    private List<ImageView> puzzleImages;
    private LinearLayout[] rows = new LinearLayout[4];
    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_layout);
        this.puzzlePieces = getResources().obtainTypedArray(R.array.puzzels);
        this.puzzleImages = new ArrayList<ImageView>(this.puzzlePieces.length());
        this.toast = Toast.makeText(this, "Bravo", Toast.LENGTH_LONG);
        this.grid = (GridLayout) findViewById(R.id.grid);

        InitScreen();
    }

    private void InitScreen() {
        for (int i = 0; i < this.puzzlePieces.length(); i++) {
            ImageView piece = CreateDragableImageView(i);
            this.puzzleImages.add(piece);
        }
        this.puzzlePieces.recycle();
        Collections.shuffle(this.puzzleImages);

        for (int i = 0; i < this.puzzleImages.size(); i++) {
            grid.addView(this.puzzleImages.get(i));
        }
    }

    private ImageView CreateDragableImageView(int i) {
        ImageView piece = new ImageView(this);
        piece.setTag(i);
        Drawable pieceDr = this.puzzlePieces.getDrawable(i);
        piece.setImageDrawable(pieceDr);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 120;
        params.setGravity(Gravity.LEFT | Gravity.TOP);
        piece.setLayoutParams(params);
        piece.setAdjustViewBounds(true);

        piece.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Instantiates the drag shadow builder.
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                view.startDrag(null,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        view,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );

                return true;
            }
        });

        piece.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                final int action = dragEvent.getAction();
                if (action == DragEvent.ACTION_DROP) {
                    ImageView otherView = ((ImageView) dragEvent.getLocalState());

                    Object tempTag = view.getTag();
                    Object otherTag = otherView.getTag();

                    otherView.setTag(tempTag);
                    view.setTag(otherTag);

                    ((ImageView) view).setImageAlpha(255);

                    // Animation
                    PropertyValuesHolder x = PropertyValuesHolder.ofFloat("x", view.getX());
                    PropertyValuesHolder y = PropertyValuesHolder.ofFloat("y", view.getY());
                    PropertyValuesHolder otherX = PropertyValuesHolder.ofFloat("x", otherView.getX());
                    PropertyValuesHolder otherY = PropertyValuesHolder.ofFloat("y", otherView.getY());
                    ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, otherX, otherY);
                    ObjectAnimator otherAnim = ObjectAnimator.ofPropertyValuesHolder(otherView, x, y);
                    AnimatorSet animSetXY = new AnimatorSet();
                    animSetXY.playTogether(anim, otherAnim);
                    animSetXY.setDuration(1000);
                    animSetXY.start();

                    if (isGameWon()) {
                        toast.show();
                    }
                }

                if (action == DragEvent.ACTION_DRAG_ENTERED) {
                    ((ImageView) view).setImageAlpha(150);
                }

                if (action == DragEvent.ACTION_DRAG_EXITED) {
                    ((ImageView) view).setImageAlpha(255);
                }
                return true;
            }
        });

        return piece;
    }

    private boolean isGameWon() {
        for (int i = 0; i < 16; i++) {
            int tag = Integer.parseInt(grid.getChildAt(i).getTag().toString());
            if (tag != i) return  false;
        }
        return true;
    }
}
