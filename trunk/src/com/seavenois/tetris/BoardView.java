package com.seavenois.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/*************************************************/
/* The game board ********************************/
/*************************************************/
public class BoardView extends View{
	
	//Drawables for the board boxes, the playable zone
	public Drawable[][] block = new Drawable[20][10];
	//Drawables for the wall (yes, it's done with tiles)
	public Drawable[] wall = new Drawable[102];
	//Context and canvas to be used along the class
	Context context;
	Canvas c;
	
	/*************************************************/
	/* Class constructor *****************************/
	/*************************************************/
	/* Defines the context and the canvas ************/
	/*************************************************/
	public BoardView(Context cont, AttributeSet attrs) {
		super(cont, attrs);
		context = cont;
	}
	
	/*************************************************/
	/* Initializes drawables for playable boxes ******/
	/*************************************************/
	/* Must be initialized one by one from the Game **/
	/* activity, passing all the parameters **********/
	/*************************************************/
	public void initialize(int i, int j, int left, int top, int side){
		block[i][j] = context.getResources().getDrawable(R.drawable.alpha);
        block[i][j].setBounds(left, top, left + side, top + side);
	}
	
	/*************************************************/
	/* Draws the board wall **************************/
	/*************************************************/
	/* Needs the top-left point of the board frame ***/
	/* and the width of the wall *********************/
	/*************************************************/
	public void createWall(int left, int top, int side){
		int i = 0, x, y;
		x = left - side / 2;
		y = top;
		//The left wall
		while (i < 40){
			wall[i] = context.getResources().getDrawable(R.drawable.brick);
			wall[i].setBounds(x, y, x + side / 2, y + side / 2);
			y = y + side / 2;
			i = i + 1;
		}
		x = left + side * 10;
		y = top;
		//The right wall
		while (i < 80){
			wall[i] = context.getResources().getDrawable(R.drawable.brick);
			wall[i].setBounds(x, y, x + side / 2, y + side / 2);
			y = y + side / 2;
			i = i + 1;
		}		
		x = left - side / 2;
		//The floor
		while (i < 102){
			wall[i] = context.getResources().getDrawable(R.drawable.brick);
			wall[i].setBounds(x, y, x + side / 2, y + side / 2);
			x = x + side / 2;
			i = i + 1;
		}		
	}
	
	/*************************************************/
	/* Draws the board *******************************/
	/*************************************************/
	/* Draws the walls and all the boxes *************/
	/*************************************************/
	@Override
    protected void onDraw(Canvas canvas) {
		c = canvas;
        super.onDraw(canvas); 
        for (int i = 0; i < 102; i++)
        	wall[i].draw(c);
        for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++){
        		block[i][j].draw(canvas);
        	}
        //Actually draw
        invalidate();
    }
	
	/*************************************************/
	/* Canvas getter *********************************/
	/*************************************************/
	public Canvas getCanvas(){
        return c;
	}
	
	/*************************************************/
	/* Colors a box **********************************/
	/*************************************************/
	/* Changes the drawable for the indicated box to */
	/* to 'c'. Can also be COLOR_NONE to undraw ******/
	/*************************************************/
	public void setColor(int i, int j, byte c){
		Rect rect;
		rect = block[i][j].getBounds();
		switch (c){
			case Values.COLOR_NONE:
				block[i][j] = context.getResources().getDrawable(R.drawable.alpha);;
				break;
			case Values.COLOR_RED:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_red);
				break;
			case Values.COLOR_GREEN:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_green);
				break;
			case Values.COLOR_BLUE:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_blue);
				break;
			case Values.COLOR_YELLOW:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_yellow);
				break;
			case Values.COLOR_PINK:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_pink);
				break;
			case Values.COLOR_PURPLE:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_purple);
				break;
			case Values.COLOR_WHITE:
				block[i][j] = context.getResources().getDrawable(R.drawable.block_white);
				break;
			}
		block[i][j].setBounds(rect);
	}
}
