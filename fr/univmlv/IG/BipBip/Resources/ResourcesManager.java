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
package fr.univmlv.IG.BipBip.Resources;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * This class manages all resources of each classes of the application
 */
public class ResourcesManager {
	
	private static ResourcesManager instance;
	private final HashMap<String, ImageIcon> resources = new HashMap<String, ImageIcon>();
	
	/**
	 * Get the instance of the ResourcesManager
	 * 
	 * @return an instance of the ResourcesManager
	 */
	private static ResourcesManager getInstance() {
		if(instance == null)
			instance = new ResourcesManager();
		
		return instance;
	}
	
	/**
	 * Get a resource URL
	 * 
	 * @param name of the resource
	 * @return the URL resource or null
	 */
	public URL getRessource(String name) {
		return ResourcesManager.class.getResource(name);
	}
	
	/**
	 * Get a resource has Image
	 * 
	 * @param name of the resource
	 * @return the Image of the resource name
	 */
	public static Image getRessourceAsImage(String name) {
		return getRessourceAsImageIcon(name).getImage();
	}
	
	/**
	 * Get a resource has ImageIcon
	 * 
	 * @param name of the resource
	 * @return the ImageIcone of the resource name
	 */
	public static ImageIcon getRessourceAsImageIcon(String name) {
		ImageIcon image = getInstance().resources.get(name);
		if(image == null) {
			image = new ImageIcon(getInstance().getRessource(name));
			getInstance().resources.put(name, image);
		}
		return image;
		
	}
}
