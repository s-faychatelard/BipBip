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
package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JButton;

import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class EditButton extends JButton {
	private static final long serialVersionUID = -8973864888152429363L;

	/**
	 * Create an Edit Button
	 * 
	 * @param title of the button
	 */
	public EditButton(String title) {
		super("<html><font color='white'>" + title + "</font></html>");
		this.setFont(new Font("Arial", Font.BOLD, 13));
		this.setSize(this.getWidth(), 40);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
		this.setRolloverEnabled(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	/**
	 * Paint component
	 */
	@Override
	public void paintComponent(Graphics g) {
		this.setSize(this.getWidth(), 40);
		Graphics2D g2d = (Graphics2D)g; 
		
		final Image left = ResourcesManager.getRessourceAsImage(ImageNames.EditButton.LEFT);
		final Image right = ResourcesManager.getRessourceAsImage(ImageNames.EditButton.RIGHT);
		final Image bg = ResourcesManager.getRessourceAsImage(ImageNames.EditButton.BG);

		g2d.drawImage(left, 0, 0, null);
		g2d.drawImage(right, this.getWidth() - right.getWidth(null), 0, null);
		
		g2d.drawImage(bg, left.getWidth(null), 0, this.getWidth() - left.getWidth(null) - right.getWidth(null), this.getHeight(), null);

		super.paintComponent(g);
	}
}
