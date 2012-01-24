package com.seavenois.tetris;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class Game extends Activity {
	
	BoardView gameBoard;
	int d; // The side of a box
	public Box[][] box;
	int x, y;
	BoardView boardView;
	Display display;
	CountDownTimer timer;
	Piece currentPiece;
	Piece nextPiece;
	boolean pieceOnGame;
	Button btnMoveRight, btnMoveLeft, btnMoveDown, btnRotateRight, btnRotateLeft;
	ImageView nextPieceImg;
	
    /** Called when the activity is first created. */    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Assign layouts
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game);
        
        //Assign buttons
        btnMoveRight = (Button) findViewById(R.id.ButtonMoveR);
        btnMoveLeft = (Button) findViewById(R.id.ButtonMoveL);
        btnMoveDown = (Button) findViewById(R.id.ButtonMoveD);
        btnRotateRight = (Button) findViewById(R.id.buttonRotateR);
        btnRotateLeft = (Button) findViewById(R.id.ButtonRotateL);
        
        //Get measures for the board
        display = getWindowManager().getDefaultDisplay(); 
        int width = (int) (display.getWidth() * 0.7);
        int height = (int) (display.getHeight() * 0.9);
        gameBoard = (BoardView) findViewById(R.id.GameView);
        d = (int) (width * 0.85 / 10);
        
        //Initialize boxes and draw the wall
        box = new Box[20][10];
        x = (int) (width * 0.05);
        y = (int) (height * 0.05);
        gameBoard.createWall(x, y, d);
        for (int i = 0; i < 20; i++){
        	x = (int) (width * 0.05);
        	for (int j = 0; j < 10; j++){
        		box[i][j] = new Box(x + d * j, y + d * i, d);
        		gameBoard.initialize(i, j, x, y, d);
        		x = x + d;
        	}
        	y = y + d;
        };
        //Initialize pieces
        currentPiece = new Piece();
        nextPiece = new Piece();
        nextPieceImg = (ImageView) findViewById(R.id.imageViewNext);
        //Start the time bucle
        timer = new CountDownTimer(150000, 1000) {
	        public void onTick(long millisUntilFinished) {
	        	gameAction();
	        }

	        public void onFinish() {
	        	gameAction();
	            start();
	        }
	     }.start();
    
	    //Assign buttons action
	    btnMoveRight.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.moveRight();
	    		reDraw();
			}
	    });
	    btnMoveLeft.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.moveLeft();
	    		reDraw();
			}
	    });
	    //TODO: On long click
	    btnMoveDown.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.moveDown();
	    		reDraw();
			}
	    });
	    btnRotateRight.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.rotateRight();
	    		reDraw();
			}
	    });
	    btnRotateLeft.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		//currentPiece.rotateLeft(); TODO
	    		reDraw();
			}
	    });
	    //Start the game
	    currentPiece.start();
	    //Set image for next piece
		switch(nextPiece.type){
		case Values.PIECE_0:
			nextPieceImg.setImageResource(R.drawable.piece0);
			break;
		case Values.PIECE_1:
			nextPieceImg.setImageResource(R.drawable.piece1);
			break;
		case Values.PIECE_2:
			nextPieceImg.setImageResource(R.drawable.piece2);
			break;
		case Values.PIECE_3:
			nextPieceImg.setImageResource(R.drawable.piece3);
			break;
		case Values.PIECE_4:
			nextPieceImg.setImageResource(R.drawable.piece4);
			break;
		case Values.PIECE_5:
			nextPieceImg.setImageResource(R.drawable.piece5);
			break;
		case Values.PIECE_6:
			nextPieceImg.setImageResource(R.drawable.piece6);
			break;
		}
    }
    
    //The time bucle
    public void gameAction(){
    	
    	//Undraw the current piece
    	unDraw();
    	//Try to move
		if (currentPiece.moveDown() == false){
			//Then: fix the blocks currently occupied, and start a new piece
			for (int i = 0; i < 20; i++)
	        	for (int j = 0; j < 10; j++){
	        		if (currentPiece.box[i][j] == true){
	        			box[i][j].setColor(currentPiece.getColor());
	        			gameBoard.setColor(i, j, currentPiece.getColor());
	        		}
	        	}
			currentPiece = nextPiece;
			currentPiece.start();
			nextPiece = new Piece();
			//Set the next piece image
			switch(nextPiece.type){
			case Values.PIECE_0:
				nextPieceImg.setImageResource(R.drawable.piece0);
				break;
			case Values.PIECE_1:
				nextPieceImg.setImageResource(R.drawable.piece1);
				break;
			case Values.PIECE_2:
				nextPieceImg.setImageResource(R.drawable.piece2);
				break;
			case Values.PIECE_3:
				nextPieceImg.setImageResource(R.drawable.piece3);
				break;
			case Values.PIECE_4:
				nextPieceImg.setImageResource(R.drawable.piece4);
				break;
			case Values.PIECE_5:
				nextPieceImg.setImageResource(R.drawable.piece5);
				break;
			case Values.PIECE_6:
				nextPieceImg.setImageResource(R.drawable.piece6);
				break;
			}
		}
		//Copy the board info to the piece
    	currentPiece.readBoard(box);
    	reDraw();
    }
    
    public void reDraw(){
    	//Read where the piece is and colorize
    	for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++){
        		if (currentPiece.box[i][j] == true){
        			box[i][j].setColor(currentPiece.getColor());
        			gameBoard.setColor(i, j, currentPiece.getColor());
        		}
        	}
    }
    public void unDraw(){
    	//Undraw the current piece before moving
    	for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++){
        		if (currentPiece.box[i][j] == true){
        			box[i][j].setColor(0);
        			gameBoard.setColor(i, j, (byte) 0);
        		}
        	}
    }
}
