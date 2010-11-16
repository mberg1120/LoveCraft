package edu.asu;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Node;
public class Map extends GameObject
{
	public Map(Node XML) {
		super(XML);
	}
	public static void setMap(ArrayList roomList)
	{

		RoomObject currentRoom = (RoomObject) roomList.get(0);
		RoomObject holderRoom = (RoomObject) CharacterObject.you.location();
		CreatureObject person = CharacterObject.you;
		person.setLocation(currentRoom);
//		System.out.print("[ ]");
		ExitObject exit = currentRoom.getExit("East");
		for(int count =  0; count < roomList.size(); count++)
		{
			if(person._location.beenInRoom == false)
				System.out.print("   ");
			if(person._location.currentRoom == true)
			{
				System.out.print("[x]");
			}
			else if(person._location.beenInRoom == true && person._location.currentRoom == false)
			{
				System.out.print("[ ]");
			}
			if(exit == null || exit.destination() == null)
			{
				exit = currentRoom.getExit("South");
				if(exit != null)
				{
					person.setLocation(exit.destination());
					currentRoom = (RoomObject) person.location();
					System.out.print("\n");
					if(person._location.beenInRoom == false)
						System.out.print("   ");
					else if(person._location.beenInRoom == true && person._location.currentRoom == false)
						System.out.print("[ ]");
					else if(person._location.currentRoom == true)
						System.out.print("[x]");
						
					exit = currentRoom.getExit("East");
				}
				else if(exit == null)
				{
					person.setLocation(holderRoom);
					return;
				}
			}
			if(exit != null)
			{
				person.setLocation(exit.destination());
				exit = person._location.getExit("East");
			}
		}
		person.setLocation(holderRoom);
		
	}


}