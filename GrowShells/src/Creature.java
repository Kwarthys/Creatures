import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Creature
{
	private Matrix fhto = new Matrix(1,3);
	private Matrix itoh = new Matrix(3,4);
	private Matrix fito = new Matrix(1,4);
	private Matrix htoo = new Matrix(4,3);
	private double x,y,angle, oeilG, oeilD;
	
	private int[] target;
	
	private int faim;
	private boolean isAlive = true; 
	private BassinGenetique bassin;
	private int score = 0;
	
	private ArrayList<int[]> coordsQueue = new ArrayList<int[]>(); 

	public Color patteD;
	public Color patteG;
	public Color queue;
	
	//private int penalite = 0;
	
	private Matrix outputs = null, inputs = null, mid = null/*, oldOutputs = null*/;
	
	//private boolean premierTour = true;
	
	public static int faimMax = 2000;
	
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
		itoh = Matrix.random(3, 4).times(2).minus(1);
		//itoh.show("inputs to hidden");
		htoo = Matrix.random(4, 3).times(2).minus(1);
		fito = Matrix.random(1, 4).times(2).minus(1);
		//fito.show("Threshold Hidden");
		fhto = Matrix.random(1, 3).times(2).minus(1);
		angle = 360*Math.random(); faim = 600;setScore(0);
		x = (bassin.tailleX-200)*Math.random()+100;
		y = (bassin.tailleY-200)*Math.random()+100;
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.add(tmp);
	}
	
	public void live(ArrayList<int[]> nourriture)
	{
		//recherche de nourriture la plus proche
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
		
		double[][] in = {{((float)faim)/faimMax, (bassin.tailleX - oeilD )/bassin.tailleX, ((oeilG)-bassin.tailleX)/bassin.tailleX}};
		//System.out.println();
		inputs = new Matrix(in);
		//inputs.show("inputs");
		mid = inputs.times(this.itoh).masque(this.fito).timesTanH();
		//mid.show("mid");
		outputs = mid.times(this.htoo).masque(this.fhto).timesTanH();
		faim++;
		if( norme(nourriture.get(indexRecord)) < 15)
		{
			//System.out.println("Avant manger : " + faim);
			manger(300);
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
		
		if(outputs.get(0, 1) > 0)
		{
			turnRight();
			patteD = Color.RED;
		}
		else
			patteD = Color.GRAY;
		
		if(outputs.get(0, 2) > 0)
		{
			turnLeft();
			patteG = Color.RED;
		}
		else
			patteG = Color.GRAY;
		
		testMort();
	}
	
	public Creature fusion(Creature mere)
	{
		Creature child = new Creature(this.bassin);
		child.fhto = this.fhto.fusion(mere.fhto);
		child.itoh = this.itoh.fusion(mere.itoh);
		child.htoo = this.htoo.fusion(mere.htoo);
		child.fito = this.fito.fusion(mere.fito);
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
	
	private void manger(int qtt)
	{
		faim -= qtt;
		addScore(30);
		testMort();
	}
	
	private void forward()
	{
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
		angle += 2; controlAngle();
		//System.out.println("turnleft");
		int[] tmp = {(int)( x-10*Math.sin(-Math.toRadians(angle))),(int)( y+10*Math.cos(-Math.toRadians(angle)))};
		coordsQueue.add(0,tmp);
		manageQueue();
	}
	
	private void turnRight()
	{
		angle -= 2; controlAngle();
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

		int xOeilD = (int)getX() + (int)(30*(-Math.sin(-Math.toRadians(getAngle()) + Math.PI + Math.PI/4)));
		int yOeilD = (int)getY() + (int)(30*(Math.cos(-Math.toRadians(getAngle()) + Math.PI + Math.PI/4)));
		int xOeilG = (int)getX() + (int)(30*(-Math.sin(-Math.toRadians(getAngle()) + Math.PI - Math.PI/4)));
		int yOeilG = (int)getY() + (int)(30*(Math.cos(-Math.toRadians(getAngle()) + Math.PI - Math.PI/4)));
		
		if(oeil == -1)
			return Math.sqrt(Math.pow(leX[0] - xOeilG,2) + Math.pow(leX[1] - yOeilG,2));
		else
			return Math.sqrt(Math.pow(leX[0] - xOeilD,2) + Math.pow(leX[1] - yOeilD,2));
	}
	
	public void showBrain()
	{
		itoh.show("inputs to hidden");
		htoo.show("Hidden to Outputs");
		fito.show("Threshold Hidden");
		fhto.show("Threshold Output");
		
	}
	
	public void show()
	{
		inputs.show("inputs");
		mid.show("mid");
		outputs.show("Outputs");
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
	
	public static void writeFile(Creature[] zoo, int size)
	{
		File f = new File ("logEcriture.txt");
		 
		try
		{
		    FileWriter fw = new FileWriter (f);
		    fw.write(size + " ");
		    for(int i = 0; i < size; i++)
		    {
			    fw.write (zoo[i].itoh.toString()+zoo[i].htoo.toString()+zoo[i].fito.toString()+zoo[i].fhto.toString());	
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
	    			for(int quatre = 0; quatre < 4; quatre++)
	    			{
				        c = fr.nextDouble(); im = (int)c; System.out.println(" im : " + im);
				        c = fr.nextDouble(); jm = (int)c; System.out.println(" jm : " + jm);
				        for(int j = 0; j < jm; j++)
				        {
				        	//System.out.println(" j : " + j);
				        	for(int i=0; i < im; i++)
				        	{
					        	//System.out.println(" i : " + i);
				        		c = Double.parseDouble(fr.next()); System.out.println(" c : " + c);
				        		if(quatre == 0)
				        		{
				        			zoo[o].itoh.set(i,j,c);
				        		}
				        		else if(quatre == 1)
				        		{
				        			zoo[o].htoo.set(i,j,c);
				        		}
				        		else if(quatre == 2)
				        		{
				        			zoo[o].fito.set(i,j,c);
				        		}
				        		else if(quatre == 3)
				        		{
				        			zoo[o].fhto.set(i,j,c);
				        		}
				        	}
					    }
	    			}
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
