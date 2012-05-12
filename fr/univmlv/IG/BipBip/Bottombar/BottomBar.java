package fr.univmlv.IG.BipBip.Bottombar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univmlv.IG.BipBip.Map.TimelineMap;

public class BottomBar extends JComponent {
	private static final long serialVersionUID = -2860351564159893223L;
	
	private final TimeSlider timeSlider;
	
	private static final Image bg = new ImageIcon(BottomBar.class.getResource("bottombar-bg.png")).getImage();
	
	public BottomBar(JPanel overlayPanel, TimelineMap timelineMap) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(null);
		
		/* Time slider */
		timeSlider = new TimeSlider(overlayPanel, timelineMap);
	}
	
	public void addTimeSlider(TimeSlider timeSlider) {
		this.removeAll();
		timeSlider.setPreferredSize(new Dimension(this.getWidth() - 150, this.getHeight() - 10));
		
		this.add(Box.createRigidArea(new Dimension(85,0)));
		this.add(timeSlider);
		this.add(Box.createRigidArea(new Dimension(85,0)));
	}
	
	public void addText(String text) {
		this.removeAll();
		JLabel label = new JLabel(text);
		label.setBorder(null);
		this.add(Box.createHorizontalGlue());
		this.add(label);
		this.add(Box.createHorizontalGlue());
	}
	
	public TimeSlider getTimeSlider() {
		return this.timeSlider;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		/* Draw background */
		g2d.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
