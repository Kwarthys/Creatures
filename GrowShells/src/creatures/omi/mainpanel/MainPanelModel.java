package creatures.omi.mainpanel;

import java.util.ArrayList;
import java.util.Iterator;

import creatures.model.Creature;
import creatures.omi.mainpanel.brainpanel.BrainWindow;
import creatures.utils.Formulas;
import creatures.utils.Organizer;
import utils.Matrix;

public class MainPanelModel {
	protected ArrayList<Creature> zoo = new ArrayList<Creature>();
	protected Organizer<Creature> registre = new Organizer<>();
	protected ArrayList<int[]> frigo = new ArrayList<int[]>(); 

	protected int tailleX;
	protected int tailleY;

	protected int xScreen = 0, yScreen = 0;
	
	protected int pressedX = 0,pressedY = 0;
	
	protected int population;
	protected int popstack;
	protected double temps = 0;
	protected double tpsReprod = 0;
	
	protected int qttBouffe;
	
	protected Integer oldMoyenne;
	
	protected ArrayList<Integer> evols = new ArrayList<Integer>();
	
	protected int step;
	protected BrainWindow brainWindowOpened;
	
	protected void manageFrigo()
	{
		while(frigo.size() <= qttBouffe)
		{
			int[] tmp = {(int)(tailleX*Math.random()+50), (int)(tailleY*Math.random()+50)};
			frigo.add(tmp);
		}
	}
	
	protected void eaten(int bouffe)
	{
		frigo.remove(bouffe);
		manageFrigo();
	}
	
	protected void live()
	{
		zoo.forEach((Creature c) -> c.live(frigo));
		cleanDeaths();
	}
	
	protected void cleanDeaths()
	{
		Iterator<Creature> i = zoo.iterator();
		while(i.hasNext())
		{
			Creature c = i.next();
			if(!c.isAlive())
			{
				registre.add(c);
				i.remove();
			}
		}
		
		manageRegistre();
		reproduction();
	}
	
	protected void manageRegistre()
	{
		Organizer<Creature> tmp = new Organizer<>();
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
		Matrix.setDiv(Formulas.scaleEvols(totalEvols));
		
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
	
	protected void reproduction()
	{	
		while(zoo.size()<population && temps - tpsReprod >= 0)
		{
			//System.out.println("Size " + zoo.size());
			int indexPere, indexMere;
			if(Formulas.random(100)>=60)
				indexPere = 0;
			else if(Formulas.random(100)>=60)
				indexPere = Formulas.random((registre.size()-1)/600);
			else if(Formulas.random(100)>=60)
				indexPere = Formulas.random((registre.size()-1)/300);
			else if(Formulas.random(100)>=60)
				indexPere = Formulas.random((registre.size()-1)/100);
			else
				indexPere = Formulas.random((registre.size()-1)/10);
			//Pere determiné
			do
			{
				//System.out.println("Recherche de la mere");
				if(Formulas.random(100) >= 70)
					indexMere = 0;
				else if(Formulas.random(100)>=70)
					indexMere = Formulas.random((registre.size()-1)/500);
				else if(Formulas.random(100)>=70)
					indexMere = Formulas.random((registre.size()-1)/100);
				else if(Formulas.random(100)>=70)
					indexMere = Formulas.random((registre.size()-1)/50);
				else if(Formulas.random(100)>=70)
					indexMere = Formulas.random((registre.size()-1)/4);
				else
					indexMere = Formulas.random((registre.size()-1)/2);	
				//System.out.println("Index pere / mere : " + indexPere + " / " + indexMere);	
			}while(indexPere == indexMere); //Mere determinée
			
			zoo.add(registre.get(indexPere).fusion(registre.get(indexMere)));
			//System.out.println("Index pere / mere : " + indexPere + " / " + indexMere);
			
			
			tpsReprod = temps;
			
		}	
	}
}
