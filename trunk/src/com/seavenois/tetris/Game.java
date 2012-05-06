package com.seavenois.tetris;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*************************************************/
/* Main game activity ****************************/
/*************************************************/
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
	Button btnMoveRight, btnMoveLeft, btnMoveDown, btnRotateRight, btnRotateLeft, btnPause;
	ImageView nextPieceImg;
	//Score and combo
	TextView textScore;
	ImageView imageCombo;
	int score = 0;
	int combo = 1;
	Vibrator vibrator;
	//The pause indicator
	boolean game;
	
    /*************************************************/
	/* On activity creation **************************/
	/*************************************************/
	/* Initializes variables, ui elements, and *******/
	/* starts the game *******************************/
	/* See inline comments for more details **********/
	/*************************************************/
	@Override
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
        btnPause = (Button) findViewById(R.id.buttonPause);
        
        //Get measures for the board
        display = getWindowManager().getDefaultDisplay(); 
        int width = (int) (display.getWidth() * 0.7);
        int height = (int) (display.getHeight() * 0.9);
        gameBoard = (BoardView) findViewById(R.id.GameView);
        d = (int) (width * 0.85 / 10);
        
        //Asign score and combo resources
        textScore = (TextView) findViewById(R.id.TextViewScore);
        imageCombo = (ImageView) findViewById(R.id.imageViewCombo);
        
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
        
        
        //initialize vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        //Initialize pieces
        currentPiece = new Piece();
        nextPiece = new Piece();
        nextPieceImg = (ImageView) findViewById(R.id.imageViewNext);
        
        //Start the time bucle
        game = true;
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
	    		vibrator.vibrate(30);
	    		reDraw();
			}
	    });
	    btnMoveLeft.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.moveLeft();
	    		vibrator.vibrate(30);
	    		reDraw();
			}
	    });
	    //TODO: On long click
	    btnMoveDown.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.moveDown();
	    		vibrator.vibrate(30);
	    		reDraw();
			}
	    });
	    btnRotateRight.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.rotateRight();
	    		vibrator.vibrate(30);
	    		reDraw();
			}
	    });
	    btnRotateLeft.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		unDraw();
	    		currentPiece.rotateLeft();
	    		vibrator.vibrate(30);
	    		reDraw();
			}
	    });
	    btnPause.setOnClickListener(new OnClickListener(){
	    	public void onClick(View v) {
	    		if (game == true){
	    			game = false;
	    			btnPause.setText(R.string.resume);
	    		}
	    		else{
	    			game = true;
	    			btnPause.setText(R.string.pause);
	    		}
	    		vibrator.vibrate(30);	    		
			}
	    });
	    
	    //Start the game
	    textScore.setText("0");
	    currentPiece.start();
	    
	    //Set image for next piece
	    //Has to be done here, or there is no next piece image at the beggining
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
    
	/*************************************************/
	/* Main time bucle *******************************/
	/*************************************************/
	/* Checks the game state (ongoing, paused, *******/ 
	/* ended... On each bucle, it tries to move the **/
	/* current piece down. If its imposible, it ******/
	/* checks for filled rows, updates the combo flag /
	/* if necessary, and check if the game is loose, */
	/* otherwise it initiates a new piece ************/
	/*************************************************/
    public void gameAction(){
    	if (game == true){
	    	//Undraw the current piece
	    	unDraw();
	    	//Try to move it down.
			if (currentPiece.moveDown() == false){
				//If couldnt move the piece down, the boxes occupied by it become ocuupied boxes
				for (int i = 0; i < 20; i++)
		        	for (int j = 0; j < 10; j++){
		        		if (currentPiece.box[i][j] == true){
		        			box[i][j].setColor(currentPiece.getColor());
		        			gameBoard.setColor(i, j, currentPiece.getColor());
		        		}
		        	}
				/// ...check if there is any full row...
				if (lookForRows() == false){
					combo = 1; //If nothing has been removed, set combo to 1
					imageCombo.setImageResource(R.drawable.alpha);
				}
				//... check if the game is loose... 
				checkGameLoose();
				// ... and start a new piece
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
    }
    
    /*************************************************/
	/* Checks if the current game is loose************/
	/*************************************************/
	/* Checks if there is something in the first two */ 
	/* rows of the board. If there is something, *****/
	/* the game is loose. Score is checked for *******/
	/* highscores and those are updated if necessary */
	/* A dialog is shown to the user where the score */
	/* and a trophy (if highscore) are shown, and ****/
    /* asking to choose between exiting or sharing ***/
    /* the score with an external app. On exit, the **/
    /* activity is finished and you return to the ****/
    /* main menu *************************************/
	/*************************************************/
    private void checkGameLoose() {
    	int hScore1, hScore2, hScore3, aux;
    	String hScore1Date, hScore2Date, hScore3Date, auxDate;
    	boolean loose = false;
		for (int j = 0; j < 10; j++)
			if (box[1][j].getColor() != Values.COLOR_NONE)
				loose = true;
		if (loose == false)
			return;
		//If I get here, the game is loose. Game state variable is set to false
		game = false;
		//Vibrate if vibration is active in prefenrences
		//TODO: See line above
		vibrator.vibrate(1000);
		//Add high scores if needed
		SharedPreferences highScores = getSharedPreferences("highScores", 0);
		hScore1 = highScores.getInt("hScore1", 0);
		hScore2 = highScores.getInt("hScore2", 0);
		hScore3 = highScores.getInt("hScore3", 0);
		hScore1Date = highScores.getString("hScore1Date", "0");
		hScore2Date = highScores.getString("hScore2Date", "0");
		hScore3Date = highScores.getString("hScore3Date", "0");
		Calendar currentDate = Calendar.getInstance();
		Date dateNow = currentDate.getTime();
		if(score > hScore3){
			hScore3 = score;
			hScore3Date = dateNow.toString();
		}
		if(hScore3 > hScore2){
			aux = hScore2;
			auxDate = hScore2Date;
			hScore2 = hScore3;
			hScore2Date = hScore3Date;
			hScore3 = aux;
			hScore3Date = auxDate;
		}
		if(hScore2 > hScore1){
			aux = hScore1;
			auxDate = hScore1Date;
			hScore1 = hScore2;
			hScore1Date = hScore2Date;
			hScore2 = aux;
			hScore2Date = auxDate;
		}
		SharedPreferences.Editor editor = highScores.edit();
		editor.putInt("hScore1", hScore1);
		editor.putInt("hScore2", hScore2);
		editor.putInt("hScore3", hScore3);
		editor.putString("hScore1Date", hScore1Date);
		editor.putString("hScore2Date", hScore1Date);
		editor.putString("hScore3Date", hScore1Date);
		editor.commit();
		//Show dialog showing score
		//TODO:Show a trophy icon if high score
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.gameover);
		builder.setMessage(R.string.score)
		       .setCancelable(false)
		       //A button to just quit. Finishes the activity, so the user returns to the main menu
		       .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		                finish();
		           }
		       })
		       //A button to just share score with an external app
		       .setPositiveButton(R.string.shareScore, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //TODO: Implement this. Check if a finish() is necesary
		        	   Toast toast = Toast.makeText(getBaseContext(), R.string.notImplemented, Toast.LENGTH_LONG);
		        	   toast.show();
		           }
		       });
		AlertDialog endGameAlert = builder.create();
		endGameAlert.show();
	}

    /*************************************************/
	/* Checks for filled rows ***********/
	/*************************************************/
	/* Check if some row is filled. If there is some */
	/* it calls to removeRow(), that will remove the */
    /* row and increase score. ***********************/
	/* This function returns a boolean indicating if */
	/* something has been removed, to keep track of  */
	/* the combo multiplier. *************************/
	/*************************************************/
    public boolean lookForRows(){
    	boolean somethingRemoved = false; //To determine if some row has been removed to keep the combo
    	boolean full = true;
    	for (int i = 1; i < 20; i++){
    		full = true;
    		for (int j = 0; j < 10; j++){
    			if (box[i][j].getColor() == Values.COLOR_NONE)
    				full = false;
    		}
    		if (full == true){
    			somethingRemoved = true;
    			//Remove the row. The score is increase here
    			removeRow(i);
    		}
    	}
    	return somethingRemoved;
    }
    
    
    /*************************************************/
	/* Removes the row passed as argument ************/
	/*************************************************/
	/* First increases the score according to the ****/
	/* combo multiplier. Then doubles the combo ******/
    /* multiplier (never higher than 16). Finally it */
	/* moves all the rows above the removed one one **/
	/* position down. ********************************/
	/*************************************************/
    public void removeRow(int row){
    	score = score + Values.SCORE_PER_ROW * combo;
    	textScore.setText(Integer.toString(score));
    	//Setcombo multiplier
    	combo = combo * 2;
    	if (combo == 32)
    		combo = 16;
    	switch (combo){
    		case 2:
    			imageCombo.setImageResource(R.drawable.x2);
    			break;
    		case 4:
    			imageCombo.setImageResource(R.drawable.x4);
    			break;
    		case 8:
    			imageCombo.setImageResource(R.drawable.x8);
    			break;
    		case 16:
    			imageCombo.setImageResource(R.drawable.x16);
    			break;
    	}
    	for (int i = row; i > 1; i--)
    		for (int j = 0; j < 10; j++){
    			box[i][j].setColor(box[i - 1][j].getColor());
    			gameBoard.setColor(i, j, (byte) box[i - 1][j].getColor());
    		}
    }
    
    /*************************************************/
	/* Draws the piece being played ******************/
	/*************************************************/
	/* Draws cubes in the positions occupied by the **/
	/* current piece. Should be called after undraw() /
	/*************************************************/
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
    
    /*************************************************/
	/* Clears the piece being played *****************/
	/*************************************************/
	/* Clears cubes in the positions occupied by the */
	/* current piece. Should be called befors draw() */
	/*************************************************/
    public void unDraw(){
    	for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++){
        		if (currentPiece.box[i][j] == true){
        			box[i][j].setColor(0);
        			gameBoard.setColor(i, j, (byte) 0);
        		}
        	}
    }
}
