package fr.univmlv.IG.BipBip;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class SplitPane extends JSplitPane {
	private static final long serialVersionUID = -3741186309641104813L;
	
	private static final Image bg = new ImageIcon(SplitPane.class.getResource("splitdivider-bg.png")).getImage();

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
