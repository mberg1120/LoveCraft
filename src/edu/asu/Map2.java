package edu.asu;

import java.util.ArrayList;
import org.w3c.dom.Node;

/** 
  * Creates and maintains the map in the form of an ArrayList of character arrays. 
  * When set, it creates an effective grid of '0's which indicates unexplored rooms, 
  * and also sets the beginning room to a '2', which indicates the current room.
  * When moving, it changes the previous '2' to a '1' (indicating explored rooms) and sets
  * the destination room to a '2'. When called with the map command, it prints out a map
  * with ASCII wall characters, an x to show the player position, and a ? to show
  * undiscovered rooms that are adjacent or surrounded by explored rooms.
  * 
  * @author Chris
  * 
  */

public class Map2 extends GameObject {
	static int height = 0;
	static int width = 0;
	static int xPos = 0;
	static int yPos = 0;
	static String element = "";
	static String temp;
	static ArrayList<String> map = new ArrayList<String>();
	//Forced constructor
	public Map2 (Node XML) {
		super(XML);
	}
	//Sets the map and starting location
	public static void setup () { //Needs to be called after obtaining height and width of map, which Client pulls from the XML
//		height = Client.x;
//		width = Client.y;
		for (int h = 0; h <= height; h++) {
			for (int w = 0; w <= width; w++) {
				element += '-';
			}
			map.add(element);
			element = "";
		}
	}
	//Gets a map coordinate and puts a 0, or a 2 if that is the starting room
	public static void fillout (String LN, boolean current) { //Map2.fillout(roomList.get(i)._name, roomList.get(i).currentRoom); in Client, in the for-loop with roomList.add
		if(!LN.equalsIgnoreCase("Final"))
		{
			int x = (int)LN.charAt(0)-65;
			int y = Integer.parseInt(LN.substring(1))-1;
			if (!current) {
				temp = map.get(x);
				if (y == 0){
					temp = '0' + temp.substring(y+1);
				} else {
					temp = temp.substring(0, y) + '0' + temp.substring(y+1);
				}
				map.set(x, temp);
			} else {
				temp = map.get(x);
				if (y == 0){
					temp = '2' + temp.substring(y+1);
				} else {
					temp = temp.substring(0, y) + '2' + temp.substring(y+1);
				}
				map.set(x, temp);
				xPos = x;
				yPos = y;
			}
		}
	}
	//Moves the player and updates the map, called during the designated move command
	public static void moveNorth () {
		temp = map.get(xPos);
		if (yPos == 0){
			temp = '1' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '1' + temp.substring(yPos+1);
		}
		map.set(xPos, temp);
		xPos -= 1;
		temp = map.get(xPos);
		if (yPos == 0){
			temp = '2' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '2' + temp.substring(yPos+1);
		}
		map.set(xPos, temp);
	}
	public static void moveEast () {
		temp = map.get(xPos);
		if (yPos == 0){
			temp = '1' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '1' + temp.substring(yPos+1);
		}
		yPos += 1;
		if (yPos == 0){
			temp = '2' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '2' + temp.substring(yPos+1);
		}
		map.set(xPos, temp);
	}
	public static void moveSouth () {
		temp = map.get(xPos);
		if (yPos == 0){
			temp = '1' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '1' + temp.substring(yPos+1);
		}
		map.set(xPos, temp);
		xPos += 1;
		temp = map.get(xPos);
		if (yPos == 0){
			temp = '2' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '2' + temp.substring(yPos+1);
		}
		map.set(xPos, temp);
	}
	public static void moveWest () {
		temp = map.get(xPos);
		if (yPos == 0){
			temp = '1' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '1' + temp.substring(yPos+1);
		}
		yPos -= 1;
		if (yPos == 0){
			temp = '2' + temp.substring(yPos+1);
		} else {
			temp = temp.substring(0, yPos) + '2' + temp.substring(yPos+1);
		}
		map.set(xPos, temp);
	}
	//Prints the map depending on how much is discovered
	public static void display () {
		String output = "";
		//Prepares the top bound of map
		if (map.get(0).charAt(0) == '1' || map.get(0).charAt(0) == '2') {
			output += "┌"; //Adds top-left corner
		} else { //No room
			output += " ";
		}
		for (int w = 0; w < width; w++) {
			if (map.get(0).charAt(w) == '1' || map.get(0).charAt(w) == '2') {
				if (map.get(0).charAt(w+1) == '1' || map.get(0).charAt(w+1) == '2') {
					output += "─┬"; //Adds top wall and top-T wall
				} else {
					output += "─┐"; //Adds top wall and top-right corner (no next room)
				}
			} else if (map.get(0).charAt(w+1) == '1' || map.get(0).charAt(w+1) == '2') {
				output += " ┌"; //Adds space and top-left corner (no previous room)
			} else { //No rooms
				output += "  ";
			}
		}
		output += "\n";
		for (int h = 0; h < height; h++) {
			String firstlayer = "";
			String secondlayer = "";
			//Prepares the middle chunk of map
			if (map.get(h).charAt(0) == '1' || map.get(h).charAt(0) == '2') {
				firstlayer += "│"; //Adds left wall
				if (map.get(h+1).charAt(0) == '1' || map.get(h+1).charAt(0) == '2') {
					secondlayer += "├"; //Adds left-T wall
				} else {
					secondlayer += "└"; //Adds bottom-left corner
				}
			} else { //No room
				firstlayer += " ";
				if (map.get(h+1).charAt(0) == '1' || map.get(h+1).charAt(0) == '2') {
					secondlayer += "┌"; //Adds top-left corner
				} else { //No room again
					secondlayer += " ";
				}
			}
			for (int w = 0; w < width; w++) {
				if (map.get(h).charAt(w) == '1' || map.get(h).charAt(w) == '2') {
					if (map.get(h).charAt(w) == '2') {
						firstlayer += "x"; //You are here!
					} else {
						firstlayer += " "; //You are not here
					}
					if (map.get(h+1).charAt(w) == '-') {
						secondlayer += "─"; //Adds bottom wall
					} else {
						secondlayer += " "; //Doorway
					}
					if (map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') {
						firstlayer += " "; //Doorway
						if ((map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h+1).charAt(w+1) == '1' || map.get(h+1).charAt(w+1) == '2')) {
							secondlayer += "┼"; //Adds 4-way thing
						} else {
							secondlayer += "┴"; //Adds bottom-T wall
						}
					} else {
						if (map.get(h).charAt(w+1) == '-') {
							firstlayer += "│"; //Adds right wall
						} else {
							firstlayer += " "; //Doorway
						}
						if (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') {
							if (map.get(h+1).charAt(w+1) == '1' || map.get(h+1).charAt(w+1) == '2') {
								secondlayer += "┼"; //Adds 4-way thing
							} else {
								secondlayer += "┤"; //Adds right-T wall
							}
						} else {
							secondlayer += "┘"; //Adds bottom-right corner
						}
					}
				} else if (map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') {
					if (map.get(h).charAt(w) == '-') {
						firstlayer += " │"; //Adds left wall
					} else if (map.get(h).charAt(w) == '0') {
						if (w == 0 && h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (w == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						}
					} else {
						firstlayer += "  "; //Adds doorway
					}
					if (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') {
						if (map.get(h).charAt(w) == '-') {
							secondlayer += "─"; //Adds bottom wall
						} else {
							secondlayer += " "; //Doorway
						}
						secondlayer += "┼"; //Adds 4-way thing
					} else if (map.get(h+1).charAt(w+1) == '1' || map.get(h+1).charAt(w+1) == '2') {
						secondlayer += " ├"; //Adds space and left-T wall
					} else {
						secondlayer += " └"; //Adds space and bottom-left corner
					}
				} else if (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') {
					if (map.get(h).charAt(w) == '0') {
						if (w == 0 && h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (w == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						}
					} else {
						firstlayer += "  ";
					}
					if (map.get(h).charAt(w) == '-') {
						secondlayer += "─"; //Adds bottom wall
					} else {
						secondlayer += " "; //Doorway
					}
					if (map.get(h+1).charAt(w+1) == '1' || map.get(h+1).charAt(w+1) == '2') {
						secondlayer += "┬"; //Adds top-T wall
					} else {
						secondlayer += "┐"; //Adds top-right corner
					}
				} else if (map.get(h+1).charAt(w+1) == '1' || map.get(h+1).charAt(w+1) == '2') {
					if (map.get(h).charAt(w) == '0') {
						if (w == 0 && h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (w == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						}
					} else {
						firstlayer += "  ";
					}
					secondlayer += " ┌"; //Adds space and top-left corner
				} else { //No rooms
					if (map.get(h).charAt(w) == '0') {
						if (w == 0 && h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (w == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else if (h == 0) {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						} else {
							if ((map.get(h).charAt(w+1) == '1' || map.get(h).charAt(w+1) == '2') || (map.get(h+1).charAt(w) == '1' || map.get(h+1).charAt(w) == '2') || (map.get(h-1).charAt(w) == '1' || map.get(h-1).charAt(w) == '2') || (map.get(h).charAt(w-1) == '1' || map.get(h).charAt(w-1) == '2')) {
								firstlayer += "? "; //Wonder what's in here!
							} else {
								firstlayer += "  ";
							}
						}
					} else {
						firstlayer += "  ";
					}
					secondlayer += "  ";
				}
			}
			output += firstlayer + "\n";
			output += secondlayer + "\n";
		}
		System.out.println(output);
	}
	//Pure garbage
//	public static void main (String[] args) {
//		setup();
//		fillout("A1",true);
//		fillout("A2",false);
//		fillout("A3",false);
//		fillout("A4",false);
//		fillout("B4",false);
//		fillout("C4",false);
//		fillout("D4",false);
//		fillout("D3",false);
//		fillout("D2",false);
//		fillout("C2",false);
//		fillout("B1",false);
//		fillout("D1",false);
//		fillout("C3",false);
//		fillout("C2",false);
//		fillout("B2",false);
//		fillout("B3",false);
//		moveSouth();
//		moveSouth();
//		moveEast();
//		moveEast();
//		display();
//	}
}
