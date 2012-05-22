package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Map.MapPanel;
import fr.univmlv.IG.BipBip.Pin.Pin;


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

	private Container panel;
	private EditButton saveButton;
	private EditButton backButton;
	private final MapPanel map;
	private final Pin pin;
	private AnswerEditDialog answer = AnswerEditDialog.BACK;


	public AnswerEditDialog getAnswer() {
		return answer;
	}

	public Event getEvent() {
		Event evt = new Event(pin.getType(), pin.getCoords().x, pin.getCoords().y);
		return evt;
	}

	public EditDialog(JFrame frame, Event event, boolean modal) {
		super(frame, modal);
		this.setLocationRelativeTo(frame);
		this.setMinimumSize(new Dimension(550, 440));
		this.setSize(new Dimension(600, 440));

		panel = this.getContentPane();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets.top = 10;
		gbc.insets.left = 10;
		gbc.insets.right = 10;

		/* Type information label */
		JLabel typeLabel = new JLabel("Choisir le type d'alerte");
		typeLabel.setHorizontalAlignment(JLabel.CENTER);

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = .1f;
		gbc.weighty = .0f;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(typeLabel, gbc);



		/* Type manager buttons */		
		final JPanel buttonsTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
		buttonsTypePanel.setOpaque(false);
		buttonsTypePanel.setMaximumSize(new Dimension(100, 60));
		buttonsTypePanel.setMinimumSize(new Dimension(100, 60));
		buttonsTypePanel.setPreferredSize(new Dimension(100, 60));

		EditTypeButton fixeButton = new EditTypeButton(EventType.RADAR_FIXE, EditTypeButton.POSITION.LEFT);
		EditTypeButton mobileButton = new EditTypeButton(EventType.RADAR_MOBILE);
		EditTypeButton accidentButton = new EditTypeButton(EventType.ACCIDENT);
		EditTypeButton travauxButton = new EditTypeButton(EventType.TRAVAUX);
		EditTypeButton diversButton = new EditTypeButton(EventType.DIVERS, EditTypeButton.POSITION.RIGHT);

		ButtonGroup group = new ButtonGroup();
		group.add(fixeButton);
		group.add(mobileButton);
		group.add(accidentButton);
		group.add(travauxButton);
		group.add(diversButton);

		buttonsTypePanel.add(fixeButton);
		buttonsTypePanel.add(mobileButton);
		buttonsTypePanel.add(accidentButton);
		buttonsTypePanel.add(travauxButton);
		buttonsTypePanel.add(diversButton);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.weightx = .9f;
		gbc.weighty = .0f;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(buttonsTypePanel, gbc);

		/* Map information label */
		JLabel mapLabel = new JLabel("Glisser/déposer le point pour mettre à jour ses coordonnées");
		mapLabel.setHorizontalAlignment(JLabel.CENTER);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.weightx = 1.f;
		gbc.weighty = .0f;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(mapLabel, gbc);

		/* Map to change position */
		map = new MapPanel(new Point(0, 0), 13);
		map.getOverlayPanel().setVisible(false);
		map.getControlPanel().setVisible(false);
		map.setUseAnimations(false);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.weightx = 1.f;
		gbc.weighty = 1.f;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(map, gbc);

		/* Save/Back buttons */
		saveButton = new EditButton("Sauvegarder");
		saveButton.addActionListener(this);

		gbc.gridwidth = 1;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = .0f;
		gbc.weighty = .0f;
		gbc.insets.bottom = 20;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(saveButton, gbc);

		JPanel emptyPanel = new JPanel();
		emptyPanel.setOpaque(false);
		gbc.gridwidth = 2;
		gbc.weightx = 1.f;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(emptyPanel, gbc);

		backButton = new EditButton("Retour");
		backButton.addActionListener(this);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = .0f;
		gbc.weighty = .0f;
		gbc.insets.bottom = 20;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(backButton, gbc);

		/* All listener */
		map.setSize(500, 260); 						// c'est sale, mais avec un peu de chance ça ne se verra pas... Surtout avec un petit commentaire très discret

		pin = new Pin(new Point.Double(event.getX(), event.getY()), event.getType(), "Drag'n drop pour déplacer le point", false);

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pin.setType(((EditTypeButton) e.getSource()).getType());
			}
		};

		fixeButton.addActionListener(listener);
		mobileButton.addActionListener(listener);
		accidentButton.addActionListener(listener);
		travauxButton.addActionListener(listener);
		diversButton.addActionListener(listener);

		switch (pin.getType()) {
		case RADAR_FIXE: fixeButton.setSelected(true); break;
		case RADAR_MOBILE: mobileButton.setSelected(true); break;
		case ACCIDENT: accidentButton.setSelected(true); break;
		case TRAVAUX: travauxButton.setSelected(true); break;
		case DIVERS: diversButton.setSelected(true); break;
		default : throw new IllegalStateException();
		}

		pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);

		MouseInputListener inputListener = new MouseInputAdapter() {

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

		pin.getButton().addMouseListener(inputListener);
		pin.getButton().addMouseMotionListener(inputListener);

		map.add(pin);
		map.addPropertyChangeListener("mapPosition", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
				pin.repaint();
			}
		});
		map.setMapPosition( MapPanel.lon2position(event.getX(), map.getZoom()) - map.getWidth() / 2, MapPanel.lat2position(event.getY(), map.getZoom()) - map.getHeight() / 2);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (saveButton == e.getSource())
			answer = AnswerEditDialog.SAVE;

		this.setVisible(false);
	}
}
