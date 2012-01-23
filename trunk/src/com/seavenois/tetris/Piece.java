package com.seavenois.tetris;

import java.util.Random;

import android.util.Log;

public class Piece {
	
	Random generator;
	byte type;
	byte color;
	boolean[][] box;
	boolean board[][];
	boolean[][] aux;
	byte rotation;
	
	public byte getColor(){
		return color;
	}
	
	Piece(){
		generator = new Random();
		type = (byte) generator.nextInt(7);
		//type = 0;//TESTING
		color = (byte) (type + 1);
		rotation = 0;
		box = new boolean[20][10];
		board = new boolean[20][10];
		for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++)
        		box[i][j] = false;
	}
	
	public void start(){
		switch (type){
		case Values.PIECE_0:
			box[0][3] = true;
			box[0][4] = true;
			box[0][5] = true;
			box[0][6] = true;
			break;
		case Values.PIECE_1:
			box[0][4] = true;
			box[1][4] = true;
			box[1][5] = true;
			box[1][6] = true;
			break;
		case Values.PIECE_2:
			box[0][5] = true;
			box[1][3] = true;
			box[1][4] = true;
			box[1][5] = true;
			break;
		case Values.PIECE_3:
			box[0][4] = true;
			box[0][5] = true;
			box[1][4] = true;
			box[1][5] = true;
			break;
		case Values.PIECE_4:
			box[0][4] = true;
			box[0][5] = true;
			box[1][3] = true;
			box[1][4] = true;
			break;
		case Values.PIECE_5:
			box[0][4] = true;
			box[0][5] = true;
			box[1][5] = true;
			box[1][6] = true;
			break;
		case Values.PIECE_6:
			box[0][4] = true;
			box[1][3] = true;
			box[1][4] = true;
			box[1][5] = true;
			break;
		}
	}
	
	public void readBoard(Box a[][]){
		for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++)
        		if (a[i][j].getColor() == Values.COLOR_NONE)
        			board[i][j] = false;
        		else
        			board[i][j] = true;
	}
	
	public boolean moveDown(){
		aux = new boolean[20][10];
		for (int j = 0; j < 10; j++)
			if (box[19][j] == true)
				return false;
		for (int i = 0; i < 19; i++)			
        	for (int j = 0; j < 10; j++){
        		if (box[i][j] == true){
        			if (board[i + 1][j] == true)        				
        				return false;        			
        			else
        				aux[i + 1][j] = true;
        		}
        		else
        			aux[i + 1][j] = false;
        	}
		for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++)
        		box[i][j] = aux[i][j];
		return true;
	}
	
	public boolean moveLeft(){
		aux = new boolean[20][10];
		for (int i = 0; i < 20; i++)
        	for (int j = 1; j < 10; j++){
        		if (box[i][j] == true){
        			if (box[i][0] == true)
        				return false;
        			if (board[i][j - 1] == true)
        				return false;
        			else
        				aux[i][j - 1] = true;
        		}
        		else
        			aux[i][j - 1] = false;
        	}
		for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++)
        		box[i][j] = aux[i][j];
		return true;
	}
	
	public boolean moveRight(){
		aux = new boolean[20][10];
		for (int i = 0; i < 19; i++)
        	for (int j = 0; j < 9; j++){
        		if (box[i][j] == true){
        			if (box[i][9] == true)
        				return false;
        			if (board[i][j + 1] == true)
        				return false;
        			else
        				aux[i][j + 1] = true;
        		}
        		else
        			aux[i][j + 1] = false;
        	}
		for (int i = 0; i < 20; i++)
        	for (int j = 0; j < 10; j++)
        		box[i][j] = aux[i][j];
		return true;
	}
	
	public boolean rotateRight(){
		int i = 0;
		int j = 0;
		rotation++;
		if (rotation == 4)
			rotation = 0;
		switch (type){
			case Values.PIECE_0:
				//Switch new rotation state
				switch (rotation){
					case 1: //From horizontal to vertical
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (i == 0)
							return false;
						if (board[i - 1][j + 2] == true)
							return false;
						if (board[i + 1][j + 2] == true)
							return false;
						if (board[i + 2][j + 2] == true)
							return false;
						//Perform transformation
						box[i - 1][j + 2] = true;
						box[i + 1][j + 2] = true;
						box[i + 2][j + 2] = true;
						box[i][j] = false;
						box[i][j + 1] = false;
						box[i][j + 3] = false;
						break;
					case 2: //From vertical to horizontal
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (board[i + 1][j - 2] == true)
							return false;
						if (board[i + 1][j - 1] == true)
							return false;
						if (board[i + 1][j + 1] == true)
							return false;
						//Perform transformation
						box[i + 1][j - 2] = true;
						box[i + 1][j - 1] = true;
						box[i + 1][j + 1] = true;
						box[i][j] = false;
						box[i + 2][j] = false;
						box[i + 3][j] = false;
						break;
					case 3: //From horizontal to vertical
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (i == 0)
							return false;
						if (board[i - 1][j + 2] == true)
							return false;
						if (board[i + 1][j + 2] == true)
							return false;
						if (board[i + 2][j + 2] == true)
							return false;
						//Perform transformation
						box[i - 1][j + 2] = true;
						box[i + 1][j + 2] = true;
						box[i + 2][j + 2] = true;
						box[i][j] = false;
						box[i][j + 1] = false;
						box[i][j + 3] = false;
						break;
					case 0: //From vertical to horizontal
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (board[i + 1][j - 2] == true)
							return false;
						if (board[i + 1][j - 1] == true)
							return false;
						if (board[i + 1][j + 1] == true)
							return false;
						//Perform transformation
						box[i + 1][j - 2] = true;
						box[i + 1][j - 1] = true;
						box[i + 1][j + 1] = true;
						box[i][j] = false;
						box[i + 2][j] = false;
						box[i + 3][j] = false;
						break;
				}
				break;
			case Values.PIECE_1:
				//Switch new rotation state
				switch (rotation){
					case 1: //From horizontal left-side-up to vertical top-side-right
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (board[i][j + 1] == true)
							return false;
						if (board[i][j + 2] == true)
							return false;
						if (board[i + 2][j + 1] == true)
							return false;
						//Perform transformation
						box[i][j + 1] = true;
						box[i][j + 2] = true;
						box[i + 2][j + 1] = true;
						box[i][j] = false;
						box[i + 1][j] = false;
						box[i + 1][j + 2] = false;
						break;
					case 2: //From vertical top-side-right to horizontal right-side-down
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (board[i][j - 1] == true)
							return false;
						if (board[i + 1][j + 1] == true)
							return false;
						//Perform transformation
						box[i][j - 1] = true;
						box[i + 1][j + 1] = true;
						box[i + 1][j] = false;
						box[i + 2][j] = false;
						break;
					case 3: //From horizontal left-side-down to vertical down-side-left
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (board[i + 2][j + 1] == true)
							return false;
						if (board[i + 2][j + 2] == true)
							return false;
						//Perform transformation
						box[i + 2][j + 1] = true;
						box[i + 2][j + 2] = true;
						box[i][j] = false;
						box[i][j + 1] = false;
						break;
					case 0: //From vertical down-side-left to horizontal left-side-up
						//Find the first occupied box
						while (box[i][j] == false){
							j++;
							if (j == 9){
								j = 0;
								i++;
							}
						}
						//Check availability
						if (board[i][j - 2] == true)
							return false;
						if (board[i + 1][j - 2] == true)
							return false;
						if (board[i + 1][j - 1] == true)
							return false;
						//Perform transformation
						box[i][j - 2] = true;
						box[i + 1][j - 2] = true;
						box[i + 1][j - 1] = true;
						box[i][j] = false;
						box[i + 2][j - 1] = false;
						box[i + 2][j] = false;
						break;
				}
				break;
		}
		return true;
	}
}
