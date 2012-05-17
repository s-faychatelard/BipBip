package fr.univmlv.IG.BipBip.EditDialog;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class EditButton extends JButton {
	private static final long serialVersionUID = -8973864888152429363L;
	
	private static final Image left = new ImageIcon(EditButton.class.getResource("editButton-left.png")).getImage();
	private static final Image right = new ImageIcon(EditButton.class.getResource("editButton-right.png")).getImage();
	private static final Image bg = new ImageIcon(EditButton.class.getResource("editButton-bg.png")).getImage();
	
	/**
	 * Paint component
	 */
	@Override
	public void paintComponent(Graphics g) {		
		Graphics2D g2d = (Graphics2D)g; 

		g2d.drawImage(left, 0, 0, null);
		g2d.drawImage(right, this.getWidth() - right.getWidth(null), 0, null);
		g2d.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);

		super.paintComponent(g);
	}
}
