/**
 * ESIPE Project - IR2 2011/2012 - IG
 * Copyright (C) 2012 ESIPE - Universite Paris-Est Marne-la-Vallee
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Please see : http://www.gnu.org/licenses/gpl.html
 *
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
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
 * This class provides method to draw a dialog window
 * that allow the user to edit a pin (move its location, change the event type)
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

	/**
	 * Get the answer of the Dialog (Save or Back)
	 * 
	 * @return the answer
	 */
	public AnswerEditDialog getAnswer() {
		return answer;
	}

	/**
	 * Get the edited event
	 * 
	 * @return the edited event
	 */
	public Event getEvent() {
		Event evt = new Event(pin.getType(), pin.getEvent().getX(), pin.getEvent().getY());
		return evt;
	}
	
	/**
	 * ActionListener of the Save and Back buttons
	 */
	public void actionPerformed(ActionEvent e) {
		if (saveButton == e.getSource())
			answer = AnswerEditDialog.SAVE;

		this.setVisible(false);
	}

	/**
	 * Create an EditDialog for a specific event
	 * 
	 * @param frame where to open the Dialog
	 * @param event which you want to edit
	 * @param modal to specify if it is a modal Dialog
	 */
	public EditDialog(JFrame frame, Event event, boolean modal) {
		super(frame, modal);
		
		Event evt = new Event(event.getType(), event.getX(), event.getY());
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
		map.setSize(500, 260);

		pin = new Pin(evt, "Drag'n drop pour déplacer le point", false);

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Event evt = pin.getEvent();
				Event newTypeEvent = new Event(((EditTypeButton) e.getSource()).getType(), evt.getX(), evt.getY());
				pin.setEvent(newTypeEvent);
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

		pin.setLocation(MapPanel.lon2position(pin.getEvent().getX(), map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getEvent().getY(), map.getZoom()) - map.getMapPosition().y);

		/* Listener to drag'n drop the pin */
		MouseInputListener inputListener = new MouseInputAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				Component source = (Component) pin;
				Point location = source.getLocation();
				location.translate(e.getX(), e.getY());
				
				Point.Double coord = map.getLongitudeLatitude(new Point(location.x + map.getMapPosition().x, location.y + map.getMapPosition().y));
				Event evt = pin.getEvent();
				Event newTypeEvent = new Event(evt.getType(), coord.getX(), coord.getY());
				pin.setEvent(newTypeEvent);
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
				pin.setLocation(MapPanel.lon2position(pin.getEvent().getX(), map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getEvent().getY(), map.getZoom()) - map.getMapPosition().y);
				pin.repaint();
			}
		});
		map.setMapPosition( MapPanel.lon2position(event.getX(), map.getZoom()) - map.getWidth() / 2, MapPanel.lat2position(event.getY(), map.getZoom()) - map.getHeight() / 2);

		this.setVisible(true);
	}
}
