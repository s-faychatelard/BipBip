package fr.univmlv.IG.BipBip.Tooltip;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TooltipButton extends JButton {
	private static final long serialVersionUID = -202732288388163416L;
	
	private boolean isLast = false;
	
	private static final Image bg = new ImageIcon(Tooltip.class.getResource("tooltip-bg.png")).getImage();
	private static final Image sep = new ImageIcon(Tooltip.class.getResource("tooltip-sep.png")).getImage();
	
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
	
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	
	@Override
	public void paintComponent(Graphics g) {		
		Graphics2D g2d = (Graphics2D)g; 

		g2d.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);
		if(!isLast)
			g2d.drawImage(sep, this.getWidth() - sep.getWidth(null), 0, null);

		super.paintComponent(g);
	}
}
