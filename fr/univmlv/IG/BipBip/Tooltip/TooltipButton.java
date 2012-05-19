package fr.univmlv.IG.BipBip.Tooltip;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class TooltipButton extends JButton {
	private static final long serialVersionUID = -202732288388163416L;
	
	private boolean isLast = false;
	
	/**
	 * Create a TooltipButton
	 * 
	 * @param icon of the button
	 */
	public TooltipButton(Icon icon) {
		super();
	
		this.setIcon(icon);
		this.setText("");
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
		this.setRolloverEnabled(true);
		this.setMinimumSize(new Dimension(20, 51));
	}
	
	/**
	 * Set has last and do not print separator on the button
	 * @param isLast
	 */
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	
	/**
	 * Paint component
	 */
	@Override
	public void paintComponent(Graphics g) {		
		Graphics2D g2d = (Graphics2D)g; 
		Image separator = ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.SEPARATOR);
		g2d.drawImage(ResourcesManager.getRessourceAsImage(ImageNames.Tooltip.BG), 0, 0, this.getWidth(), this.getHeight(), null);
		if(!isLast)
			g2d.drawImage(separator, this.getWidth() - separator.getWidth(null), 0, null);

		super.paintComponent(g);
	}
}
