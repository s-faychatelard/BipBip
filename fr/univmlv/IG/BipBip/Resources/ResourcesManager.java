package fr.univmlv.IG.BipBip.Resources;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class ResourcesManager {
	
	private static ResourcesManager instance;
	private final HashMap<String, ImageIcon> resources = new HashMap<String, ImageIcon>();
	
	private static ResourcesManager getInstance() {
		if(instance == null)
			instance = new ResourcesManager();
		
		return instance;
	}
	
	public URL getRessource(String name) {
		return ResourcesManager.class.getResource(name);
	}
	
	public static Image getRessourceAsImage(String name) {
		return getRessourceAsImageIcon(name).getImage();
	}
	
	public static ImageIcon getRessourceAsImageIcon(String name) {
		ImageIcon image = getInstance().resources.get(name);
		if(image == null) {
			image = new ImageIcon(getInstance().getRessource(name));
			getInstance().resources.put(name, image);
		}
		return image;
		
	}
}
