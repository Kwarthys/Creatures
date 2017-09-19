package creatures.omi.mainpanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import creatures.model.Creature;
import creatures.omi.mainpanel.brainpanel.BrainWindow;
import utils.StringCallback;

public class MainPanelCtrl implements MainPanel{

	private MainPanelModel model;
	private MainPanelView view;
	
	private MainPanelServices services = new MainPanelServices() {		
		@Override
		public int getYSize() {
			return model.tailleY;
		}
		
		@Override
		public int getXSize() {
			return model.tailleX;
		}
		
		@Override
		public void eat(int indexEaten) {
			model.eaten(indexEaten);			
		}

		@Override
		public int getXScreen() {
			return model.xScreen;
		}

		@Override
		public int getYScreen() {
			return model.yScreen;
		}

		@Override
		public ArrayList<Creature> getZoo() {
			return model.zoo;
		}

		@Override
		public ArrayList<int[]> getFrigo() {
			return model.frigo;
		}
	};
	
	public MainPanelCtrl(int rang)
	{		
		model = new MainPanelModel();
		view = new MainPanelView(new StringCallback() {			
			@Override
			public void callback(String s) {
				mooveScreen(s);
			}
		}, services);
				
		registerListener();
		
		model.tailleX = 2000;
		model.tailleY = 2000;
		
		view.setSize(model.tailleX+100, model.tailleY+100);

		model.population = model.tailleX * model.tailleY / 20000;
		model.popstack = model.population*4;
		model.qttBouffe = model.population/2;
		
		model.manageFrigo();
		

		if(rang == 1)
		{
			Creature[] lesLues = Creature.readFile(services,"logEcriture.txt");
			for(int i=0; i<model.population && i<lesLues.length; i++)
			{
				model.zoo.add(lesLues[i]);
				model.registre.add(model.zoo.get(i));
			}
		}	
		else if(rang == 2)
		{
			Creature[] lesLues = Creature.readFile(services, "logLecture.txt");
			for(int i=0; i<model.population && i<lesLues.length; i++)
			{
				model.zoo.add(lesLues[i]);
				model.registre.add(model.zoo.get(i));
			}
		}	
		else
		{
			for(int i=0; i<model.population; i++)
			{
				model.zoo.add(new Creature(services));
				model.registre.add(model.zoo.get(i));
			}
		}		
		
		model.oldMoyenne = (int) model.registre.moyenne();
		
		model.reproduction();
		
	}
	
	private void registerListener()
	{	
		view.setBindings();
		
		view.addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent e) {}			
			@Override
			public void mousePressed(MouseEvent e) {
				model.pressedX = e.getX(); model.pressedY = e.getY();				
			}			
			@Override
			public void mouseExited(MouseEvent e) {}			
			@Override
			public void mouseEntered(MouseEvent e) {}			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		view.addMouseMotionListener(new MouseMotionListener() {			
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				mooveScreen(model.pressedX-e.getX(), model.pressedY-e.getY());
				model.pressedX = e.getX(); model.pressedY = e.getY();				
			}
		});
	}
	
	@Override
	public void playRound()
	{		
		model.live();
		
		if(model.brainWindowOpened != null)
		{
			model.brainWindowOpened.setBrainToDraw(model.zoo.get(0).getBrain());
		}
	}
	
	private void mooveScreen(int dx, int dy)
	{
		model.xScreen += dx;
		model.yScreen += dy;
	}
	
	private void mooveScreen(String cmd)
	{	
		switch(cmd)
		{
		case "UP":
			model.yScreen--;
			break;
		
		case "DOWN":
			model.yScreen++;
			break;

		case "LEFT":
			model.xScreen--;
			break;
		
		case "RIGHT":
			model.xScreen++;
			break;
		}
	}

	@Override
	public void save()
	{
		System.out.println("Sauvegarde");
		Creature[] tmp = new Creature[model.registre.size()];
		for(int o = 0; o<model.registre.size();o++)
		{
			tmp[o] = model.registre.get(o);
		}

		Creature.writeFile(tmp);
	}

	@Override
	public JPanel getView() {
		return view;
	}

	@Override
	public void repaint() {
		view.repaint();
		if(model.brainWindowOpened != null)model.brainWindowOpened.repaintContent();
	}

	@Override
	public void openBrainView() {
		if(model.brainWindowOpened != null)return;
		model.brainWindowOpened = new BrainWindow();
		model.brainWindowOpened.setBrainToDraw(model.zoo.get(0).getBrain());
	}
}
