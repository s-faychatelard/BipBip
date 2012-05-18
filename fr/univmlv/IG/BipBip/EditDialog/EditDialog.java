package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Map.Map;
import fr.univmlv.IG.BipBip.Map.MapPanel;
import fr.univmlv.IG.BipBip.Pin.Pin;
import fr.univmlv.IG.BipBip.Tooltip.Tooltip;

/**
 * @author djubeau & sfaychat This class provides method to draw a dialog window
 *         that allow the user to edit a pin (move its location, change the
 *         event type)
 * 
 */
public class EditDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -2838144934781827987L;

	public enum AnswerEditDialog {
		SAVE, BACK
	}

	private final Event event;
	private Container panel;
	private EditButton saveButton;
	private EditButton backButton;
	private final MapPanel map;
	private final Pin pin;
	private AnswerEditDialog answer = AnswerEditDialog.BACK;

	// TODO
	public AnswerEditDialog getAnswer() {
		return answer;
	}

	public Event getEvent() {
		Event evt = new Event(EventType.ACCIDENT, pin.getCoords().x, pin.getCoords().y);
		return evt;
	}

	public EditDialog(JFrame frame, Event event, boolean modal) {
		super(frame, modal);
		this.setLocationRelativeTo(frame);
		this.setSize(new Dimension(500, 370));

		this.event = event;

		panel = this.getContentPane();
		panel.setBackground(Color.WHITE);

		map = new MapPanel(new Point(0, 0), 13);
		map.getOverlayPanel().setVisible(false);
		map.getControlPanel().setVisible(false);
		map.setUseAnimations(false);

		panel.add(map);
		map.setSize(500, 260); 					// c'est sale, mais avec un peu de chance ça ne se verra pas... Surtout avec un petit commentaire très discret

        /* Tooltip to add other alert */
        JPanel topPanel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
		
        JPanel buttonsTypePanel = new JPanel();
        buttonsTypePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        buttonsTypePanel.add(new EditTypeButton(new ImageIcon(Map.class.getResource("alert-fixe.png")), EditTypeButton.POSITION.LEFT));
        buttonsTypePanel.add(new EditTypeButton(new ImageIcon(Map.class.getResource("alert-mobile.png")), EditTypeButton.POSITION.CENTER));
        buttonsTypePanel.add(new EditTypeButton(new ImageIcon(Map.class.getResource("alert-fixe.png")), EditTypeButton.POSITION.CENTER));
        buttonsTypePanel.add(new EditTypeButton(new ImageIcon(Map.class.getResource("alert-fixe.png")), EditTypeButton.POSITION.RIGHT));

        topPanel.add(buttonsTypePanel, BorderLayout.CENTER);
        
        
		JLabel label = new JLabel("Glisser/déposer le point pour mettre à jour ses coordonnées");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setPreferredSize(new Dimension(500, 35));
		topPanel.add(label, BorderLayout.NORTH);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setOpaque(false);

		buttonsPanel.add(Box.createRigidArea(new Dimension(20, 70)));
		saveButton = new EditButton("Sauvegarder");
		saveButton.addActionListener(this);
		buttonsPanel.add(saveButton);

		buttonsPanel.add(Box.createHorizontalGlue());

		backButton = new EditButton("Retour");
		backButton.addActionListener(this);
		buttonsPanel.add(backButton);

		buttonsPanel.add(Box.createRigidArea(new Dimension(20, 70)));

		panel.add(buttonsPanel, BorderLayout.SOUTH);

		pin = new Pin(new Point.Double(event.getX(), event.getY()),
				event.getType(), "Drag'n drop pour déplacer le point", false);
		pin.setLocation(
				MapPanel.lon2position(pin.getCoords().x, map.getZoom())
						- map.getMapPosition().x,
				MapPanel.lat2position(pin.getCoords().y, map.getZoom())
						- map.getMapPosition().y);
		JLabel lbl = new JLabel(pin.getButton().getIcon());
		lbl.setSize(pin.getButton().getSize());
		pin.remove(pin.getButton());
		pin.add(lbl);
		MouseInputListener inputListener = new MouseInputListener() {
			@Override public void mouseMoved(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				Component source = (Component) pin;
				Point location = source.getLocation();
				location.translate(e.getX(), e.getY());
				pin.setCoords(map.getLongitudeLatitude(new Point(location.x + map.getMapPosition().x, location.y + map.getMapPosition().y)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pin.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				pin.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Component source = (Component) pin;
				Point location = source.getLocation();
				location.translate(e.getX(), e.getY());

				pin.setLocation(location);
				pin.repaint();
			}
		};

		pin.addMouseListener(inputListener);
		pin.addMouseMotionListener(inputListener);

		map.add(pin);
		map.addPropertyChangeListener("mapPosition",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						pin.setLocation(
								MapPanel.lon2position(pin.getCoords().x,
										map.getZoom())
										- map.getMapPosition().x,
								MapPanel.lat2position(pin.getCoords().y,
										map.getZoom())
										- map.getMapPosition().y);
						pin.repaint();
					}
				});
		map.setMapPosition(
				MapPanel.lon2position(event.getX(), map.getZoom())
						- map.getWidth() / 2,
				MapPanel.lat2position(event.getY(), map.getZoom())
						- map.getHeight() / 2);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (saveButton == e.getSource())
			answer = AnswerEditDialog.SAVE;
		
		this.setVisible(false);
	}
}
