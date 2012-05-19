package fr.univmlv.IG.BipBip.Tooltip;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

/**
 * This class create an graphic element with differents button or text.
 * 
 * @author sfaychat & djubeau
 */
public class Tooltip extends JComponent {
	private static final long serialVersionUID = 3292202175247078445L;
	
	private final Collection<TooltipListener> tooltipListeners = new ArrayList<TooltipListener>();
	private final ArrayList<JButton> buttons = new ArrayList<JButton>();
	private JLabel label = null;
	private Point.Double coords = new Point.Double(0,0);
	private int width = 82;
	
	private static final int height = 51;
	private static final Image left = ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.LEFT);
	private static final Image right = ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.RIGHT);
	private static final Image arrow = ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.ARROW);
	private static final Image lblBg = ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.BG);

	/**
	 * Create a tooltip with no layout
	 */
	public Tooltip() {
		super();
		this.setOpaque(false);
		this.setLayout(null);
	}
	
	/**
	 * Add an event listener for buttons inside the tooltip
	 * 
	 * @param listener
	 */
	public void addTooltipListener(TooltipListener listener) {
		tooltipListeners.add(listener);
	}
	
	/**
	 * Fire a select event
	 * 
	 * @param index of the button clicked
	 */
	protected void fireSelectedAtIndex(int index) {
		for(TooltipListener listener : tooltipListeners)
			listener.eventSelectedAtIndex(index);
	}
	
	/**
	 * This method reorganize all buttons in the tooltip
	 */
	private void reorganizeButtons() {
		int offset=left.getWidth(null);
		for(JButton btn : buttons) {
			btn.setLocation(offset, 0);
			offset+=btn.getWidth();
		}
		width = offset + right.getWidth(null);
	}
	
	/**
	 * Add a text inside the tooltip, the text replace all buttons.
	 * 
	 * @param text to print
	 * @return the label
	 */
	public JLabel addLabel(String text) {
		label = new JLabel("<html><font color='white'>" + text + "</font></html>");
		label.setFont(new Font("Arial", Font.BOLD, 11));
		label.setLocation(4, 0);
		this.setLabelText(text);
		this.add(label);
		return label;
	}
	
	/**
	 * Set the text label, you must call previously addLabel method.
	 * 
	 * @param text
	 */
	public void setLabelText(String text) {
		if (label!=null) {
			label.setText("<html><font color='white'>" + text + "</font></html>");
			FontMetrics metrics = label.getFontMetrics(label.getFont());
			label.setSize(metrics.stringWidth(text), 40);
			width = label.getWidth() + 8;
		}
	}

	/**
	 * Add a button to the tooltip, this replace all label.
	 * 
	 * @param icon of the button
	 * @param tooltipText text of the TooltipButton print on rollover
	 * 
	 * @return the tooltip button
	 */
	public TooltipButton addButton(Icon icon, String tooltipText) {
		return this.addButton(icon, tooltipText, buttons.size());
	}
	
	/**
	 * Add a button to the tooltip at a specific index
	 * 
	 * @param icon of the button
	 * @param tooltipText text of the TooltipButton print on rollover
	 * @param index
	 * @return
	 */
	public TooltipButton addButton(Icon icon, String tooltipText, final int index) {
		TooltipButton button = new TooltipButton(icon);
        button.setToolTipText(tooltipText);
        button.setSize(32, 0);
		buttons.add(index, button);
		button.setSize(button.getWidth(), 51);
		reorganizeButtons();
		this.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireSelectedAtIndex(index);
			}
		});
		return button;
	}
	
	/**
	 * Set the latitude and longitude of the tooltip
	 * 
	 * @param coords of the tooltip
	 */
	public void setCoords(Point.Double coords) {
		this.coords = coords;
	}
	
	/**
	 * Get the latitude and longitude of the tooltip
	 * 
	 * @return the coordinate of the tooltip
	 */
	public Point.Double getCoords() {
		return this.coords;
	}
	
	/**
	 * Set the position of the tooltip in the JComponent
	 *
	 * @param x
	 * @param y
	 */
	@Override
	public void setLocation(int x, int y) {
		this.setLocation(new Point(x, y));
	}
	
	/**
	 * Set the position of the tooltip in the JComponent
	 *
	 * @param p
	 */
	@Override
	public void setLocation(Point p) {
		super.setLocation(p.x - this.getWidth()/2, p.y - this.getHeight());
	}
	
	/**
	 * Get the position of the tooltip in the JComponent
	 */
	@Override
	public Point getLocation() {
		return new Point(super.getLocation().x + this.getWidth()/2, super.getLocation().y + this.getHeight());
	}
	
	/**
	 * Get the size of the tooltip
	 */
	@Override
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	/**
	 * Get the width of the tooltip
	 */
	@Override
	public int getWidth() {
		return width;
	}
	
	/**
	 * Get the height of the tooltip
	 */
	@Override
	public int getHeight() {
		return height;
	}
	
	/**
	 * Paint component
	 * 
	 * Repaint the background and the arrow of the tooltip
	 */
	@Override
	public void paintComponent(Graphics g) {
		this.setSize(width, height);
		
		Graphics2D g2d = (Graphics2D)g;

		/* Draw tooltip */
		g2d.drawImage(left, 0, 0, null);
		g2d.drawImage(right, this.getWidth() - right.getWidth(null), 0, null);
		
		g2d.drawImage(arrow, super.getWidth()/2 - arrow.getWidth(null)/2, this.getHeight() - arrow.getHeight(null), null);
		
		/* Draw lbl background */
		if (label != null)
			g2d.drawImage(lblBg, label.getLocation().x, label.getLocation().y, label.getWidth(), left.getHeight(null), null);
	}
}
