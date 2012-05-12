package fr.univmlv.IG.BipBip.Bottombar;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.text.SimpleDateFormat;

import fr.univmlv.IG.BipBip.Map.TimelineMap;
import fr.univmlv.IG.BipBip.Tooltip.Tooltip;

public class TimeSlider extends JComponent {

	private static final long serialVersionUID = 7634340909155886872L;

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	private final TimelineMap timelineMap;
	private final Tooltip tooltipText = new Tooltip();
	private long min = 0;
	private long max = 1;
	
	private static final Image left = new ImageIcon(BottomBar.class.getResource("timeslider-left.png")).getImage();
	private static final Image right = new ImageIcon(BottomBar.class.getResource("timeslider-right.png")).getImage();
	private static final Image bg = new ImageIcon(BottomBar.class.getResource("timeslider-bg.png")).getImage();
	private static final Image handle = new ImageIcon(BottomBar.class.getResource("timeslider-handle.png")).getImage();

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
	
	private void updatePosition(int mouseX) {
		int posX = mouseX;
		if (posX + TimeSlider.this.getX() < TimeSlider.this.getX() + TimeSlider.this.getParent().getX() + 8) {
			posX = TimeSlider.this.getParent().getX() + 8;
		}
		else if (posX + TimeSlider.this.getX() > TimeSlider.this.getX() + TimeSlider.this.getParent().getX() + TimeSlider.this.getWidth() - 9) {
			posX = TimeSlider.this.getParent().getX() + TimeSlider.this.getWidth() - 9;
		}
		tooltipText.setLocation(posX + TimeSlider.this.getX(), TimeSlider.this.getY() + TimeSlider.this.getParent().getY() + handle.getHeight(null)/2);
		updateDate(posX);
	}
	
	private void updateDate(int posX) {
		long difference = max-min;
		long date = (difference / (TimeSlider.this.getWidth() - 8 - 9)) * posX;
		tooltipText.setLabelText(dateFormatter.format(min + date));
		tooltipText.setLocation(posX + TimeSlider.this.getX(), TimeSlider.this.getY() + TimeSlider.this.getParent().getY() + handle.getHeight(null)/2);
		tooltipText.repaint();
		this.timelineMap.setTime(min + date);
	}
	
	public void setMinAndMax(long min, long max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		/* Draw tooltip */
		g2d.drawImage(left, 0, this.getHeight()/2 - left.getHeight(null)/2, null);
		g2d.drawImage(right, this.getWidth() - right.getWidth(null), this.getHeight()/2 - right.getHeight(null)/2, null);
		g2d.drawImage(bg, left.getWidth(null), this.getHeight()/2 - bg.getHeight(null)/2, this.getWidth() - left.getWidth(null) - right.getWidth(null), bg.getHeight(null), null);
		g2d.drawImage(handle, tooltipText.getLocation().x - this.getX() - handle.getWidth(null)/2, this.getHeight()/2 - handle.getHeight(null)/2, null);
	}
}
