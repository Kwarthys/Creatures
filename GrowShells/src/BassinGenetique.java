import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BassinGenetique extends JPanel
{

	private ArrayList<Creature> zoo = new ArrayList<Creature>();
	private Organizer registre = new Organizer();
	private ArrayList<int[]> frigo = new ArrayList<int[]>(); 

	public int tailleX;
	public int tailleY;
	
	private int population;
	private int popstack;
	private double temps = 0;
	private double tpsReprod = 0;
	
	private int qttBouffe;
	
	private Integer oldMoyenne;
	private int step = 0;
	private ArrayList<Integer> evols = new ArrayList<Integer>();
	
	public BassinGenetique(int rang)
	{
		super();
		this.setSize(tailleX+100, tailleY+100); 

		tailleX = 1400;
		tailleY = 800;
		population = tailleX * tailleY / 20000;
		popstack = population*4;
		qttBouffe = population/3;
		
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
		g.drawRect(50, 50, tailleX, tailleY);
		
		
		
		//Dessin des graphs
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
		
		//graph2
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
		
		//System.out.println(maxVivant +" " + maxMort + " " + max + " : " + ((zoo.get(4).getScore()*100/max)));
		
		for(int a = 0; a < zoo.size() ; a++)
		{
			Point absc = new Point(a*(largeur+espace) + 15 , origineG2Y);
			
			int h = (zoo.get(a).getScore()*90/max);
			
			g.setColor(new Color(255, 255-(zoo.get(a).getFaim()*255/Creature.faimMax) ,255-(zoo.get(a).getFaim()*255/Creature.faimMax)));
			g.fillRect(absc.x, absc.y, largeur, -h);
		}
		g.setColor(Color.red);
		g.drawLine(10, origineG2Y - (maxMort*90/max), 330+10, origineG2Y - (maxMort*90/max));
		g.setColor(Color.black);
		g.drawLine(10, origineG2Y - (oldMoyenne*90/max), 330+10, origineG2Y - (oldMoyenne*90/max));
		g.drawLine(10, origineG2Y - (minMort*90/max), 330+10, origineG2Y - (minMort*90/max));
		
		//dessin du temoin de fonctionnement
		String waiting = "Waiting enough Data";
		if(registre.size()>= popstack){g.setColor(Color.BLACK);}
		else{g.setColor(Color.red);g.drawString(waiting, (310-waiting.length()*6)/2, 65);}
		
		g.fillOval(22+(int)(10*Math.cos(temps)), 130+20+110+(int)(10*Math.sin(temps)), 10, 10);
		

		for(int i = 0; i < frigo.size();i++)
		{
			g.setColor(Color.green);
			g.fillOval(frigo.get(i)[0]-5, frigo.get(i)[1]-5, 10, 10);
		}
		
		
		
		for(int i = 0; i < zoo.size(); i++)
		{
			Creature truc = zoo.get(i);
			//System.out.print("Creature " + i);
			truc.live(frigo);
			if(i==0)
			{
				truc.showBrain();
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
				g.fillOval(xPatteD-10 , yPatteD-10, 20, 20);
				g.setColor(truc.patteG);
				g.fillOval(xPatteG-10 , yPatteG-10, 20, 20);
				
				
				g.setColor(truc.queue);
				//g.drawLine((int)truc.getX(), (int)truc.getY(), (int)truc.getX() + (int)(100*Math.sin(Math.toRadians(truc.getAngle()))), (int)truc.getY() + (int)(100*Math.cos(Math.toRadians(truc.getAngle()))));
				ArrayList<int[]> queue = truc.getQueue();				
				for(int[] coords : queue)
				{
					g.drawRect(coords[0], coords[1], 1,1);
				}

				g.setColor(Color.BLACK);
				if(i == indexMaxVivant)
					g.setColor(podium);
				g.drawLine((int)truc.getX(), (int)truc.getY(), xOeilD , yOeilD);
				g.drawLine((int)truc.getX(), (int)truc.getY(), xOeilG , yOeilG);

				//g.drawOval((int)(xOeilD-(truc.getOeilD())), (int)(yOeilD-(truc.getOeilD())),(int) truc.getOeilD()*2,(int) truc.getOeilD()*2);
				//g.drawOval((int)(xOeilG-(truc.getOeilG())), (int)(yOeilG-(truc.getOeilG())),(int) truc.getOeilG()*2,(int) truc.getOeilG()*2);

				//g.drawLine(xOeilD, yOeilD, truc.getTarget()[0], truc.getTarget()[1]);
				//g.drawLine(xOeilG, yOeilG, truc.getTarget()[0], truc.getTarget()[1]);
				
				
				g.setColor(Color.black);
				if(i == indexMaxVivant)
					g.setColor(podium);
				g.fillOval(xOeilD-5, yOeilD-5, 10, 10);
				g.fillOval(xOeilG-5, yOeilG-5, 10, 10);
				
				g.setColor(new Color(255, 255-(truc.getFaim()*255/Creature.faimMax) ,255-(truc.getFaim()*255/Creature.faimMax)));
				g.fillOval((int)truc.getX()-10, (int)truc.getY()-10, 20, 20);
				g.setColor(Color.BLACK);
				if(i == indexMaxVivant)
					g.setColor(podium);				
				g.drawOval((int)truc.getX()-10, (int)truc.getY()-10, 20, 20);
				
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
		
		//registre.show();
		//System.out.println("Naissances "+ ++generation +"\n"+ registre.size() + " : " + registre.get(0).getScore() + " : " + registre.get(1).getScore()+ " : " + registre.get(2).getScore() + " : " + registre.get(registre.size()-2).getScore() + " : " + registre.get(registre.size()-1).getScore() +" / " + registre.moyenne());
		/*System.out.print(" -> " + registre.size() + " : " );//+ registre.get(0).getScore() + " : " + registre.get(1).getScore()+ " : " + registre.get(2).getScore() + " : " + registre.get(registre.size()-2).getScore() + " : " + registre.get(registre.size()-1).getScore() +" // " + (registre.moyenne()-oldMoyenne));
		for(Creature index : registre)
		{
			System.out.print(index.getScore() + " || ");			
		}*/
		int evol = (int) (registre.moyenne()-oldMoyenne);
		if(registre.size()>= popstack)
		{
			evols.add(0,evol);
		}
		
		if(evol != 0 && registre.size() >= popstack){System.out.println("Evolution : " + evol + " après " + step + " stagnations"); step = 0;}
		else
			step++;
		
		oldMoyenne = (int) registre.moyenne();

		while(evols.size() >= 150)
			evols.remove(evols.size()-1);
	}

	public void save()
	{
		System.out.println("Sauvegarde");
		Creature[] tmp = new Creature[registre.size()];
		for(int o = 0; o<registre.size();o++)
		{
			tmp[o] = registre.get(o);
		}

		Creature.writeFile(tmp,registre.size());
	}
}
