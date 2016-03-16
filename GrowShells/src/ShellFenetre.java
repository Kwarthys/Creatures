import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ShellFenetre extends JFrame
{
	private JPanel container = new JPanel();
	private BassinGenetique sim;
	
	
	public ShellFenetre()
	{
		//Creature cre = new Creature();
		setVisible(true);		
		setTitle("Shells");

		
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int rang = 0;
		
		String[] sexe = {"from scratch", "Resume previous run", "Load intelligent ones"};
		rang = JOptionPane.showOptionDialog(null, "Start Condition","Shells Launcher", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, sexe,sexe[2]);
		System.out.println(rang);
		
		container.setLayout(new BorderLayout());
		sim = new BassinGenetique(rang);
		container.add(sim, BorderLayout.CENTER);
		
		setSize(sim.tailleX+100,sim.tailleY+100);

		this.setContentPane(container);
		
		addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		         sim.save();
		         System.out.println("saved");
		    }
		});
		
		setVisible(true);
		this.setLocationRelativeTo(null);		
		
		System.out.println("lancement !");
		go();
	}
	
	private void go()
	{
		while(true)
		{
			//System.out.println("ça tourne");
			repaint();
			try{
				Thread.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
