package creatures.omi.mainpanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import creatures.model.Creature;
import utils.StringCallback;

@SuppressWarnings("serial")
public class MainPanelView extends JPanel
{
	protected StringCallback callback;
	protected MainPanelServices services;
	
	public MainPanelView(StringCallback callback, MainPanelServices services)
	{
		this.callback = callback;
		this.services = services;
		
		setFocusable(true);
		setBindings();
	}
	
	public void paintComponent(Graphics g)
	{
		/* reblanchissement du fond */
		g.setColor(Color.white);
		g.fillRect(0,0,this.getWidth(), this.getHeight());
		
		//System.out.println("Size " + zoo.size());
		/*if(zoo.size()==0)
		{
			reproduction();
		}*/
		
		//temps += 0.1;
		
		//Dessin témoin de pop nouritture
		g.setColor(Color.GRAY);
		g.drawRect(50-services.getXScreen(), 50-services.getYScreen(), services.getXSize(), services.getYSize());
		
		/*
		
		//Dessin des graphs
		//Evolution overview graph
		g.setColor(Color.WHITE);
		g.fillRect(10, 10, 300, 110);
		g.setColor(Color.black);
		g.drawRect(10, 10, 300, 110);
		g.setColor(Color.RED);
		
		int maxEvol = 1;
		for(int e : evols)
		{
			if(e > maxEvol)
				maxEvol = e;
		}
		for(int e=0; e < evols.size()-1; e++)
		{
			Point p1 = new Point(12+2*e, 110 - (evols.get(e)*90/maxEvol));
			g.drawLine(p1.x, p1.y, 12+2*e, 110);
		}
		
		g.drawString(String.valueOf(maxEvol),20 , 30);
		
		//Current scores graph
		g.setColor(Color.WHITE);
		g.fillRect(10, 130, 330, 110);
		g.setColor(Color.black);
		g.drawRect(10, 130, 330, 110);
		
		int maxVivant = 1;
		int indexMaxVivant = 0;
		int maxMort = 1;
		int minMort = registre.get(0).getScore();
		int max;
		for(int z = 0; z < zoo.size(); z++)
		{
			if(zoo.get(z).getScore() > maxVivant)
			{
				maxVivant = zoo.get(z).getScore();
				indexMaxVivant = z;
			}
		}
		for(Creature e : registre)
		{
			if(e.getScore() > maxMort)
				maxMort = e.getScore();
			if(e.getScore() < minMort)
				minMort = e.getScore();
		}
		if(maxVivant > maxMort)
			max = maxVivant;
		else
			max = maxMort;
		
		int espace = 1;
		int largeur = (320 - (zoo.size()*espace)) / zoo.size();
		int origineG2Y = 130+110-10;

		//Tracé des droites témoins
		g.setColor(Color.red);
		g.drawLine(10, origineG2Y - (maxMort*90/max), 330+10, origineG2Y - (maxMort*90/max));
		g.setColor(Color.black);
		g.drawLine(10, origineG2Y - (oldMoyenne*90/max), 330+10, origineG2Y - (oldMoyenne*90/max));
		g.drawLine(10, origineG2Y - (minMort*90/max), 330+10, origineG2Y - (minMort*90/max));
		
		//System.out.println(maxVivant +" " + maxMort + " " + max + " : " + ((zoo.get(4).getScore()*100/max)));
		
		for(int a = 0; a < zoo.size() ; a++)
		{
			Point absc = new Point(a*(largeur+espace) + 15 , origineG2Y);
			
			int h = (zoo.get(a).getScore()*90/max);
			
			g.setColor(new Color(255, 255-(zoo.get(a).getFaim()*255/Creature.faimMax) ,255-(zoo.get(a).getFaim()*255/Creature.faimMax)));
			g.fillRect(absc.x, absc.y-h, largeur, h);
		}
		
		//dessin du temoin de fonctionnement
		String waiting = "Waiting enough Data";
		if(registre.size()>= popstack){g.setColor(Color.BLACK);}
		else{g.setColor(Color.red);g.drawString(waiting, (310-waiting.length()*6)/2, 65);}
		
		g.fillOval(22+(int)(10*Math.cos(temps)), 130+20+110+(int)(10*Math.sin(temps)), 10, 10);
		*/

		for(int i = 0; i < services.getFrigo().size();i++)
		{
			g.setColor(Color.green);
			g.fillOval(services.getFrigo().get(i)[0]-5-services.getXScreen(), services.getFrigo().get(i)[1]-5-services.getYScreen(), 10, 10);
		}
		
		for(int i = 0; i < services.getZoo().size(); i++)
		{
			Creature currentCreature = services.getZoo().get(i);
			
			if(i==0)
			{
				//truc.drawBrain(g);
			}
			if(currentCreature.isAlive())
			{
				//truc.show();
				
				Color podium = Color.ORANGE;
				
				int xOeilD = (int)currentCreature.getX() + (int)(30*(-Math.sin(-Math.toRadians(currentCreature.getAngle()) + Math.PI + Math.PI/4)));
				int yOeilD = (int)currentCreature.getY() + (int)(30*(Math.cos(-Math.toRadians(currentCreature.getAngle()) + Math.PI + Math.PI/4)));
				int xOeilG = (int)currentCreature.getX() + (int)(30*(-Math.sin(-Math.toRadians(currentCreature.getAngle()) + Math.PI - Math.PI/4)));
				int yOeilG = (int)currentCreature.getY() + (int)(30*(Math.cos(-Math.toRadians(currentCreature.getAngle()) + Math.PI - Math.PI/4)));

				int xPatteG = (int)currentCreature.getX() + (int)(8*(-Math.sin(-Math.toRadians(currentCreature.getAngle()) + Math.PI + Math.PI/2)));
				int yPatteG = (int)currentCreature.getY() + (int)(8*(Math.cos(-Math.toRadians(currentCreature.getAngle()) + Math.PI + Math.PI/2)));

				int xPatteD = (int)currentCreature.getX() + (int)(8*(-Math.sin(-Math.toRadians(currentCreature.getAngle()) + Math.PI - Math.PI/2)));
				int yPatteD = (int)currentCreature.getY() + (int)(8*(Math.cos(-Math.toRadians(currentCreature.getAngle()) + Math.PI - Math.PI/2)));

				g.setColor(currentCreature.patteD);
				g.fillOval(xPatteD-10-services.getXScreen() , yPatteD-10-services.getYScreen(), 20, 20);
				g.setColor(currentCreature.patteG);
				g.fillOval(xPatteG-10-services.getXScreen() , yPatteG-10-services.getYScreen(), 20, 20);
				
				
				g.setColor(currentCreature.queue);
				//g.drawLine((int)truc.getX(), (int)truc.getY(), (int)truc.getX() + (int)(100*Math.sin(Math.toRadians(truc.getAngle()))), (int)truc.getY() + (int)(100*Math.cos(Math.toRadians(truc.getAngle()))));
				
				currentCreature.getQueue().forEach((int[] coords) -> {
					g.drawRect(coords[0]-services.getXScreen(), coords[1]-services.getYScreen(), 1,1);
				});

				g.setColor(Color.BLACK);
				if(i == 0)
					g.setColor(podium);
				g.drawLine((int)currentCreature.getX()-services.getXScreen(), (int)currentCreature.getY()-services.getYScreen(), xOeilD-services.getXScreen() , yOeilD-services.getYScreen());
				g.drawLine((int)currentCreature.getX()-services.getXScreen(), (int)currentCreature.getY()-services.getYScreen(), xOeilG-services.getXScreen() , yOeilG-services.getYScreen());

				//g.drawOval((int)(xOeilD-(truc.getOeilD())), (int)(yOeilD-(truc.getOeilD())),(int) truc.getOeilD()*2,(int) truc.getOeilD()*2);
				//g.drawOval((int)(xOeilG-(truc.getOeilG())), (int)(yOeilG-(truc.getOeilG())),(int) truc.getOeilG()*2,(int) truc.getOeilG()*2);

				//g.drawLine(xOeilD, yOeilD, truc.getTarget()[0], truc.getTarget()[1]);
				//g.drawLine(xOeilG, yOeilG, truc.getTarget()[0], truc.getTarget()[1]);
				
				
				g.setColor(Color.black);
				if(i == 0)
					g.setColor(podium);
				g.fillOval(xOeilD-5-services.getXScreen(), yOeilD-5-services.getYScreen(), 10, 10);
				g.fillOval(xOeilG-5-services.getXScreen(), yOeilG-5-services.getYScreen(), 10, 10);
				
				g.setColor(new Color(255, 255-(currentCreature.getFaim()*255/Creature.faimMax) ,255-(currentCreature.getFaim()*255/Creature.faimMax)));
				g.fillOval((int)currentCreature.getX()-10-services.getXScreen(), (int)currentCreature.getY()-10-services.getYScreen(), 20, 20);
				g.setColor(Color.BLACK);
				if(i == 0)
					g.setColor(podium);				
				g.drawOval((int)currentCreature.getX()-10-services.getXScreen(), (int)currentCreature.getY()-10-services.getYScreen(), 20, 20);				
			}			
		}		
	}
	
	protected void setBindings()
	{
		Action upAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				callback.callback("UP");
			}		
		};
		
		Action downAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				callback.callback("DOWN");
			}		
		};
		
		Action leftAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				callback.callback("LEFT");
			}		
		};
		
		Action rightAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				callback.callback("RIGHT");
			}		
		};

		this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upStruck");
		this.getActionMap().put("upStruck", upAction);
		
		this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downStruck");
		this.getActionMap().put("downStruck", downAction);
		
		this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftStruck");
		this.getActionMap().put("leftStruck", leftAction);
		
		this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightStruck");
		this.getActionMap().put("rightStruck", rightAction);
	}
}
