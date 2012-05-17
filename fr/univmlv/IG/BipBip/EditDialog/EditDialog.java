package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Map.MapPanel;
import fr.univmlv.IG.BipBip.Pin.Pin;

/**
 * @author djubeau & sfaychat
 * This class provides method to draw a dialog window that allow the user
 * to edit a pin (move its location, change the event type) 
 *
 */
public class EditDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -2838144934781827987L;
	
	public enum AnswerEditDialog {
		SAVE,
		BACK
	}

	private final Event event;
	private Container panel;
	private JButton saveButton;
	private JButton backButton;
	private final MapPanel map;

	
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
		this.setLocationRelativeTo(frame);		
		this.setSize(new Dimension(500, 350));

		this.event = event;
		
		panel = this.getContentPane();
		panel.setBackground(Color.WHITE);

		
		map = new MapPanel(new Point(0,0), 13);
        map.getOverlayPanel().setVisible(false);
        map.getControlPanel().setVisible(false);
        map.setUseAnimations(false);
        
		panel.add(map);
		map.setSize(500, 260);														 // c'est sale, mais avec un peu de chance ça ne se verra pas... Surtout avec un petit commentaire très discret
	
        JLabel label = new JLabel("Glisser/déposer le point pour mettre à jour ses coordonnées");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setPreferredSize(new Dimension(500, 35));
		panel.add(label, BorderLayout.NORTH);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setOpaque(false);
		
		
		buttonsPanel.add(Box.createRigidArea(new Dimension(20, 50)));
		saveButton = new JButton("Sauvegarder");
		saveButton.addActionListener(this);
		buttonsPanel.add(saveButton);	
		
		buttonsPanel.add(Box.createHorizontalGlue());
		
		backButton = new JButton("Retour");
		backButton.addActionListener(this);
		buttonsPanel.add(backButton);
		
		buttonsPanel.add(Box.createRigidArea(new Dimension(20, 50)));

		panel.add(buttonsPanel, BorderLayout.SOUTH);
		
		final Pin pin = new Pin(new Point.Double(event.getX(), event.getY()), event.getType(), "Drag'n drop pour déplacer le point", false);
        pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
        pin.getButton().addMouseMotionListener(new MouseMotionListener() {
			
			@Override public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				pin.setLocation(e.getPoint());
				pin.repaint();
			}
		});
        
        map.add(pin);
        map.addPropertyChangeListener("mapPosition", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
				pin.repaint();
			}
		});
		map.setMapPosition(MapPanel.lon2position(event.getX(), map.getZoom()) - map.getWidth()/2, MapPanel.lat2position(event.getY(), map.getZoom())- map.getHeight()/2);

		this.setVisible(true);
		System.out.println("toto" +map.getSize());

	 }

	 public void actionPerformed(ActionEvent e) {
		if(saveButton == e.getSource()) {
		    System.err.println("User chose yes.");
		    this.setVisible(false);
		}
		else if(backButton == e.getSource()) {
		    System.err.println("User chose no.");
		    this.setVisible(false);
		}
	 }
}
