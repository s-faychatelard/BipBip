package fr.univmlv.IG.BipBip;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomBar extends JPanel {
	private static final long serialVersionUID = -2860351564159893223L;
	
	private static final Image bg = new ImageIcon(BottomBar.class.getResource("bottombar-bg.png")).getImage();
	
	public BottomBar() {
		this.setLayout(new FlowLayout());
		
		JLabel lbl = new JLabel("Pour ajouter une nouvelle alerte, faites un clic prolongé sur le lieu de l'alerte, puis choisissez son type.");
		this.add(lbl);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		/* Draw bg */
		g2d.drawImage(bg, 0, 0, this.getHeight(), this.getWidth(), null);
	}
}
