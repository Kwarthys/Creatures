package creatures.omi.mainpanel;

import java.util.ArrayList;

import creatures.model.Creature;

public interface MainPanelServices {
	public int getXSize();
	public int getYSize();
	public void eat(int indexEaten);
	
	public int getXScreen();
	public int getYScreen();

	public ArrayList<Creature> getZoo();
	public ArrayList<int[]> getFrigo();
}
