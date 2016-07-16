import java.util.ArrayList;

public class Brain
{
	
	private ArrayList<Matrix> weigths = new ArrayList<Matrix>(); 	//passage
	private ArrayList<Matrix> states = new ArrayList<Matrix>(); 	//Etats
	
	//private int nbHiddens;
	
	public Brain(Brain copy)
	{
		for(Matrix m : copy.weigths)
		{
			weigths.add(new Matrix(m));
		}
		for(Matrix m : copy.states)
		{
			states.add(new Matrix(m));
		}
	}
	
	public Brain(ArrayList<Matrix> list)
	{
		for(Matrix m : list)
		{
			weigths.add(new Matrix(m));
			states.add(new Matrix(1,m.getSize()[0]));
		}
		states.add(new Matrix(1,list.get(list.size()-1).getSize()[1]));
		
		show();
	}
	
	public Brain(int inputs, int outputs, int nHidden, int hiddenSize)
	{
		//nbHiddens = nHidden;
		if(nHidden!=0)
		{
			weigths.add(Matrix.random(inputs, hiddenSize));
			states.add(new Matrix(1,inputs));
			
			if(nHidden >= 1){states.add(new Matrix(1,hiddenSize));}
			
			for(int i = 0; i<nHidden-1;i++)
			{
				weigths.add(Matrix.random(hiddenSize,hiddenSize));
				states.add(new Matrix(1,hiddenSize));
			}
			weigths.add(Matrix.random(hiddenSize, outputs));
			states.add(new Matrix(1,outputs));
		}
		else
		{
			weigths.add(Matrix.random(inputs, outputs));
			states.add(new Matrix(1,inputs));
			states.add(new Matrix(1,outputs));
		}
		
	}
	
	public Matrix compute(Matrix inputs)
	{
		states.get(0).set(inputs);
		for(int i = 1; i < states.size(); i++)
		{
			states.get(i).set(states.get(i-1).times(weigths.get(i-1)));
			states.get(i).timesTanH();
			//Pour la paralysie neuronale, enlever la moyenne des n derniers -> passe haut
		}
		
		//states.get(1).set(states.get(0).times(weigths.get(0)));
		//states.get(2).set(states.get(1).times(weigths.get(1)));
		
		return states.get(states.size()-1);
	}
	
	static public Brain fusion(Brain pap, Brain mam)
	{
		Brain child = new Brain(pap);
		if(pap.weigths.size() != mam.weigths.size())return null;
		
		for(int i = 0; i < pap.weigths.size() ; i++)
		{
			child.weigths.get(i).set(pap.weigths.get(i).fusion(mam.weigths.get(i)));
		}
		//child.show();
		return child;
	}
	
	public String toString()
	{
		String s = "";
		for(Matrix m : weigths)
		{
			s+=m.toString();
		}
		return s;
	}
	
	public String getType()
	{
		return (weigths.size() + " ");
	}
	
	public void show()
	{
		System.out.println("Weigths :");
		for(Matrix m : weigths)
		{
			m.show();
		}
		System.out.println("\nStates :");
		
		for(Matrix m : states)
		{
			m.show();
		}
		System.out.println("\n\n");
	}
	
}
