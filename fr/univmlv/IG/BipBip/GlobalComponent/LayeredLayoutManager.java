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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * New LayeredLayoutManager to create be more pratical with LayeredPane
 */
public class LayeredLayoutManager implements LayoutManager {

	 @Override
    public Dimension preferredLayoutSize(Container parent) {
        Component[] components = parent.getComponents();
        if (components.length == 0) {
            return new Dimension(0, 0);
        }
        Dimension d = components[0].getPreferredSize();
        for (int i=1; i<components.length; i++) {
            Dimension tmp = components[i].getPreferredSize();
            if (d.width < tmp.width)
                d.width = tmp.width;
            if (d.height < tmp.height)
                d.height = tmp.height;
        }
        return d;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Component[] components = parent.getComponents();
        if (components.length == 0)
            return new Dimension(0, 0);
        Dimension d = components[0].getMinimumSize();
        for (int i=1; i<components.length; i++) {
            Dimension tmp = components[i].getMinimumSize();
            if (d.width < tmp.width)
                d.width = tmp.width;
            if (d.height < tmp.height)
                d.height = tmp.height;
        }
        return d;
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension d = parent.getSize();
        for (Component c : parent.getComponents()) {
            c.setSize(d);
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    	throw new UnsupportedOperationException("Method addLayoutComponent not implemented");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    	throw new UnsupportedOperationException("Method removeLayoutComponent not implemented");
    }
}
