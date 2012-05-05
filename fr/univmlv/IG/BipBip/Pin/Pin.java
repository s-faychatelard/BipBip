package fr.univmlv.IG.BipBip.Pin;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import fr.univmlv.IG.BipBip.Tooltip.Tooltip;
import fr.univmlv.IG.BipBip.Tooltip.TooltipListener;

public class Pin extends JPanel {
	
	private static final long serialVersionUID = -4087568813531690419L;
	private final Collection<PinListener> pinListeners = new ArrayList<>();

	public enum PinType {
		FIXE,
		MOBILE,
		ACCIDENT,
		TRAVAUX,
		DIVERS
	}
	
	private static JButton pinButton;
	private final Tooltip tooltipConfirm = new Tooltip();
	private static final ImageIcon fixe = new ImageIcon(Pin.class.getResource("pin-fixe.png"));
	private static final ImageIcon mobile = new ImageIcon(Pin.class.getResource("pin-mobile.png"));
	private static final ImageIcon accident = new ImageIcon(Pin.class.getResource("pin-accident.png"));
	private static final ImageIcon travaux = new ImageIcon(Pin.class.getResource("pin-travaux.png"));
	private static final ImageIcon divers = new ImageIcon(Pin.class.getResource("pin-divers.png"));
	
	public Pin(PinType type, String tooltipText) {
		super();
		
		/* Configure panel */
		this.setOpaque(false);
		this.setLayout(null);
		this.setSize(200, 200);
		
		/* Configure pin button */
		pinButton = new JButton();
		pinButton.setToolTipText(tooltipText);
		int tooltipOffset=0;
		switch (type) {
		case FIXE:
			pinButton.setIcon(fixe);
			tooltipOffset = 5;
			break;
		case MOBILE:
			pinButton.setIcon(mobile);
			tooltipOffset = 10;
			break;
		case ACCIDENT:
			pinButton.setIcon(accident);
			tooltipOffset = 8;
			break;
		case TRAVAUX:
			pinButton.setIcon(travaux);
			tooltipOffset = 10;
			break;
		case DIVERS:
			pinButton.setIcon(divers);
			tooltipOffset = 5;
			break;
		default:
			assert(1==0);
			break;
		}
		pinButton.setSize(pinButton.getIcon().getIconWidth(), pinButton.getIcon().getIconHeight());
		pinButton.setText("");
		pinButton.setOpaque(false);
		pinButton.setContentAreaFilled(false);
		pinButton.setBorderPainted(false);
		pinButton.setFocusable(false);
		pinButton.setRolloverEnabled(true);
		this.add(pinButton);
		
		/* Configure tooltip */
		tooltipConfirm.addButton(new ImageIcon(Pin.class.getResource("confirm-yes.png")), null);
        tooltipConfirm.addButton(new ImageIcon(Pin.class.getResource("confirm-no.png")), null).setLast(true);
        
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
		
		/* Positioned and set size of elements into the panel */
		this.setSize(tooltipConfirm.getWidth(), tooltipConfirm.getHeight() + pinButton.getHeight());
		pinButton.setLocation(this.getWidth()/2 - pinButton.getWidth()/2, this.getHeight() - pinButton.getHeight());
		tooltipConfirm.setLocation(pinButton.getX() + pinButton.getWidth()/2, pinButton.getY() + tooltipOffset);
	}
	
	public void addPinListener(PinListener listener) {
		pinListeners.add(listener);
	}
	
	public void fireSelected() {
		for(PinListener listener : pinListeners)
			listener.eventSelected();
	}
	
	public void fireConfirm(boolean confirm) {
		for(PinListener listener : pinListeners)
			listener.eventConfirm(confirm);
	}
	
	public void clear() {
		this.remove(tooltipConfirm);
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
