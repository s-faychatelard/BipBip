/**
 * ESIPE Project - IR2 2011/2012 - IG
 * Copyright (C) 2012 ESIPE - Universite Paris-Est Marne-la-Vallee
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Please see : http://www.gnu.org/licenses/gpl.html
 *
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
package fr.univmlv.IG.BipBip.Tooltip;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;
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
