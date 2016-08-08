import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class BassinGenetique extends JPanel implements MouseMotionListener, MouseListener
{

	private ArrayList<Creature> zoo = new ArrayList<Creature>();
	private Organizer registre = new Organizer();
	private ArrayList<int[]> frigo = new ArrayList<int[]>(); 

	public int tailleX;
	public int tailleY;

	private int xScreen = 0, yScreen = 0;
	
	private int pressedX = 0,pressedY = 0;
	
	private int population;
	private int popstack;
	private double temps = 0;
	private double tpsReprod = 0;
	
	private int qttBouffe;
	
	private Integer oldMoyenne;
	@SuppressWarnings("unused")
	private int step = 0;
	private ArrayList<Integer> evols = new ArrayList<Integer>();
	
	public BassinGenetique(int rang)
	{
		super();

		addMouseMotionListener(this);
		addMouseListener(this);
		setFocusable(true);

		tailleX = 2000;
		tailleY = 2000;
		
		setSize(tailleX+100, tailleY+100); 
		
		setBindings();

		population = tailleX * tailleY / 20000;
		popstack = population*4;
		qttBouffe = population/2;
		
		manageFrigo();
		

		if(rang == 1)
		{
			Creature[] lesLues = Creature.readFile(this,"logEcriture.txt");
			for(int i=0; i<population && i<lesLues.length; i++)
			{
				zoo.add(lesLues[i]);
				registre.add(zoo.get(i));
			}
		}	
		else if(rang == 2)
		{
			Creature[] lesLues = Creature.readFile(this, "logLecture.txt");
			for(int i=0; i<population && i<lesLues.length; i++)
			{
				zoo.add(lesLues[i]);
				registre.add(zoo.get(i));
			}
		}	
		else
		{
			for(int i=0; i<population; i++)
			{
				zoo.add(new Creature(this));
				registre.add(zoo.get(i));
			}
		}		
		
		oldMoyenne = (int) registre.moyenne();
		reproduction();
		
	}
	
	public BassinGenetique()
	{		
		this(0);
	}
	
	private void reproduction()
	{
	
		while(zoo.size()<population && temps - tpsReprod >= 0)
		{
			//System.out.println("Size " + zoo.size());
			int indexPere, indexMere;
			if(random(100)>=60)
				indexPere = 0;
			else if(random(100)>=60)
				indexPere = random((registre.size()-1)/600);
			else if(random(100)>=60)
				indexPere = random((registre.size()-1)/300);
			else if(random(100)>=60)
				indexPere = random((registre.size()-1)/100);
			else
				indexPere = random((registre.size()-1)/10);
			//Pere determiné
			do
			{
				//System.out.println("Recherche de la mere");
				if(random(100) >= 70)
					indexMere = 0;
				else if(random(100)>=70)
					indexMere = random((registre.size()-1)/500);
				else if(random(100)>=70)
					indexMere = random((registre.size()-1)/100);
				else if(random(100)>=70)
					indexMere = random((registre.size()-1)/50);
				else if(random(100)>=70)
					indexMere = random((registre.size()-1)/4);
				else
					indexMere = random((registre.size()-1)/2);	
				//System.out.println("Index pere / mere : " + indexPere + " / " + indexMere);	
			}while(indexPere == indexMere); //Mere determinée
			
			zoo.add(registre.get(indexPere).fusion(registre.get(indexMere)));
			//System.out.println("Index pere / mere : " + indexPere + " / " + indexMere);
			
			
			tpsReprod = temps;
			
		}
		
		
		
	
	}
	
	public static int random(int max)
	{
		return (int)(max*Math.random()+1);
	}
	
	private void manageFrigo()
	{
		while(frigo.size() <= qttBouffe)
		{
			int[] tmp = {(int)(tailleX*Math.random()+50), (int)(tailleY*Math.random()+50)};
			frigo.add(tmp);
		}
	}
	
	public void eaten(int bouffe)
	{
		frigo.remove(bouffe);
		manageFrigo();
	}
	
	public void paintComponent(Graphics g)
	{
		//System.out.println("Size " + zoo.size());
		/*if(zoo.size()==0)
		{
			reproduction();
		}*/
		
		temps += 0.1;
		
		//Dessin témoin de pop nouritture
		g.setColor(Color.GRAY);
		g.drawRect(50-xScreen, 50-yScreen, tailleX, tailleY);
		
		
		
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
		

		for(int i = 0; i < frigo.size();i++)
		{
			g.setColor(Color.green);
			g.fillOval(frigo.get(i)[0]-5-xScreen, frigo.get(i)[1]-5-yScreen, 10, 10);
		}
		
		
		
		for(int i = 0; i < zoo.size(); i++)
		{
			Creature truc = zoo.get(i);
			//System.out.print("Creature " + i);
			truc.live(frigo);
			
			if(i==0)
			{
				//truc.drawBrain(g);
			}
			if(truc.isAlive())
			{
				//truc.show();
				
				Color podium = Color.ORANGE;
				
				int xOeilD = (int)truc.getX() + (int)(30*(-Math.sin(-Math.toRadians(truc.getAngle()) + Math.PI + Math.PI/4)));
				int yOeilD = (int)truc.getY() + (int)(30*(Math.cos(-Math.toRadians(truc.getAngle()) + Math.PI + Math.PI/4)));
				int xOeilG = (int)truc.getX() + (int)(30*(-Math.sin(-Math.toRadians(truc.getAngle()) + Math.PI - Math.PI/4)));
				int yOeilG = (int)truc.getY() + (int)(30*(Math.cos(-Math.toRadians(truc.getAngle()) + Math.PI - Math.PI/4)));

				int xPatteG = (int)truc.getX() + (int)(8*(-Math.sin(-Math.toRadians(truc.getAngle()) + Math.PI + Math.PI/2)));
				int yPatteG = (int)truc.getY() + (int)(8*(Math.cos(-Math.toRadians(truc.getAngle()) + Math.PI + Math.PI/2)));

				int xPatteD = (int)truc.getX() + (int)(8*(-Math.sin(-Math.toRadians(truc.getAngle()) + Math.PI - Math.PI/2)));
				int yPatteD = (int)truc.getY() + (int)(8*(Math.cos(-Math.toRadians(truc.getAngle()) + Math.PI - Math.PI/2)));

				g.setColor(truc.patteD);
				g.fillOval(xPatteD-10-xScreen , yPatteD-10-yScreen, 20, 20);
				g.setColor(truc.patteG);
				g.fillOval(xPatteG-10-xScreen , yPatteG-10-yScreen, 20, 20);
				
				
				g.setColor(truc.queue);
				//g.drawLine((int)truc.getX(), (int)truc.getY(), (int)truc.getX() + (int)(100*Math.sin(Math.toRadians(truc.getAngle()))), (int)truc.getY() + (int)(100*Math.cos(Math.toRadians(truc.getAngle()))));
				ArrayList<int[]> queue = truc.getQueue();				
				for(int[] coords : queue)
				{
					g.drawRect(coords[0]-xScreen, coords[1]-yScreen, 1,1);
				}

				g.setColor(Color.BLACK);
				if(i == indexMaxVivant)
					g.setColor(podium);
				g.drawLine((int)truc.getX()-xScreen, (int)truc.getY()-yScreen, xOeilD-xScreen , yOeilD-yScreen);
				g.drawLine((int)truc.getX()-xScreen, (int)truc.getY()-yScreen, xOeilG-xScreen , yOeilG-yScreen);

				//g.drawOval((int)(xOeilD-(truc.getOeilD())), (int)(yOeilD-(truc.getOeilD())),(int) truc.getOeilD()*2,(int) truc.getOeilD()*2);
				//g.drawOval((int)(xOeilG-(truc.getOeilG())), (int)(yOeilG-(truc.getOeilG())),(int) truc.getOeilG()*2,(int) truc.getOeilG()*2);

				//g.drawLine(xOeilD, yOeilD, truc.getTarget()[0], truc.getTarget()[1]);
				//g.drawLine(xOeilG, yOeilG, truc.getTarget()[0], truc.getTarget()[1]);
				
				
				g.setColor(Color.black);
				if(i == indexMaxVivant)
					g.setColor(podium);
				g.fillOval(xOeilD-5-xScreen, yOeilD-5-yScreen, 10, 10);
				g.fillOval(xOeilG-5-xScreen, yOeilG-5-yScreen, 10, 10);
				
				g.setColor(new Color(255, 255-(truc.getFaim()*255/Creature.faimMax) ,255-(truc.getFaim()*255/Creature.faimMax)));
				g.fillOval((int)truc.getX()-10-xScreen, (int)truc.getY()-10-yScreen, 20, 20);
				g.setColor(Color.BLACK);
				if(i == indexMaxVivant)
					g.setColor(podium);				
				g.drawOval((int)truc.getX()-10-xScreen, (int)truc.getY()-10-yScreen, 20, 20);
				
			}
			else
			{
				registre.add(zoo.remove(i));
				manageRegistre();
			}
			if(registre.size()>=3)
			{
				reproduction();
				//System.out.println("Reproduction");
			}
			//System.out.println("\nDeath, " + registre.size() + " Créatures enregistrées / " + population*2);
			
		}
		
	}
	
	private void manageRegistre()
	{
		Organizer tmp = new Organizer();
		if(registre.size() >= popstack)
		{
			for(int a = 0; a < popstack ; a++)
				tmp.add(registre.get(a));
		}
		else
		{
			for(Creature a : registre)
				tmp.add(a);
		}
		registre.clear();
		registre = tmp;

		int evol = (int) (registre.moyenne()-oldMoyenne);
		if(registre.size()>= popstack)
		{
			evols.add(0,evol);
		}
		
		double totalEvols = 0;
		double zeroes = 0;
		for(int e : evols)
		{
			totalEvols += e;
			if(e==0)
			{
				zeroes+=0.05;
				totalEvols++;
			}
		}
		if(evols.size()!=0 && evols.size()-zeroes!=0)totalEvols/=(evols.size()-zeroes);
		//System.out.println(totalEvols + " -> " + scaleEvols(totalEvols));
		Matrix.setDiv(scaleEvols(totalEvols));
		
		if(evol != 0 && registre.size() >= popstack)
		{
			//System.out.println("Evolution : " + evol + " après " + step + " stagnations");
			step = 0;
		}
		else
			step++;
		
		oldMoyenne = (int) registre.moyenne();
		
		//System.out.println(oldMoyenne);

		while(evols.size() >= 150)
			evols.remove(evols.size()-1);
	}
	
	static private double scaleEvols(double a)
	{
		return 10*sigmoidP(a-5, 0.8);		
	}
	
	static public double sigmoidP(double a, double lambda)
	{
		return 1-(1/(1+Math.exp(-lambda*a)));
	}

	public void save()
	{
		System.out.println("Sauvegarde");
		Creature[] tmp = new Creature[registre.size()];
		for(int o = 0; o<registre.size();o++)
		{
			tmp[o] = registre.get(o);
		}

		Creature.writeFile(tmp);
	}
	
	public static double getDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}
	
	private void setBindings()
	{
		Action upAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mooveScreen("UP");
			}		
		};
		
		Action downAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mooveScreen("DOWN");
			}		
		};
		
		Action leftAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mooveScreen("LEFT");
			}		
		};
		
		Action rightAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mooveScreen("RIGHT");
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
	
	
	private void mooveScreen(String cmd)
	{	
		switch(cmd)
		{
		case "UP":
			yScreen--;
			break;
		
		case "DOWN":
			yScreen++;
			break;

		case "LEFT":
			xScreen--;
			break;
		
		case "RIGHT":
			xScreen++;
			break;
		}
	}
	
	private void mooveScreen(int x, int y)
	{
		yScreen += y; xScreen += x;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mooveScreen(pressedX-e.getX(), pressedY-e.getY());

		pressedX = e.getX(); pressedY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {	
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressedX = e.getX(); pressedY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
