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
package fr.univmlv.IG.BipBip.GlobalComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class SplitPane extends JSplitPane {
	private static final long serialVersionUID = -3741186309641104813L;
	
	private static final Image bg = ResourcesManager.getRessourceAsImage(ImageNames.General.SPLIT_DIVIDER);

	/**
	 * Create a JSplitPane with a cusomize divider
	 */
	public SplitPane() {
		this.setContinuousLayout(true);
		this.setBorder(null);
        this.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 1L;
 
					@Override
					public void paint(Graphics g) {
						Graphics2D g2d = (Graphics2D)g;

						/* Draw background */
						g2d.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);
					}
				};
			}
		});
	}
}
