package edu.asu;

import java.util.Iterator;

import org.w3c.dom.Node;
/**
 * This class handles the character's interaction with the game-state.  It also has a pointer to
 * let the rest of the program call the character with.  It also handles the equipped item and
 * whether or not the item is a light source.
 * @author Michael Berg
 *
 */
public class CharacterObject extends CreatureObject {

	static CharacterObject you;  //a pointer to let the game know where the character object is
	/**
	 * This method initializes the pointer that represents the character in the map.
	 * @param XML
	 */
	public CharacterObject(Node XML) {
		super(XML);
		you = this;
	}
	/**
	 * This method checks through the list of ItemObjects in the rooms inventory and makes sure
	 * the inputed verb command is valid for that object.  Typically this if for taking an item,
	 * equipping an item and attacking with an item.
	 * @param subject
	 * @param input
	 * @author Michael Berg
	 */
	@Override
	public String doVerb(GameObject subject, String input)
	{
		GameCommand verb;
		int x = 0;
		String _Cut[] = input.split(" ");
		if(_Cut.length < 2)
		{
			verb = this.getVerb(input);
			if(verb != null)
				return Client.doVerb(subject, this, verb.getTranslation(input));
			else
				return "I don't understand \"" + input + "\".";
		}
		if(CharacterObject.you.inventory().size() > 0)
		{
			verb = null;
			for(int i = 0; i < CharacterObject.you.inventory().size(); i++)
				if(CharacterObject.you.inventory().get(i).name().equalsIgnoreCase(_Cut[1]))
				{
					x = i;
					verb = CharacterObject.you.inventory().get(i).getVerb(input);
				}
			if(verb != null){
				return CharacterObject.you.inventory().get(x).doVerb(this, input);
			}
		}
		verb = _location.getVerb(input);
		if(verb != null){
			return _location.doVerb(this, input);//return Client.doVerb(equipped(), this, verb.getTranslation(input));
		}
		ItemObject item = null;
//		String[] _Input = input.split(" ");
//		if(_Input[0].equalsIgnoreCase("Equip"))
//		{
//			for(int i = 0; i < CharacterObject.you.inventory().size(); i++)
//			{
//				verb = CharacterObject.you.getVerb(input);
//				if(CharacterObject.you.inventory().get(i).getVerb("Equip") != null)
//					item = (ItemObject) CharacterObject.you.inventory().get(i);
//				if(verb != null)
//					 return item.doVerb(this, input);
//			}
////				return null;
//		}
		for(Iterator<ItemObject> i 	= _location.items().iterator(); i.hasNext(); ){
			 item = i.next();
			 verb = item.getVerb(input);
			 if(verb != null)
				 return item.doVerb(this, input);
		}
		verb = this.getVerb(input);
		if(verb == null)
		{
			return "I don't understand \"" + input + "\"." ;

		}
		else {
			return Client.doVerb(subject, this, verb.getTranslation(input));  // in this case the character is the object not the subject
		}
	}
	/**
	 * This method returns whatever the equipped ItemObject is.
	 * @return the equipped ItemObject
	 * @author Michael Berg
	 */
	public ItemObject equipped(){
		return _equipped;
	}
	/**
	 * This method equips the ItemObject that it is being passed.
	 * @param equipment
	 * @return boolean
	 */
	public boolean equip(ItemObject equipment){
		if(equipment == null) {
			_equipped = null;
			return false;
		}
		equipment.setLocation(this);
		_equipped = equipment;
		return true;
	}
	/**
	 * This method checks to see if equipped item is a light source.
	 * @return boolean
	 */
	@Override
	public boolean isLight()
	{
		if(equipped() != null)
			return equipped().isLight();
		return false;
	}
}
