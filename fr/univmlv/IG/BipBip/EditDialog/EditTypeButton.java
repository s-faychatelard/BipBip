package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class EditTypeButton extends JToggleButton {
	private static final long serialVersionUID = -7685436527591441736L;

	enum POSITION {
		CENTER, LEFT, RIGHT
	}

	private static final Image left = ResourcesManager.getRessourceAsImage(ImageNames.EditButton.LEFT);
	private static final Image right = ResourcesManager.getRessourceAsImage(ImageNames.EditButton.RIGHT);
	private static final Image bg = ResourcesManager.getRessourceAsImage(ImageNames.EditButton.BG);
	private static final Image sep = ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.SEPARATOR);

	
	private final EditTypeButton.POSITION position;
	private final EventType type;

	
	public EditTypeButton(EventType type, EditTypeButton.POSITION position) {
		super(getImageIconFromType(type));
		this.type = type;
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setRolloverEnabled(false);
		this.setMargin(new Insets(25, 15, 15, 15));
		this.position = position;
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	public EditTypeButton(EventType type) {
		this(type, EditTypeButton.POSITION.CENTER);
	}
	
	private static ImageIcon getImageIconFromType(EventType type) {
		ImageIcon icon;
		switch (type) {
			case ACCIDENT: icon = ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.ACCIDENT); break;
			case DIVERS: icon = ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.DIVERS); break;
			case RADAR_FIXE: icon = ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.FIXE); break;
			case RADAR_MOBILE: icon = ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.MOBILE); break;
			default: icon = ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.TRAVAUX); break;
		}
		return icon;
	}

	public EventType getType() {
		return type;
	}
	
	/**
	 * Paint component
	 */
	@Override
	public void paintComponent(Graphics g) {
		this.setSize(this.getWidth(), 40);
		Graphics2D g2d = (Graphics2D)g; 
		
		if(isSelected())  {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		}

		if(position == POSITION.LEFT) {
			g2d.drawImage(left, 0, 0, null);
			g2d.drawImage(bg, left.getWidth(null), 0, this.getWidth() - left.getWidth(null), this.getHeight(), null);
			g2d.drawImage(sep, this.getWidth() - sep.getWidth(null), 0, null);
		}
		else if(position == POSITION.RIGHT) {
			g2d.drawImage(right, this.getWidth() - right.getWidth(null), 0, null);
			g2d.drawImage(bg, 0, 0, this.getWidth() - right.getWidth(null), this.getHeight(), null);
		}
		else {
			g2d.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);
			g2d.drawImage(sep, this.getWidth() - sep.getWidth(null), 0, null);
		}

		super.paintComponent(g);
	}
}
