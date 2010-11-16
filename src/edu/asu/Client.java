package edu.asu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Scanner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


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
	static ArrayList roomList = new ArrayList();
	public static void main(String[] args) {
		String input;
		//step 1: Load XML file
		Element gameFile = parseXMLFile("Project Lovecraft.xml");
		//step 2: Run through the XML file, and Build the map
		NodeList parseRoom = Client.getXMLNodes(gameFile, "Room");
		if(parseRoom != null && parseRoom.getLength() > 0)
			for(int i = 0; i < parseRoom.getLength(); i++){
				System.out.print((i+1) + ": ");
				roomList.add(new RoomObject(parseRoom.item(i)));
				System.out.println("");
			}
		Scanner scan = new Scanner(System.in);
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
		logged.println("==========| " + now.toString() + " |==========");
		do {

			if(CharacterObject.you.location().equals(room)) {
				CreatureObject monster;
				for(Iterator<CreatureObject> i 	= room.creatures().iterator(); i.hasNext(); ){
					monster = i.next();
					System.out.print(monster.think());
				}
			}
			else {
			room = CharacterObject.you.location();
			room.beenInRoom = true;
			room.currentRoom = true;
				if(room.isFirst == true)
					System.out.println(room.description(null));
				else
					System.out.println(room.description("Second"));
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
			else
				System.out.println(CharacterObject.you.location().name() + ">");
			input = scan.nextLine();
			logged.println(input);
			System.out.println(CharacterObject.you.doVerb(null, input));
		} while(!input.equalsIgnoreCase("Quit"));
		try {
			logger.close();
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
			pce.printStackTrace();
		} catch(SAXException se){
			se.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
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
	/**This method takes in the subject and object of a translated verb statement.  From there it
	 * will perform the necessary steps needed to carry out that user inputed verb command.
	 * @param gSubject
	 * @param gObject
	 * @param translation
	 * @author Michael Berg
	 */
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
			return "";
		}
		else if(userInput[0].equalsIgnoreCase("Go"))
		{ 
			CharacterObject.you.location().currentRoom = false;
			// "Go @Exit", where @Exit is the name of a Exit
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
					CreatureObject you = (CreatureObject)gSubject;
					you.setLocation(exit.destination());
					String response = "You go " + userInput[1] + ".\n";
//					if(CharacterObject.you._location.isFirst == true)
//						mapList.add(you);
//					if(userInput[1].equalsIgnoreCase("North"))
//					{
//						x--;
//						Map.setX(mapList, x, y);
//					}
//					else if(userInput[1].equalsIgnoreCase("South"))
//					{
//						x++;
//						Map.setX(mapList, x, y);
//					}
//					else if(userInput[1].equalsIgnoreCase("East"))
//					{
//						y++;
//						Map.setY(mapList, y, x);
//			    	}
//					else if(userInput[1].equalsIgnoreCase("West"))
//					{
//						y--;
//						Map.setY(mapList, y, x);
//					}
					System.out.print(response);
				}
			}		
			
		}
		else if(userInput[0].equalsIgnoreCase("Map") || userInput[0].equalsIgnoreCase("M"))
		{
			Map.setMap(roomList);
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
		else if(userInput[0].equalsIgnoreCase("Take")){ // "Take @Item", removes the declared item from a rooms inventory and puts it into the players inventory
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
				}
				else
					return "You can't take that.";
				System.out.println("You get a " + target.name());
			}
		}
		else if(userInput[0].equalsIgnoreCase("Equip"))
		{
			if(userInput.length < 2)
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
					System.out.println("You don't have a " + userInput[1]);
				if(target.equals(gActor.equipped()))
				{
					gActor.equip(null);
				}
				target.setLocation(gActor.location());
				System.out.println("You put down the " + target.name());
			}
		}
		else if(userInput[0].equalsIgnoreCase("Attack")){ // "Attack" or "Attack @Monster, Useable only if a torch is the equipped item and will set the monster's status to dead
			if(gSubject instanceof CharacterObject)
				gActor = (CharacterObject)gSubject;
			else if(gObject instanceof CharacterObject)
				gActor = (CharacterObject)gObject;
			else{
				return "You have no hands!!!";
			}	
			if(userInput.length < 2){
				target = null;
				for(Iterator<CreatureObject> i = gActor.location().creatures().iterator(); i.hasNext() && target == null; ){
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
				System.out.println("Attacking imaginary enemies already? The hallucinations must be starting early... how long HAVE you been down here?");
			if(target instanceof MonsterObject){
				if(target.getStatus().equalsIgnoreCase("Dead"))
					System.out.println("You mercilessly pummel the corpse of a " + target.name() + ". It appears to be quite dead.");
				else{
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
			System.out.println(gActor.location().description("Help"));
		}
		else 
			System.out.println("Huh?");

		return "";
	}
}
