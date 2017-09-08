package creatures.model;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import creatures.omi.BassinGenetique;
import creatures.utils.Brain;
import creatures.utils.Scorable;
import utils.Matrix;

public class Creature implements Scorable
{
	private double x,y,angle, oeilG, oeilD;
	
	private Brain brain;
	
	private int[] target;
	
	private int faim;
	private boolean isAlive = true; 
	private BassinGenetique bassin;
	private int score = 0;
	
	private ArrayList<int[]> coordsQueue = new ArrayList<int[]>(); 

	public Color patteD;
	public Color patteG;
	public Color queue;

	private int xOeilD = (int)getX() + (int)(30*(-Math.sin(-Math.toRadians(getAngle()) + Math.PI + Math.PI/4)));
	private int yOeilD = (int)getY() + (int)(30*(Math.cos(-Math.toRadians(getAngle()) + Math.PI + Math.PI/4)));
	private int xOeilG = (int)getX() + (int)(30*(-Math.sin(-Math.toRadians(getAngle()) + Math.PI - Math.PI/4)));
	private int yOeilG = (int)getY() + (int)(30*(Math.cos(-Math.toRadians(getAngle()) + Math.PI - Math.PI/4)));
	
	private Matrix outputs = null, inputs = null;
	
	public static int faimMax = 3500;
	
	public Creature(double leX, double leY,BassinGenetique lebassin)
	{
		this(lebassin);
		x = leX; y = leY;
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.set(0, tmp);
	}
		
	public Creature(BassinGenetique lebassin)
	{ 
		bassin = lebassin;
		brain = new Brain(3,3,2,5);
		angle = 360*Math.random(); faim = 1000;setScore(0);
		x = (bassin.tailleX-200)*Math.random()+100;
		y = (bassin.tailleY-200)*Math.random()+100;
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.add(tmp);
	}
	
	public void live(ArrayList<int[]> nourriture)
	{
		//Searching for nearest food cluster.
		double recordNorme = norme(nourriture.get(0));
		int indexRecord = 0;
		for(int i = 0; i < nourriture.size(); i++)
		{
			if(recordNorme > norme(nourriture.get(i)))
			{
				recordNorme = norme(nourriture.get(i)); indexRecord = i;
			}
		}

		oeilD = norme(nourriture.get(indexRecord), 1);
		oeilG = norme(nourriture.get(indexRecord), -1);
		
		target = nourriture.get(indexRecord);
				
		if(oeilD > bassin.tailleX){oeilD=bassin.tailleX;}
		if(oeilG > bassin.tailleX){oeilG=bassin.tailleX;}
		
		double[][] in = {{((float)faim)/faimMax, (-oeilD/bassin.tailleX)+1, (-oeilG/bassin.tailleX)+1}};
		//System.out.println();
		inputs = new Matrix(in);
		
		outputs = brain.compute(inputs);

		faim++;
		if( norme(nourriture.get(indexRecord)) < 15)
		{
			//System.out.println("Avant manger : " + faim);
			manger();
			//System.out.println("Après manger : " + faim);
			bassin.eaten(indexRecord);
		}
		if(outputs.get(0,0) > 0)
		{
			forward();
			queue = Color.RED;
		}
		else
			queue = Color.GRAY;
		
		boolean turning = false;
		
		if(outputs.get(0, 1) > 0)
		{
			turnRight();
			turning = true;
			patteD = Color.RED;
		}
		else
			patteD = Color.GRAY;
		
		if(outputs.get(0, 2) > 0)
		{
			turnLeft();
			patteG = Color.RED;
			if(turning)
				faim += 1;
		}
		else
			patteG = Color.GRAY;


		xOeilD = (int)getX() + (int)(30*(-Math.sin(-Math.toRadians(getAngle()) + Math.PI + Math.PI/4)));
		yOeilD = (int)getY() + (int)(30*(Math.cos(-Math.toRadians(getAngle()) + Math.PI + Math.PI/4)));
		xOeilG = (int)getX() + (int)(30*(-Math.sin(-Math.toRadians(getAngle()) + Math.PI - Math.PI/4)));
		yOeilG = (int)getY() + (int)(30*(Math.cos(-Math.toRadians(getAngle()) + Math.PI - Math.PI/4)));
		
		testMort();
	}
	
	public Creature fusion(Creature mere)
	{
		Creature child = new Creature(this.bassin);
		child.brain = Brain.fusion(this.brain, mere.brain);
		return child;
	}
		
	private void controlAngle()
	{
		if(angle >= 360)
			angle -= 360;
		if(angle < 0)
			angle += 360;
	}
	
	private void testMort()
	{
		if(faim >= faimMax || faim <= 0) //si Mort
			isAlive = false;
		else
			addScore(1);
		
		if(x < -100 || x > bassin.tailleX+100 || y < -100 || y > bassin.tailleY+100) //pénalité si trop hors du cadre
			delScore(1);
	}
	
