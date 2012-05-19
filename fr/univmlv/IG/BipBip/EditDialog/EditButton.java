package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class EditButton extends JButton {
	private static final long serialVersionUID = -8973864888152429363L;
	

	
	public EditButton(String title) {
		super("<html><font color='white'>" + title + "</font></html>");
		this.setFont(new Font("Arial", Font.BOLD, 13));
		this.setSize(this.getWidth(), 40);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
		this.setRolloverEnabled(false);
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
