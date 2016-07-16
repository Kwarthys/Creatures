import java.util.ArrayList;

public class Brain
{
	
	private ArrayList<Matrix> weigths = new ArrayList<Matrix>(); 	//passage
	private ArrayList<Matrix> states = new ArrayList<Matrix>(); 	//Etats
	
	//private int nbHiddens;
	
	public Brain(Brain copy)
	{
		weigths = new ArrayList<Matrix>(copy.weigths);
		states = new ArrayList<Matrix>(copy.states);
	}
	
	public Brain(int inputs, int outputs, int nHidden, int hiddenSize)
	{
		//nbHiddens = nHidden;
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
			Matrix toCreate = child.weigths.get(i);
			toCreate = child.weigths.get(i).fusion(child.weigths.get(i));
		}
		
		return child;
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
