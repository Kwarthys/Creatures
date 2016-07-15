import java.util.ArrayList;

public class Brain
{
	
	private ArrayList<Matrix> weigths; 	//passage
	private ArrayList<Matrix> states; 	//Etats
	
	//private int nbHiddens;
	
	
	public Brain(int inputs, int outputs, int nHidden, int hiddenSize)
	{
		//nbHiddens = nHidden;
		weigths.add(new Matrix(inputs, hiddenSize));
		states.add(new Matrix(1,inputs));
		
		if(nHidden >= 1){states.add(new Matrix(1,hiddenSize));}
		
		for(int i = 0; i<nHidden-1;i++)
		{
			weigths.add(new Matrix(hiddenSize,hiddenSize));
			states.add(new Matrix(1,hiddenSize));
		}
		weigths.add(new Matrix(nHidden, outputs));
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
	
}