	private void manger()
	{
		faim -= 800;
		addScore(200);
		testMort();
	}
	
	private void forward()
	{
		faim++;addScore(1);
		//System.out.println("gogogo");
		x -= 2*Math.sin(Math.toRadians(angle));
		y -= 2*Math.cos(Math.toRadians(angle));
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.add(0,tmp);
		manageQueue();
	}
	
	private void manageQueue()
	{
		while(coordsQueue.size()>=100)
			coordsQueue.remove(coordsQueue.size()-1);
	}
	
	private void turnLeft()
	{
		faim++;addScore(1);
		angle += 6; controlAngle();
		//System.out.println("turnleft");
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.add(0,tmp);
		manageQueue();
	}
	
	private void turnRight()
	{
		faim++;addScore(1);
		angle -= 6; controlAngle();
		//System.out.println("turnright");
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.add(0,tmp);
		manageQueue();
	}

	public double norme(int[] leX)
	{
		return Math.sqrt((leX[0]-x)*(leX[0]-x) + (leX[1]-y)*(leX[1]-y));
	}
	
	public double norme(int[] leX, int oeil) //oeil = -1 pour Gauche et +1 pour Droite
	{		
		if(oeil == -1)
			return BassinGenetique.getDistance(leX[0], leX[1], xOeilG, yOeilG);
		else
			return BassinGenetique.getDistance(leX[0],leX[1], xOeilD,  yOeilD);
	}
	
	public void showBrain()
	{
		brain.show();		
	}
	
	public void show()
	{
		showBrain();
		System.out.println( " Située en : " + x + "|" + y + " Faim : " + faim + ".\n Valeurs yeux : " + oeilG + ".." + oeilD);
		if(isAlive)
			System.out.println("En vie\n");
		else
			System.out.println("Hors course\n");
	}

	public double getAngle(){return angle;}
	public ArrayList<int[]> getQueue(){return coordsQueue;}
	public double getX(){return x;}
	public double getY(){return y;}
	public int[] getTarget(){return target;}
	public double getOeilD(){return oeilD;}
	public double getOeilG(){return oeilG;}
	public int getFaim(){return faim;}
	public boolean isAlive(){return isAlive;}
	public void setX(double leX){x = leX;}
	public void setY(double leY){y = leY;}

	@Override
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void addScore(int plus)
	{
		this.score += plus;
	}
	
	public void delScore(int moins)
	{
		this.score -= moins;
	}
	
	public static void writeFile(Creature[] zoo)
	{
		File f = new File ("logEcriture.txt");
		int nbOfBrains = zoo.length;
		 
		try
		{
		    FileWriter fw = new FileWriter (f);
		    fw.write(nbOfBrains + " " + zoo[0].brain.getType());
		    for(int i = 0; i < nbOfBrains; i++)
		    {
			    fw.write (zoo[i].brain.toString());	
		    }	 
		    fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de l'ecriture : " + exception.getMessage());
		}
	}
	
	public static Creature[] readFile(BassinGenetique leBassin, String file)
	{
		Creature[] zoo = null;
		File f = new File(file);
		try
		{
			Scanner fr = new Scanner(f);
		    double c=0;
			c = fr.nextDouble();
			int im=0,jm=0, taille =(int) c; //compteur 0 1 2 3 <=> itoh htoo fito fhto
			int typeOfBrain = fr.nextInt();
			zoo = new Creature[(int) c];
    		for(int o =0; o<taille;o++)
    		{
    			zoo[o] = new Creature(leBassin);
    		}
	    	try
	    	{
	    		System.out.println(" Taille : " + c);
	    		System.out.println(" zoo : " + zoo);
	    		for(int o =0; o<taille;o++)
	    		{
	    			ArrayList<Matrix> readBrain = new ArrayList<Matrix>();
	    			for(int quatre = 0; quatre < typeOfBrain; quatre++)
	    			{
				        c = fr.nextDouble(); im = (int)c; System.out.println(" im : " + im);
				        c = fr.nextDouble(); jm = (int)c; System.out.println(" jm : " + jm);
				        Matrix tmp = new Matrix(jm,im);
				        for(int j = 0; j < jm; j++)
				        {
				        	//System.out.println(" j : " + j);
				        	for(int i=0; i < im; i++)
				        	{
					        	//System.out.println(" i : " + i);
				        		c = Double.parseDouble(fr.next()); System.out.println(" c : " + c);
				        		tmp.set(i,j,c);
				        	}
					    }
				        readBrain.add(new Matrix(tmp));
	    			}
	    			zoo[o].brain = new Brain(readBrain);
	    		}
	    	}
	    	catch (NoSuchElementException exception)
	        {
	        	System.out.println(" End " + exception.getMessage());
	        }
		    
		    fr.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
		
		return zoo;
	}

}
