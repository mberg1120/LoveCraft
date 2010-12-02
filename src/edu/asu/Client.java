package edu.asu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Scanner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.audio.*;
import java.io.*;

public class Client {

	/**
	 * This is the main for the program.  It calls the XML parser and the methods of the other classes
	 * to render the map for the game.  It also accepts input from the user and prints descriptions of
	 * the game-state to the console.
	 * With some added things it now writes the users input to a text file. 
	 * @param args
	 * @author z
	 */
	static int x = 0, y = 0; //For the map
//	private static Crawler mapCrawler;
	public static void main(String[] args) 
	{
//	static boolean MusicOn = true;
		Music music = new Music();
		Thread music_thread = new Thread(music);
		x = 0;
		y = 0;
		ArrayList roomList = new ArrayList();
		String input;
		String descript;
		//step 1: Load XML file
		Element gameFile = parseXMLFile("Dungeon.xml");
		//step 2: Run through the XML file, and Build the map
		String y1 = Client.getXMLElement(gameFile, "Height");
		String x1 = Client.getXMLElement(gameFile, "Width");

	    int maxX = Integer.parseInt(x1.trim());
	    int maxY = Integer.parseInt(y1.trim());
		Map2.height = maxY;
		Map2.width = maxX;
		Map2.setup();
		NodeList parseRoom = Client.getXMLNodes(gameFile, "Room");
		if(parseRoom != null && parseRoom.getLength() > 0)
			for(int i = 0; i < parseRoom.getLength(); i++){
//				System.out.print((i+1) + ": ");
				roomList.add(new RoomObject(parseRoom.item(i)));
				Map2.fillout(((GameObject)roomList.get(i))._name, ((GameObject)roomList.get(i)).currentRoom);
//				System.out.println("");
			}
//		for(int i = 0; i < roomList.size(); i++)
		Scanner scan = new Scanner(System.in);
		((GameObject)roomList.get(0)).currentRoom = true;
		Map2.fillout(((GameObject)roomList.get(0))._name, ((GameObject)roomList.get(0)).currentRoom);
		FileOutputStream logger;
		PrintStream logged;
		try {
				logger = new FileOutputStream("gamelog.txt", true);
				logged = new PrintStream(logger);
		} catch (FileNotFoundException e) {
				System.err.println("file could not be read for some reason");
				e.printStackTrace();
				return;
		}
		Date now = new Date();
		GameObject room = null;
		RoomObject tempRoom;
		logged.println("==========| " + now.toString() + " |==========");
//		mapCrawler = new Crawler(roomList);
//		mapCrawler.findDimensions();
//		mapCrawler.maxX = maxX;
//		mapCrawler.maxY = maxY;
		music_thread.start();
			
		do {
//			if(Music.AudioPlayer() == false)
//				Music.music();
			if(CharacterObject.you.location().equals(room)) {
				CreatureObject monster;
				for(Iterator<CreatureObject> i 	= room.creatures().iterator(); i.hasNext(); ){
					monster = i.next();
					System.out.print(monster.think());
				}
			}
			else
			{
				tempRoom = (RoomObject)room;
				room = CharacterObject.you.location();
				room.currentRoom = true;
//				mapCrawler.userBeenRoom(x, y);
				if(room._name.equalsIgnoreCase("Final"))
				{
					if(room.isLit())
					{
						if(!room.isFirst)
							System.out.println(room.description("second"));
						else
							System.out.println(room.description(null));
						do
						{
							input = scan.next();
							if(input.equalsIgnoreCase("Yes") || input.equalsIgnoreCase("Y"))
							{
								System.out.println(room.description("final"));
//								Music.stop();
								System.exit(0);
							}
							else if (input.equalsIgnoreCase("No") || input.equalsIgnoreCase("n"))
							{
								room.isFirst = false;
								CharacterObject.you.setLocation(tempRoom);
								room = CharacterObject.you.location();
								System.out.println("You are back in the previous room.");
								input = scan.nextLine();
							}	
							else
								System.out.println("Type [Y]es or [No].");
						}while(room._name.equalsIgnoreCase("Final"));
					}
					else
					{
						System.out.println("I'm sorry, I can't let you do that Dave.");
						System.out.println("Yeah, and I put you back in the previous room.");
						CharacterObject.you.setLocation(tempRoom);
						room = CharacterObject.you.location();
					}
				}
				if(room.isFirst == true)
				{
					descript = room.description(null);
//				String[] descript2 = descript.split("\n");
					System.out.println(room._description);
//				System.out.println(descript);
					for(int i = 0; i < room.inventory().size(); i++)
					{
						if(room.inventory().get(i) instanceof CreatureObject)
							System.out.println(room.inventory().get(i).description(null));
						if(room.inventory().get(i) instanceof ExitObject && room.inventory().get(i).isLit())
							System.out.println(room.inventory().get(i).description(null));
						if(room.inventory().get(i).isOnFire)
							System.out.println(room.inventory().get(i).description("Fire"));
						else if(room.inventory().get(i) instanceof ItemObject)
							System.out.println(room.inventory().get(i).description(null));
					}
				}
				else
				{				
					descript = room.description("Second");
//					String[] descript2 = descript.split("\n");
					System.out.println(room._description);
//					System.out.println(descript);
					for(int i = 0; i < room.inventory().size(); i++)
					{
						if(room.inventory().get(i) instanceof CreatureObject)
							System.out.println(room.inventory().get(i).description(null));
						if(room.inventory().get(i) instanceof ExitObject && room.inventory().get(i).isLit())
							System.out.println(room.inventory().get(i).description(null));
						if(room.inventory().get(i).isOnFire)
							System.out.println(room.inventory().get(i).description("Fire"));
						else if(room.inventory().get(i) instanceof ItemObject)
							System.out.println(room.inventory().get(i).description(null));
					}
//					System.out.println(room.description("Second"));
				}
				if(CharacterObject.you.equipped() != null)
					System.out.println("You are holding a " + CharacterObject.you.equipped().name());
			}
			if(CharacterObject.you.getStatus().equalsIgnoreCase("Dead"))
			{
				boolean keepGoing = true;
				System.out.println("You have died! You must either [Q]uit or [R]estart. What do you wish to do?");
				do{
					input = scan.nextLine();
					if(input.equalsIgnoreCase("R") || input.equalsIgnoreCase("Restart"))
					{
						Music.stop();
						System.out.println("Your adventure will be restarted in 5 seconds!");
						try {
							Thread.sleep(5000);
						}catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						Client.main(null);
						keepGoing = false;
					}
					else if(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))
					{
						System.out.println("Good luck next time!");
						keepGoing = false;
					}
					else
						System.out.println("Since you are dead, I do not understand " + input 
								+ ".\nYou must either [Q]uit or [R]estart.");
				}while(keepGoing == true);
				System.exit(0);
			}
//			System.out.println(CharacterObject.you.location().name() + ">");
			input = null;
			try{
				input = scan.nextLine();
				logged.println(input);
				System.out.println(CharacterObject.you.doVerb(null, input));
			}catch(NoSuchElementException e)
			{}catch(NullPointerException e){}
		} while(!input.equalsIgnoreCase("Quit"));
		try {
			logger.close();
//			Music.stop();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Something went wrong.");   //change that message
			e.printStackTrace();
			return;
		}
	}
	/**
	 * This class reads in the XML file and parses through the document to set up the information for
	 * being pulled later.
	 * @param _fileName
	 * @author Michael Berg
	 */
	public static Element parseXMLFile(String _fileName){
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(_fileName);
			return dom.getDocumentElement();
		} catch(ParserConfigurationException pce) {
//			pce.printStackTrace();
		} catch(NullPointerException pce) {
//			pce.printStackTrace();
		} catch(SAXException se){
//			se.printStackTrace();
			System.out.println("SAXException... Nice fail.\n");
		} catch(IOException ioe){
//			ioe.printStackTrace();
		}
		return null;
	}
	/**
	 * This method goes through an XML node to pull the element information or the attribute
	 * Information for the tag name that is passed to this method.
	 * @param XML
	 * @param tagName
	 * @author Michael Berg
	 */
	public static String getXMLElement(Node XML, String tagName){
		NodeList nl = getXMLNodes(XML, tagName);
		Element el;
		if(nl == null || nl.getLength() == 0 ) // If the node has no contents return its attribute value instead
		{
			el = (Element)XML;
			return el.getAttribute(tagName);
		}
		for(int i = 0; i < nl.getLength(); i++)
		{  //needs to be broken down and given try blocks		
			
			el = (Element)getXMLNodes(XML, tagName).item(i);
			if(XML.equals(el.getParentNode()))
					return el.getFirstChild().getNodeValue(); // found one!
		}
		return ""; 
	}
	/**
	 * This method is called when something needs to be pulled from the parsed XML file.  
	 * It returns the node that is the String that is being passed to this method.
	 * @param XML
	 * @param NodeType
	 * @author Michael Berg
	 */
	public static NodeList getXMLNodes(Node XML, String NodeType){
		Element XMLData = (Element)XML;
		return XMLData.getElementsByTagName(NodeType); 
	}
	public static void commandList()
	{
		System.out.println("Command List:");
		for(int i = 0; i < CharacterObject.you.verbs().size(); i++)
//			if(CharacterObject.you.verbs().get(i).matches("C") ||)
			System.out.println("-" + CharacterObject.you._verbs.get(i).getVerb());
	}
	/**This method takes in the subject and object of a translated verb statement.  From there it
	 * will perform the necessary steps needed to carry out that user inputed verb command.
	 * @param gSubject
	 * @param gObject
	 * @param translation
	 * @author Michael Berg
	 */
	@SuppressWarnings("deprecation")
	public static String doVerb(GameObject gSubject, GameObject gObject, String translation) 
	{
		CharacterObject gActor;
		GameObject target;
		String[] userInput = translation.split(" ");
/*		DEBUG PRINT
 		if(gObject == null)
			System.out.println(gSubject.name() + ":" + translation);
		else if(gSubject == null)
			System.out.println("??->" + gObject.name() + ":" + translation);
		else
			System.out.println(gSubject.name() + "->" + gObject.name() + ":" + translation);
*/	
		
		if(userInput[0].equalsIgnoreCase("Quit")){ // "Quit", ends the game and terminates the program
			System.out.println("Goodbye!");

//			Music.stop();
			return "";
		}
		else if(userInput[0].equalsIgnoreCase("Go"))
		{ 
			CharacterObject.you.location().currentRoom = false;
			// "Go @Exit", where @Exit is the name of a Exit
			if(CharacterObject.you._location.isLit())
				CharacterObject.you._location.isFirst = false;
			if(userInput.length < 2)
				return "Go where?";
			else 
			{
				RoomObject room = (RoomObject)gObject; // gObject had better be a room Object
				ExitObject exit = room.getExit(userInput[1]);
				if(exit == null || exit.destination() == null)
					System.out.println("You can't go that way.");
				else
				{
//					mapCrawler.max[x][y] = 2;
					CreatureObject you = (CreatureObject)gSubject;
					you.setLocation(exit.destination());
					String response = "You go " + userInput[1] + ".\n";
					room.beenInRoom = true;
					room.currentRoom = true;
					if(exit.destination().name().equalsIgnoreCase("Final") == false)
					{
						if(userInput[1].equalsIgnoreCase("North"))
							Map2.moveNorth();
						else if(userInput[1].equalsIgnoreCase("South"))
							Map2.moveSouth();
						else if(userInput[1].equalsIgnoreCase("East"))
							Map2.moveEast();
						else if(userInput[1].equalsIgnoreCase("West"))
							Map2.moveWest();
					}
					System.out.print(response);
				}
			}		
			
		}
//		else if(userInput[0].equalsIgnoreCase("Music") && userInput[1].equalsIgnoreCase("On"))
//		{
//			if(MusicOn == true)
//				System.out.println("Music is already running.");
//			else
//			{
//				Music.music();
//				MusicOn = true;
//			}
//		}
//		else if(userInput[0].equalsIgnoreCase("Music") && userInput[1].equalsIgnoreCase("Off"))
//		{
//			if(MusicOn == true)
//			{
//				Music.stop();
//				MusicOn = false;
//			}
//			else
//				System.out.println("There isn't any music to stop...");
//		}
		else if(userInput[0].equalsIgnoreCase("Map") || userInput[0].equalsIgnoreCase("M"))
		{
			if(CharacterObject.you.equipped() != null)
			{
				if(CharacterObject.you.equipped().isLight())
					Map2.display();
				else
					System.out.println("You don't have a light source equipped, so you can't see the map.");
			}
			else
				System.out.println("You can't look at your map without a light source...");
		}
		else if(userInput[0].equalsIgnoreCase("Command") || userInput[0].equalsIgnoreCase("C") || userInput[0].equalsIgnoreCase("Commands"))
		{
			commandList();
		}
		else if(userInput[0].equalsIgnoreCase("Look")){ // "Look ...", a series of commands (see below)			
			if(gSubject instanceof CharacterObject)
				gActor = (CharacterObject)gSubject;
			else if(gObject instanceof CharacterObject)
				gActor = (CharacterObject)gObject;
			else
				return "You have no eyes!!!";
			if(userInput.length < 2 || userInput[1].equalsIgnoreCase("Room"))
				System.out.println(gActor.location().description("Look"));
//			else if(userInput.length == 2){ //Look @Exit
//				RoomObject room = (RoomObject)gObject; // gObject had better be a room Object
//				ExitObject exit = room.getExit(userInput[1]);
//				if(exit == null || exit.destination() == null)
//					return "There is nothing in that direction.";
//				else{
//					System.out.println(exit.destination().description(null));
//				}
//			}
			else if(userInput.length == 3){ // Look at @Item
				ItemObject item = (ItemObject)gObject;
				System.out.println(item.description("Look"));
			}
			else
				System.out.println("What are you trying to look at?");
		}
		else if(userInput[0].equalsIgnoreCase("I") || userInput[0].equalsIgnoreCase("Inventory"))
		{
			return CharacterObject.you.inventoryContents();
		}
		else if(userInput[0].equalsIgnoreCase("Take") || userInput[0].equalsIgnoreCase("Grab")){ // "Take @Item", removes the declared item from a rooms inventory and puts it into the players inventory
			if(userInput.length < 2)
				System.out.println("Take what?");		
			else 
			{
				if(gSubject instanceof CharacterObject)
					gActor = (CharacterObject)gSubject;
				else if(gObject instanceof CharacterObject)
					gActor = (CharacterObject)gObject;
				else{
					return "You have no hands!!!";
				}	
//				if(gActor.equipped() != null)
//					return "Put down the " + gActor.equipped().name() + " first.";
				if(gObject instanceof RoomObject && gObject.getContents(userInput[1]) != null)
					target = gObject.getContents(userInput[1]);
				else if(userInput[1].equalsIgnoreCase(gObject.name()) && CharacterObject.you._location.getContents(userInput[1]) != null){
					target = gObject;
				} 
				else {
					target = null;
				}
				if(target == null)
					return "I don't see that here.";
				else if(target instanceof ItemObject) 
				{
					CharacterObject.you._location.removeFromInventory((ItemObject)target);
					CharacterObject.you.addToInventory((ItemObject)target);
				}
				else
					return "You can't take that.";
				System.out.println("You get a " + target.name());
			}
		}
		else if(userInput[0].equalsIgnoreCase("Equip"))
		{
			if(CharacterObject.you.equipped() != null)
			{
				System.out.println("You may only have 1 item equipped.");
			}
			else if(userInput.length < 2)
				System.out.println("Equip what?");
			else
			{
				if(gSubject instanceof CharacterObject)
					gActor = (CharacterObject)gSubject;
				else
					return "Something went seriously wrong..";
				target = gActor.getContents(userInput[1]);
				if(target == null)
					System.out.println("You can't equip " + userInput[1] + " because you don't have it...");
				gActor.equip((ItemObject)target);
				System.out.println("You have equipped " + target._name + ".");
			}
		}
		else if(userInput[0].equalsIgnoreCase("UnEquip"))
		{
			if(CharacterObject.you.equipped() == null)
				System.out.println("You have nothing to unequip... ");
			else if(userInput.length < 2)
				System.out.println("Unequip what?");
			else
			{
				if(gSubject instanceof CharacterObject)
					gActor = (CharacterObject)gSubject;
				else
					return "lolno.";
				target = gActor.equipped();
				if(target == null)
					System.out.println("This should not be null.");
				System.out.println("You have unequipped " + target._name + "!");
				gActor.equip(null);
			}
				
		}
		else if(userInput[0].equalsIgnoreCase("Drop")){ // "Drop @Item", Removes the declared item from the players inventory and puts it into the room's inventory
			if(userInput.length < 2)
				System.out.println("Drop what?");
			else{
				if(gSubject instanceof CharacterObject)
					gActor = (CharacterObject)gSubject;
				else
					return "But you have no hands!!!";
				target = gActor.getContents(userInput[1]);
				if(target == null)
					return "You don't have a " + userInput[1];
				if(target.equals(gActor.equipped()))
				{
					gActor.equip(null);
				}

				CharacterObject.you._location.addToInventory(target);
				CharacterObject.you.removeFromInventory(target);
				System.out.println("You put down the " + target.name());
			}
		}
		else if(userInput[0].equalsIgnoreCase("Light"))
		{
			if(gSubject instanceof CharacterObject)
				gActor = (CharacterObject)gSubject;
			else if(gObject instanceof CharacterObject)
				gActor = (CharacterObject)gObject;
			else{
				return "You have no hands!!!";
			}	
			target = null;
			if(userInput.length < 2)
				System.out.println("Light what?");
			else if(userInput.length == 2)
					target = gActor.location().getContents(userInput[1]);
			if(target == null)
				System.out.println("Nullified.");
			else if(target instanceof ItemObject)
			{
				if(gActor.equipped() != null)
				{
					if(gActor.equipped().isLight())
					{	
						target.setStatus("Light");
						gActor._location.setStatus("Lit");
						target.isOnFire = true;
						System.out.println("You light the " + target.name() + " on the wall.");
					}
					else
						System.out.println("You don't have a light source equipped.");
				}
				else
					System.out.println("You can not light " + target.name() + ". You have nothing equipped!");
			}
			else
				System.out.println("That my friend, is not an item.");
		}
		else if(userInput[0].equalsIgnoreCase("Attack"))
		{ // "Attack" or "Attack @Monster, Useable only if a torch is the equipped item and will set the monster's status to dead
		if(gSubject instanceof CharacterObject)
			gActor = (CharacterObject)gSubject;
		else if(gObject instanceof CharacterObject)
			gActor = (CharacterObject)gObject;
		else{
			return "You have no hands!!!";
		}	
		boolean thisIs = false;
		if(gActor.equipped() != null)
			for(int i = 0; i < gActor.equipped().verbs().size(); i++)
				if(gActor.equipped().verbs().get(i).getVerb().equalsIgnoreCase("Attack"))
					thisIs = true;
		if(userInput.length < 2)
		{
			target = null;
			for(Iterator<CreatureObject> i = gActor.location().creatures().iterator(); i.hasNext() && target == null; )
			{
				CreatureObject monster = i.next();
				if(monster instanceof MonsterObject)
					target = monster;
			}
		}
		else if(userInput.length == 2)
			target = gActor.location().getContents(userInput[1]);
		else
			return "Attack what, exactly?  The darkness?";
		if(target == null)
			return "Attacking imaginary enemies already? The hallucinations must be starting early... how long HAVE you been down here?";
		if(gActor.equipped() == null)
			System.out.println("Why would you try to attack with no items equipped?");
		else if(thisIs == false)
		{
			System.out.println("Your item can't be used to attack... I'm so sorry!");
		}
		else if(target instanceof MonsterObject)
		{
			if(target.getStatus().equalsIgnoreCase("Dead"))
				System.out.println("You mercilessly pummel the corpse of a " + target.name() + ". It appears to be quite dead.");
			else
			{
				target.setStatus("Dead");
				System.out.println("You swing your " + gActor.equipped().name() + " at the " + target.name() + ", killing it in one blow.");
			}
		}
		else
			System.out.println("You attack the " + target.name() + ". It doesn't actually DO anything, but you feel better.");
		}
		else if(userInput[0].equalsIgnoreCase("Help")) { // "Help", will print a description of the room designed to provide helpful information to the player.  Note room must lit in order to get this description
			if(gSubject instanceof CharacterObject)
				gActor = (CharacterObject)gSubject;
			else if(gObject instanceof CharacterObject)
				gActor = (CharacterObject)gObject;
			else
				return "THERE IS NO HELP!!!";
			gActor.location().description("Help");
			System.out.println(gActor.location()._description);
		}
		else 
			System.out.println("Huh?");

		return "";
	}
}
