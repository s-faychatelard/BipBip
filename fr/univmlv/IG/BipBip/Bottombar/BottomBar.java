package fr.univmlv.IG.BipBip.Bottombar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univmlv.IG.BipBip.Map.TimelineMap;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

/**
 * This class manage the bottom bar of the application
 * 
 * @author djubeau & sfaychat
 */
public class BottomBar extends JComponent {
	private static final long serialVersionUID = -2860351564159893223L;
	
	private final TimeSlider timeSlider;
		
	/**
	 * Create the bottom bar
	 * 
	 * @param overlayPanel, this parameter permit to the TimeSlider to print its tooltip
	 * @param timelineMap, the map for the TimeSlider
	 */
	public BottomBar(JPanel overlayPanel, TimelineMap timelineMap) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(null);
		
		/* Time slider */
		timeSlider = new TimeSlider(overlayPanel, timelineMap);
	}
	
	/**
	 * Add the TimeSlider to the bottom bar
	 * @param timeSlider
	 */
	public void addTimeSlider(TimeSlider timeSlider) {
		this.removeAll();
		timeSlider.setPreferredSize(new Dimension(this.getWidth() - 150, this.getHeight() - 10));
		
		this.add(Box.createRigidArea(new Dimension(85,0)));
		this.add(timeSlider);
		this.add(Box.createRigidArea(new Dimension(85,0)));
	}
	
	/**
	 * Add a text to the bottom bar
	 * @param text
	 */
	public void addText(String text) {
		this.removeAll();
		JLabel label = new JLabel(text);
		label.setBorder(null);
		this.add(Box.createHorizontalGlue());
		this.add(label);
		this.add(Box.createHorizontalGlue());
	}
	
	/**
	 * Get the TimeSlider
	 * 
	 * @return the current TimeSlider
	 */
	public TimeSlider getTimeSlider() {
		return this.timeSlider;
	}
	
	/**
	 * Paint component
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		/* Draw background */
		g2d.drawImage(ResourcesManager.getRessourceAsImage(ImageNames.General.BOTTOMBAR_BG), 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
