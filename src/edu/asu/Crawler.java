package edu.asu;
import java.util.ArrayList;
public class Crawler 
{
	RoomObject thisRoom;
	int x = 0;
	int y = 0;
	int maxX;
	int maxY;
	private RoomObject Room;
	ExitObject exit;
	int[][] max = new int[100][100];
	public Crawler(ArrayList roomList)
	{
		Room = (RoomObject) roomList.get(0);
		thisRoom = (RoomObject) roomList.get(0);
	}
	public void currentRoom(RoomObject room)
	{
		thisRoom = room;
	}
	public void findDimensions()
	{
		max[x][y] = 2;
//		if(Room.beenInRoom)
//			max[x][y] = 2;
//		if(Room.currentRoom)
//			max[x][y] = 3;
		toEast();
		CharacterObject.you.setLocation(thisRoom);
	}
	public void userBeenRoom(int isX, int isY)
	{
		if(thisRoom.beenInRoom)
			max[isX][isY] = 2;
		if(thisRoom.currentRoom)
			max[isX][isY] = 3;
	}
	public void callMe()
	{
		maxY = 0;
		for(maxX = 0; maxX <= 11; maxX++)
		{
			if(max[maxX][maxY] > 0)
				if(max[maxX][maxY] > 1)
					if(max[maxX][maxY] > 2)
						System.out.print("[x]");
					else
						System.out.print("[ ]");
				else
					System.out.print("   ");
			else
				System.out.print("   ");
			if(maxX == 10 && maxY != 5)
			{
				maxX = 0;
				maxY++;
				System.out.print("\n");
				if(max[maxX][maxY] > 0)
					if(max[maxX][maxY] > 1)
						if(max[maxX][maxY] > 2)
							System.out.print("[x]");
						else
							System.out.print("[ ]");
					else
						System.out.print("   ");
				else
					System.out.print("   ");
			}
		}				
	}
	private void toEast()
	{
		exit = Room.getExit("East");
		x++;
		max[x][y] = 1;
//		if(Room.beenInRoom)
//			max[x][y] = 2;
//		if(Room.currentRoom)
//			max[x][y] = 3;
		if(exit != null)
		{
			if(max[x+1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toEast();
			}
		}

		exit = Room.getExit("South");
		if(exit != null)
		{
			if(max[x][y+1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toSouth();
			}
		}

		exit = Room.getExit("West");
		if(exit != null)
		{
			if(max[x-1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toWest();
			}
		}
		exit = Room.getExit("North");
		if(exit != null)
		{
			if(max[x][y-1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toNorth();
			}
		}
	}
	private void toSouth()
	{
		exit = Room.getExit("East");
		y++;
		max[x][y] = 1;
//		if(Room.beenInRoom)
//			max[x][y] = 2;
//		if(Room.currentRoom)
//			max[x][y] = 3;		
		if(exit != null)
		{
			if(max[x+1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toEast();
			}
		}

		exit = Room.getExit("South");
		if(exit != null)
		{
			if(max[x][y+1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toSouth();
			}
		}

		exit = Room.getExit("West");
		if(exit != null)
		{
			if(max[x-1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toWest();
			}
		}
		exit = Room.getExit("North");
		if(exit != null)
		{
			if(max[x][y-1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toNorth();
			}
		}
	}
	private void toWest()
	{
		exit = Room.getExit("East");
		x--;
		max[x][y] = 1;
//		if(Room.beenInRoom)
//			max[x][y] = 2;
//		if(Room.currentRoom)
//			max[x][y] = 3;
		if(exit != null)
		{
			if(max[x+1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toEast();
			}
		}

		exit = Room.getExit("South");
		if(exit != null)
		{
			if(max[x][y+1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toSouth();
			}
		}

		exit = Room.getExit("West");
		if(exit != null)
		{
			if(max[x-1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toWest();
			}
		}
		exit = Room.getExit("North");
		if(exit != null)
		{
			if(max[x][y-1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toNorth();
			}
		}
	}
	private void toNorth()
	{
		exit = Room.getExit("East");
		y--;
		max[x][y] = 1;
//		if(Room.beenInRoom)
//			max[x][y] = 2;
//		if(Room.currentRoom)
//			max[x][y] = 3;
		if(exit != null)
		{
			if(max[x+1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toEast();
			}
		}

		exit = Room.getExit("South");
		if(exit != null)
		{
			if(max[x][y+1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toSouth();
			}
		}

		exit = Room.getExit("West");
		if(exit != null)
		{
			if(max[x-1][y] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toWest();
			}
		}
		exit = Room.getExit("North");
		if(exit != null)
		{
			if(max[x][y-1] == 0)
			{
				CharacterObject.you.setLocation(exit.destination());
				Room = (RoomObject) CharacterObject.you.location();
				toNorth();
			}
		}
	}
}
