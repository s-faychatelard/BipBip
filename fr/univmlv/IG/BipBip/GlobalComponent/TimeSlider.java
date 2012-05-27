package fr.univmlv.IG.BipBip.GlobalComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.univmlv.IG.BipBip.Map.TimelineMap;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;
import fr.univmlv.IG.BipBip.Tooltip.Tooltip;

public class TimeSlider extends JComponent {

	private static final long serialVersionUID = 7634340909155886872L;
	
	private final TimelineMap timelineMap;
	private final Tooltip tooltipText = new Tooltip();
	
	private static final int NB_DAY_PER_WEEK = 7;
	private static final int NB_HOUR_PER_DAY = 24;
	
	private static final int OFFSET_X = 8;
	private static final int OFFSET_Y = 8;
	
	private static final String[] days = { "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};

	/**
	 * Create a TimeSlider
	 * 
	 * @param overlayPanel, the overlayPanel is for the tooltip of the TimeSlider
	 * @param timelineMap, the map for the TimeSlider
	 */
	public TimeSlider(JPanel overlayPanel, TimelineMap timelineMap) {
		this.timelineMap = timelineMap;
		
        tooltipText.addLabel("  ");
        tooltipText.setLocation(93, 0);
        
        final JPanel overlay = overlayPanel;
        this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				overlay.remove(tooltipText);	
				overlay.repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				updatePosition(e.getX());
				overlay.add(tooltipText);
				overlay.repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// Nothing to do
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// Nothing to do
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// Nothing to do
			}
		});
        
        this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// Nothing to do
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				updatePosition(e.getX());
				TimeSlider.this.repaint();
			}
		});
	}
	
	/**
	 * Call when the mouse is moved
	 * It calculate the day and the hour of the current mouse position
	 * 
	 * @param mouseX is the x position of the mouse
	 */
	private void updatePosition(int mouseX) {
		final Image handle = ResourcesManager.getRessourceAsImage(ImageNames.TimeSlider.HANDLE);

		int posX = mouseX;
		if (posX + TimeSlider.this.getX() < TimeSlider.this.getX() + TimeSlider.this.getParent().getX() + OFFSET_X) {
			posX = TimeSlider.this.getParent().getX() + OFFSET_X;
		}
		else if (posX + TimeSlider.this.getX() > TimeSlider.this.getX() + TimeSlider.this.getParent().getX() + TimeSlider.this.getWidth() - OFFSET_Y) {
			posX = TimeSlider.this.getParent().getX() + TimeSlider.this.getWidth() - OFFSET_Y;
		}
		tooltipText.setLocation(posX + TimeSlider.this.getX(), TimeSlider.this.getY() + TimeSlider.this.getParent().getY() + handle.getHeight(null)/2);
		updateDate(posX);
	}
	
	/**
	 * Call by the updatePosition to update the pins on the TimelineMap
	 * It updates the label of the tooltip and call the timelineMap to update the pins
	 * 
	 * @param posX is the x position of the mouse
	 */
	private void updateDate(int posX) {
		final Image handle = ResourcesManager.getRessourceAsImage(ImageNames.TimeSlider.HANDLE);
		
		int sliderDateSize = NB_DAY_PER_WEEK * NB_HOUR_PER_DAY - 2;
		int date = (int) (((double)(sliderDateSize / (double)(TimeSlider.this.getWidth() - OFFSET_X - OFFSET_Y))) * posX);
		
		int day = (int)(date/NB_HOUR_PER_DAY);
		int hour = (int)(date%NB_HOUR_PER_DAY);

		tooltipText.setLabelText(days[day] + " " + hour + "h");
		tooltipText.setLocation(posX + TimeSlider.this.getX(), TimeSlider.this.getY() + TimeSlider.this.getParent().getY() + handle.getHeight(null)/2);
		tooltipText.repaint();
		this.timelineMap.setTime(day+1, hour);
	}
	
	/**
	 * Paint component
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		final Image left = ResourcesManager.getRessourceAsImage(ImageNames.TimeSlider.LEFT);
		final Image right = ResourcesManager.getRessourceAsImage(ImageNames.TimeSlider.RIGHT);
		final Image bg = ResourcesManager.getRessourceAsImage(ImageNames.TimeSlider.BG);
		final Image handle = ResourcesManager.getRessourceAsImage(ImageNames.TimeSlider.HANDLE);
		
		/* Draw tooltip */
		g2d.drawImage(left, 0, this.getHeight()/2 - left.getHeight(null)/2, null);
		g2d.drawImage(right, this.getWidth() - right.getWidth(null), this.getHeight()/2 - right.getHeight(null)/2, null);
		g2d.drawImage(bg, left.getWidth(null), this.getHeight()/2 - bg.getHeight(null)/2, this.getWidth() - left.getWidth(null) - right.getWidth(null), bg.getHeight(null), null);
		g2d.drawImage(handle, tooltipText.getLocation().x - this.getX() - handle.getWidth(null)/2, this.getHeight()/2 - handle.getHeight(null)/2, null);
	}
}
