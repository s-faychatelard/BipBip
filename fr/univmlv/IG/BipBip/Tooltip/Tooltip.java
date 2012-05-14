package fr.univmlv.IG.BipBip.Tooltip;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Tooltip extends JComponent {
	private static final long serialVersionUID = 3292202175247078445L;
	
	private final Collection<TooltipListener> tooltipListeners = new ArrayList<TooltipListener>();
	private final ArrayList<JButton> buttons = new ArrayList<JButton>();
	private JLabel label = null;
	private Point.Double coords = new Point.Double(0,0);
	private int width = 82;
	
	private static final int height = 51;
	private static final Image left = new ImageIcon(Tooltip.class.getResource("tooltip-left.png")).getImage();
	private static final Image right = new ImageIcon(Tooltip.class.getResource("tooltip-right.png")).getImage();
	private static final Image arrow = new ImageIcon(Tooltip.class.getResource("tooltip-arrow.png")).getImage();
	private static final Image lblBg = new ImageIcon(Tooltip.class.getResource("tooltip-bg.png")).getImage();

	public Tooltip() {
		super();
		this.setOpaque(false);
		this.setLayout(null);
	}
	
	public void addTooltipListener(TooltipListener listener) {
		tooltipListeners.add(listener);
	}
	
	protected void fireSelectedAtIndex(int index) {
		for(TooltipListener listener : tooltipListeners)
			listener.eventSelectedAtIndex(index);
	}
	
	private void reorganizeButtons() {
		int offset=left.getWidth(null);
		for(JButton btn : buttons) {
			btn.setLocation(offset, 0);
			offset+=btn.getWidth();
		}
		width = offset + right.getWidth(null);
	}
	
	public JLabel addLabel(String text) {
		label = new JLabel("<html><font color='white'>" + text + "</font></html>");
		label.setFont(new Font("Arial", Font.BOLD, 11));
		label.setLocation(4, 0);
		this.setLabelText(text);
		this.add(label);
		return label;
	}
	
	public void setLabelText(String text) {
		if (label!=null) {
			label.setText("<html><font color='white'>" + text + "</font></html>");
			FontMetrics metrics = label.getFontMetrics(label.getFont());
			label.setSize(metrics.stringWidth(text), 40);
			width = label.getWidth() + 8;
		}
	}

	public TooltipButton addButton(Icon icon, String tooltipText) {
		return this.addButton(icon, tooltipText, buttons.size());
	}
	
	public TooltipButton addButton(Icon icon, String tooltipText, final int index) {
		TooltipButton button = new TooltipButton(icon);
        button.setToolTipText(tooltipText);
        button.setSize(32, 0);
		buttons.add(index, button);
		button.setSize(button.getWidth(), 51);
		reorganizeButtons();
		this.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireSelectedAtIndex(index);
			}
		});
		return button;
	}
	
	public void setCoords(Point.Double coords) {
		this.coords = coords;
	}
	
	public Point.Double getCoords() {
		return this.coords;
	}
	
	@Override
	public void setLocation(int x, int y) {
		this.setLocation(new Point(x, y));
	}
	
	@Override
	public void setLocation(Point p) {
		super.setLocation(p.x - this.getWidth()/2, p.y - this.getHeight());
	}
	
	@Override
	public Point getLocation() {
		return new Point(super.getLocation().x + this.getWidth()/2, super.getLocation().y + this.getHeight());
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		this.setSize(width, height);
		
		Graphics2D g2d = (Graphics2D)g;

		/* Draw tooltip */
		g2d.drawImage(left, 0, 0, null);
		g2d.drawImage(right, this.getWidth() - right.getWidth(null), 0, null);
		
		g2d.drawImage(arrow, super.getWidth()/2 - arrow.getWidth(null)/2, this.getHeight() - arrow.getHeight(null), null);
		
		/* Draw lbl background */
		if (label != null)
			g2d.drawImage(lblBg, label.getLocation().x, label.getLocation().y, label.getWidth(), left.getHeight(null), null);
	}
}
