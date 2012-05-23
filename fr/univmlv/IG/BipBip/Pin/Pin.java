package fr.univmlv.IG.BipBip.Pin;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;
import fr.univmlv.IG.BipBip.Tooltip.Tooltip;
import fr.univmlv.IG.BipBip.Tooltip.TooltipListener;

/**
 * TODO : externaliser les specificités liés aux pins
 *  
 * @author djubeau & sfaychat
 * Pin class is a JComponent used to represent geolocated pins 
 */
public class Pin extends JComponent {

	private static final long serialVersionUID = -4087568813531690419L;
	private final Collection<PinListener> pinListeners = new ArrayList<PinListener>();
	private Event event;

	private final JButton pinButton;
	private final Tooltip tooltipConfirm = new Tooltip();

	public Pin(Event event, String tooltipText) {
		this(event, tooltipText, true);
	}

	public Pin(Event event, String tooltipText, boolean openable) {
		super();

		/* Save coords */
		this.event = event;

		/* Configure panel */
		this.setOpaque(false);
		this.setLayout(null);
		this.setSize(200, 200);

		/* Configure pin button */
		pinButton = new JButton();
		pinButton.setToolTipText(tooltipText);
		int tooltipOffset=0;
		this.refreshType();


		pinButton.setSize(pinButton.getIcon().getIconWidth(), pinButton.getIcon().getIconHeight());
		pinButton.setText("");
		pinButton.setOpaque(false);
		pinButton.setContentAreaFilled(false);
		pinButton.setBorderPainted(false);
		pinButton.setFocusable(false);
		pinButton.setRolloverEnabled(true);
		this.add(pinButton);

		if (openable) {
			/* Configure tooltip */
			tooltipConfirm.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Confirm.YES), null);
			tooltipConfirm.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Confirm.NO), null).setLast(true);

			/* Get click event to open the tooltip */
			pinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireSelected();
					Pin.this.remove(tooltipConfirm);
					Pin.this.add(tooltipConfirm);
					Pin.this.repaint();
				}
			});
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			/* Confirm event */
			tooltipConfirm.addTooltipListener(new TooltipListener() {
				@Override
				public void eventSelectedAtIndex(int index) {
					switch (index) {
					case 0:
						fireConfirm(true);
						break;
					case 1:
						fireConfirm(false);
						break;
					default:
						assert(0==1);
						break;
					}
					Pin.this.remove(tooltipConfirm);
					Pin.this.repaint();
				}
			});
			this.setSize(tooltipConfirm.getWidth(), tooltipConfirm.getHeight() + pinButton.getHeight());
		}
		else {
			this.setSize(pinButton.getWidth(), pinButton.getHeight());
			pinButton.setEnabled(false);
		}

		/* Positioned and set size of elements into the panel */
		pinButton.setLocation(this.getWidth()/2 - pinButton.getWidth()/2, this.getHeight() - pinButton.getHeight());
		tooltipConfirm.setLocation(pinButton.getX() + pinButton.getWidth()/2, pinButton.getY() + tooltipOffset);
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Event getEvent() {
		return this.event;
	}
	
	public EventType getType() {
		return this.event.getType();
	}
	
	public void refreshType() {
		
		final ImageIcon fixe = ResourcesManager.getRessourceAsImageIcon(ImageNames.Pin.FIXE);
		final ImageIcon mobile =  ResourcesManager.getRessourceAsImageIcon(ImageNames.Pin.MOBILE);
		final ImageIcon accident = ResourcesManager.getRessourceAsImageIcon(ImageNames.Pin.ACCIDENT);
		final ImageIcon travaux = ResourcesManager.getRessourceAsImageIcon(ImageNames.Pin.TRAVAUX);
		final ImageIcon divers = ResourcesManager.getRessourceAsImageIcon(ImageNames.Pin.DIVERS);
		
		switch (event.getType()) {
		case RADAR_FIXE:
			pinButton.setIcon(fixe);
			pinButton.setDisabledIcon(fixe);
			//tooltipOffset = 5;
			break;
		case RADAR_MOBILE:
			pinButton.setIcon(mobile);
			pinButton.setDisabledIcon(mobile);
			//tooltipOffset = 10;
			break;
		case ACCIDENT:
			pinButton.setIcon(accident);
			pinButton.setDisabledIcon(accident);
			//tooltipOffset = 8;
			break;
		case TRAVAUX:
			pinButton.setIcon(travaux);
			pinButton.setDisabledIcon(travaux);
			//tooltipOffset = 10;
			break;
		case DIVERS:
			pinButton.setIcon(divers);
			pinButton.setDisabledIcon(divers);
			//tooltipOffset = 5;
			break;
		default:
			assert(1==0);
			break;
		}
		
	}

	public void addPinListener(PinListener listener) {
		pinListeners.add(listener);
	}

	protected void fireSelected() {
		for(PinListener listener : pinListeners)
			listener.eventSelected();
	}

	protected void fireConfirm(boolean confirm) {
		for(PinListener listener : pinListeners)
			listener.eventConfirm(confirm);
	}

	public void clear() {
		this.remove(tooltipConfirm);
	}
	
	public JButton getButton() {
		return pinButton;
	}

	@Override
	public void setLocation(int x, int y) {
		this.setLocation(new Point(x, y));
	}

	@Override
	public void setLocation(Point p) {
		super.setLocation(p.x - this.getWidth()/2, p.y - this.getHeight() + 5);
	}
}
