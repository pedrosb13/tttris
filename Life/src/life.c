/***************************************************************************
 *   Copyright (C) 2012 by Iñigo Valentin Villate                          *
 *   seavenois@gmail.com                                                   *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/


#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

//Headers
#include <stdio.h>
#include <stdlib.h>
#include <SDL.h>
#include <ctype.h>
#include <string.h>
#include "SDL_mixer.h"
#include "SDL_ttf.h"
#include "SDL_image.h"

//Measures
#define XRES 800
#define YRES 600
#define VERBOSE 0

//Main bucle
int done=0;
int sleepTime;
int speed_text;

//Game measures
int rows, columns, rows_text, columns_text;

//SDL variables
SDL_Surface *screen;
TTF_Font *font;
SDL_Event event;
Uint8 *keys;
SDL_Color bgcolor, fgcolor;
SDL_Surface *no_cell, *cell, *menu_set, *menu_game;
SDL_Rect box[100][100], menu;
char box_state[100][100]; //0:free, 1:occupied
char game_state; //s: setting, r:running, p:paused

//Text variables
SDL_Surface *textRows;
SDL_Surface *textColumns;
SDL_Rect textRowsLocation, textColumnsLocation;

//File to save
FILE *file;

//Sounds
int effectschannel, musicchanel;
Mix_Chunk *beep, *click;

void draw(){
	int i, j;
	char buffer[3];
	Uint32 color;
	color = SDL_MapRGB(screen->format,200,200,200);
	SDL_FillRect(screen, &menu, color);
	if(game_state == 's'){
		SDL_BlitSurface(menu_set, NULL, screen, &menu);
		buffer[3] = "   ";
		sprintf(buffer, "%d", rows_text);
		textRows = TTF_RenderText_Shaded(font, buffer, fgcolor, bgcolor);
		SDL_BlitSurface(textRows, NULL, screen, &textRowsLocation);
		buffer[3] = "   ";
		sprintf(buffer, "%d", columns_text);
		textColumns = TTF_RenderText_Shaded(font, buffer, fgcolor, bgcolor);
		SDL_BlitSurface(textColumns, NULL, screen, &textColumnsLocation);
	}
	if(game_state == 'r' || game_state == 'p'){
		SDL_BlitSurface(menu_game, NULL, screen, &menu);
		buffer[3] = "   ";
		sprintf(buffer, "%d", speed_text);
		textRows = TTF_RenderText_Shaded(font, buffer, fgcolor, bgcolor);
		SDL_BlitSurface(textRows, NULL, screen, &textRowsLocation);
	}
	for(i = 0; i < columns; i++)
		for(j = 0; j < rows; j++){
			if (box_state[i][j] == '0')
				SDL_BlitSurface(no_cell, NULL, screen, &box[i][j]);
			else
				SDL_BlitSurface(cell, NULL, screen, &box[i][j]);
		}
	SDL_Flip(screen);
}

int initSDL(){
	if (SDL_Init(SDL_INIT_VIDEO|SDL_INIT_AUDIO) < 0) {
		printf("Couldn init SDL: %s\n",SDL_GetError());
		exit(1);
	}
	//Init SDL_ttf
	if(TTF_Init()<0){
		printf("Couldn init SDL_ttf: %s\n", SDL_GetError());
		return(1);
	}
	atexit(TTF_Quit);
	//Init SDL_Mixer
	if(Mix_OpenAudio(22050, AUDIO_S16, 2, 4096)) {
		printf("Couldn init SDL_mixer %s\n",Mix_GetError());
		exit(1);
	}
	atexit(Mix_CloseAudio);
	click=Mix_LoadWAV("../snd/click.wav");
	//Load text font
	font=TTF_OpenFont("fonts/DejaVuSerif.ttf", 20);
	//Colors for text
	fgcolor.r=0;
	fgcolor.g=0;
	fgcolor.b=0;
	bgcolor.r=200;
	bgcolor.g=200;
	bgcolor.b=200;
	textRowsLocation.x = 53;
	textRowsLocation.y = 28;
	textRowsLocation.w = 18;
	textRowsLocation.h = 18;
	textColumnsLocation.x = 100;
	textColumnsLocation.y = 28;
	textColumnsLocation.w = 18;
	textColumnsLocation.h = 18;
	// Init Video mode
	if (columns > 25)
		screen = SDL_SetVideoMode(20 * columns, 20 * rows + 80, 24, SDL_ASYNCBLIT);
	else
		screen = SDL_SetVideoMode(500, 20 * rows + 80, 24, SDL_ASYNCBLIT);
	keys=SDL_GetKeyState(NULL);
	//Set images
	no_cell = IMG_Load("img/box.png");
	if (no_cell == NULL ) {
		fprintf(stderr, "Couldn't load %s: %s\n", file, SDL_GetError());
		return;
    }
    cell = IMG_Load("img/cell.png");
	if (cell == NULL ) {
		fprintf(stderr, "Couldn't load %s: %s\n", file, SDL_GetError());
		return;
    }
    menu_set = IMG_Load("img/menu_set.png");
	if (menu_set == NULL ) {
		fprintf(stderr, "Couldn't load %s: %s\n", file, SDL_GetError());
		return;
    }
    menu_game = IMG_Load("img/menu_game.png");
	if (menu_game == NULL ) {
		fprintf(stderr, "Couldn't load %s: %s\n", file, SDL_GetError());
		return;
    }
}

void add_random(int total){
	int i = 0, j, f = 0;
	//Count free boxes, to see if they fit
	for(i = 0; i < columns; i++)
		for(j = 0; j < rows; j++)
			if (box_state[i][j] == '0')
				f++;
	if (f < total){
		return;
	}
	f = 0;
	while (f < total){
		srand (time(NULL));
		i = rand() % columns;
		j = rand() % rows;
		while (box_state[i][j] == '1'){
			i = rand() % columns;
			j = rand() % rows;
		}
		box_state[i][j] = '1';
		f++;
	}
	draw();
	return;
}

void board_setting(){
	SDL_Event event;
	int i, j;
	while (game_state == 's'){
		while (SDL_PollEvent(&event) ) {
			switch (event.type) {
				case SDL_MOUSEBUTTONDOWN:
					if(event.button.y > 80){ //If click in the board
						i = event.button.x / 20;
						j = event.button.y / 20 - 4;
						if (box_state[i][j] == '0'){
							box_state[i][j] = '1';
							SDL_BlitSurface(cell, NULL, screen, &box[i][j]);
							SDL_UpdateRects(screen, 1, &box[i][j]);
						}
						else{
							box_state[i][j] = '0';
							SDL_BlitSurface(no_cell, NULL, screen, &box[i][j]);
							SDL_UpdateRects(screen, 1, &box[i][j]);
						}
					}
					else{ //If click in the menu
						if (event.button.x > 46 && event.button.y > 5 && event.button.x < 87 && event.button.y < 19){ //More rows
							rows_text++;
							if(rows_text == 100)
								rows_text--;
							draw();
						}
						if (event.button.x > 49 && event.button.y > 55 && event.button.x < 90 && event.button.y < 69){ //Less rows
							rows_text--;
							if(rows_text == 2)
								rows_text++;
							draw();
						}
						if (event.button.x > 93 && event.button.y > 5 && event.button.x < 137 && event.button.y < 19){ //More columns
							columns_text++;
							if(columns_text == 100)
								columns_text--;
							draw();
						}
						if (event.button.x > 96 && event.button.y > 55 && event.button.x < 140 && event.button.y < 69){ //Less columns
							columns_text--;
							if(columns_text == 2)
								columns_text++;
							draw();
						}
						if (event.button.x > 144 && event.button.y > 24 && event.button.x < 246 && event.button.y < 53){ //Regenerate
							columns = columns_text;
							rows = rows_text;
							initSDL();
							for(i = 0; i < columns; i++)
								for(j = 0; j < rows; j++){
									box[i][j].x = 20 * i;
									box[i][j].y = 20 * j + 80;
									box[i][j].w = 20;
									box[i][j].h = 20;
									box_state[i][j] = '0';
								}
							draw();
						}
						if (event.button.x > 261 && event.button.y > 19 && event.button.x < 303 && event.button.y < 54) //add 5
							add_random(5);
						if (event.button.x > 303 && event.button.y > 19 && event.button.x < 344 && event.button.y < 54) //add 10
							add_random(10);
						if (event.button.x > 344 && event.button.y > 19 && event.button.x < 384 && event.button.y < 54) //add 20
							add_random(20);
						if (event.button.x > 404 && event.button.y > 19 && event.button.x < 485 && event.button.y < 54){ //start
							game_state = 'r';
							game();
							return;
						}
					}
					break;
				case SDL_QUIT:
					exit(0);
			}
		}
	}
}

int get_surrounding(int i, int j){
	int surrounding;
	surrounding = 0;
	if (i > 0 && j > 0)
		if (box_state[i - 1][j - 1] == '1')
			surrounding++;
	if (i > 0)
		if (box_state[i - 1][j] == '1')
			surrounding++;
	if (i > 0 && j < rows - 1)
		if (box_state[i - 1][j + 1] == '1')
			surrounding++;
	if (j > 0)
		if (box_state[i][j - 1] == '1')
			surrounding++;
	if (j < rows - 1)
		if (box_state[i][j + 1] == '1')
			surrounding++;
	if (i < columns - 1 && j > 0)
		if (box_state[i + 1][j - 1] == '1')
			surrounding++;
	if (i < columns - 1)
		if (box_state[i + 1][j] == '1')
			surrounding++;
	if (i < columns - 1 && j < rows - 1)
		if (box_state[i + 1][j + 1] == '1')
			surrounding++;
	return surrounding;
}

void game(){
	int i, j, surrounding;
	char aux_state[100][100];
	SDL_Event event;
	for (i = 0; i < columns; i++)
		for (j = 0; j < rows; j++)
			aux_state[i][j] = '0';
	while (game_state == 'r' || game_state == 'p'){
		if (game_state == 'r'){
			for (i = 0; i < columns; i++)
				for (j = 0; j < rows; j++){
					surrounding = get_surrounding(i, j);
					if (box_state[i][j] == '1' && surrounding == 2)
						aux_state[i][j] = '1';
					if (surrounding == 3)
						aux_state[i][j] = '1';
					if (surrounding >= 4)
						aux_state[i][j] = '0';
					if (surrounding < 2)
						aux_state[i][j] = '0';					
				}
			for (i = 0; i < columns; i++)
				for (j = 0; j < rows; j++)
					box_state[i][j] = aux_state[i][j];
			//Sleep
			struct timespec req = {0};
			req.tv_sec = 0;
			req.tv_nsec = sleepTime * 1000000L;
			nanosleep(&req, (struct timespec *)NULL);
			draw();
		}
		while (SDL_PollEvent(&event) ) {
			switch (event.type) {
				case SDL_MOUSEBUTTONDOWN:
					if (event.button.x > 49 && event.button.y > 55 && event.button.x < 90 && event.button.y < 69){ //Less speed
						speed_text--;
						sleepTime = sleepTime + 100;
						if(speed_text == 0){
							speed_text++;
							sleepTime = sleepTime - 100;
						}
						draw();
					}
					if (event.button.x > 46 && event.button.y > 5 && event.button.x < 87 && event.button.y < 19){ //More speed
						speed_text++;
						sleepTime = sleepTime - 100;
						if(speed_text == 10){
							speed_text--;
							sleepTime = sleepTime + 100;
						}
						draw();
					}
					if (event.button.x > 134 && event.button.y > 25 && event.button.x < 233 && event.button.y < 51){ //Pause, resume
						if (game_state == 'r')
							game_state = 'p';
						else
							game_state = 'r';
						draw();
					}
					if (event.button.x > 293 && event.button.y > 24 && event.button.x < 492 && event.button.y < 52){ //Stop. Back to config
						game_state = 's';
						draw();
						board_setting();
						return;
					}
					break;
				case SDL_QUIT:
					exit(0);
			}
		}
	}
}

int main(int argc, char *argv[]) {
	rows = 20;
	columns = 50;
	sleepTime = 500;
	rows_text = rows;
	speed_text = 5;
	columns_text = columns;
	int i, j;
	//Assign menu psitions
	menu.x = 0;
	menu.y = 0;
	menu.w = 500;
	menu.h = 80;
	//Assign box positions and set state of every box to 0
	for(i = 0; i < columns; i++)
		for(j = 0; j < rows; j++){
			box[i][j].x = 20 * i;
			box[i][j].y = 20 * j + 80;
			box[i][j].w = 20;
			box[i][j].h = 20;
			box_state[i][j] = '0';
		}
	initSDL();
	atexit(SDL_Quit);
	game_state = 's';
	draw();
	board_setting();
	return 0;
}






