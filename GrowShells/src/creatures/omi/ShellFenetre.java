package creatures.omi;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import creatures.omi.mainpanel.MainPanel;
import creatures.omi.mainpanel.MainPanelCtrl;

@SuppressWarnings("serial")
public class ShellFenetre extends JFrame
{
	private JPanel container = new JPanel();
	private MainPanel sim;
	
	
	public ShellFenetre()
	{
		setVisible(true);		
		setTitle("Shells");
		
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int choice = 0;
		
		String[] userChoice = {"from scratch", "Resume previous run", "Load intelligent ones"};
		choice = JOptionPane.showOptionDialog(null, "Start Condition","Shells Launcher", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, userChoice,userChoice[2]);
		System.out.println(choice);
		
		container.setLayout(new BorderLayout());
		sim = new MainPanelCtrl(choice);
		container.add(sim.getView(), BorderLayout.CENTER);
		
		setSize(1000,600);

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
			sim.playRound();
			repaint();
			try{
				Thread.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
