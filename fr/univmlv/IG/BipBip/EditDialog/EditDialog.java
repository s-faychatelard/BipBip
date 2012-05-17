package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;

public class EditDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -2838144934781827987L;
	
	public enum AnswerEditDialog {
		SAVE,
		BACK
	}

	private final Event event;
	private JPanel panel = null;
	private EditButton yesButton = null;
	private EditButton noButton = null;
	
	//TODO
	public AnswerEditDialog getAnswer() {
		return AnswerEditDialog.SAVE;
	}
	
	public Event getEvent() {
		Event evt = new Event(EventType.ACCIDENT, this.event.getX(), this.event.getY());
		return evt;
	}

	public EditDialog(JFrame frame, Event event, boolean modal) {
		super(frame, modal);
		
		this.event = event;
		
		panel = new JPanel();
		this.getContentPane().add(panel);
		panel.add(new JLabel("Toto est beau ?"));
		
		yesButton = new EditButton("Yes");
		yesButton.addActionListener(this);
		panel.add(yesButton);	
		
		noButton = new EditButton("No");
		noButton.addActionListener(this);
		panel.add(noButton);
		
		this.pack();
		this.setLocationRelativeTo(frame);
		this.setVisible(true);
	 }

	 public void actionPerformed(ActionEvent e) {
		if(yesButton == e.getSource()) {
		    System.err.println("User chose yes.");
		    this.setVisible(false);
		}
		else if(noButton == e.getSource()) {
		    System.err.println("User chose no.");
		    this.setVisible(false);
		}
	 }
}
