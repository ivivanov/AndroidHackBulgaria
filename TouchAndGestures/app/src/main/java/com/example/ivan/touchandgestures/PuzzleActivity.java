package com.example.ivan.touchandgestures;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
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
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f);
    private Toast toast;
    private TypedArray puzzlePieces;
    private List<ImageView> puzzleImages;
    private LinearLayout[] rows = new LinearLayout[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_layout);
        this.puzzlePieces = getResources().obtainTypedArray(R.array.puzzels);
        this.puzzleImages = new ArrayList<ImageView>(this.puzzlePieces.length());
        this.toast = Toast.makeText(this, "You win this game!", Toast.LENGTH_SHORT);

        InitScreen();
    }

    private void InitScreen() {
        for (int i = 0; i < this.puzzlePieces.length(); i++) {
            ImageView piece = CreateDragableImageView(i);
            this.puzzleImages.add(piece);
        }
        this.puzzlePieces.recycle();
        Collections.shuffle(this.puzzleImages);
        LinearLayout currentView = (LinearLayout) findViewById(R.id.main);

        int counter = 0;
        for (int i = 0; i < this.rowsCount; i++) {
            LinearLayout row = new LinearLayout(this);
            this.rows[i] = row;
            currentView.addView(row);
            for (int j = 0; j < this.colsCount; j++) {
                row.addView(this.puzzleImages.get(counter++));
            }
        }
        return;
    }

    private ImageView CreateDragableImageView(int i) {
        ImageView piece = new ImageView(this);
        piece.setTag(i);
        Drawable pieceDr = this.puzzlePieces.getDrawable(i);
        piece.setImageDrawable(pieceDr);
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
                    Drawable other = otherView.getDrawable();
                    Drawable tempImg = ((ImageView) view).getDrawable();

                    ((ImageView) view).setImageDrawable(other);
                    view.setTag(otherTag);

                    otherView.setImageDrawable(tempImg);
                    otherView.setTag(tempTag);
                    ((ImageView)view).setImageAlpha(255);

                    if (isGameWon()) {
                        toast.show();
                    }
                }

                if (action == DragEvent.ACTION_DRAG_ENTERED){
                    ((ImageView)view).setImageAlpha(150);
                }

                if(action == DragEvent.ACTION_DRAG_EXITED){
                    ((ImageView)view).setImageAlpha(255);
                }
                return true;
            }
        });

        return piece;
    }

    private boolean isGameWon() {
        int counter = 0;
        for (int i = 0; i < this.rowsCount; i++) {
            LinearLayout currentRow = this.rows[i];
            for (int j = 0; j < this.colsCount; j++) {
                ImageView imageView = (ImageView) currentRow.getChildAt(j);
                int tag = Integer.parseInt(imageView.getTag().toString());
                if (tag != counter) {
                    return false;
                }
                counter++;
            }
        }
        return true;
    }
}
