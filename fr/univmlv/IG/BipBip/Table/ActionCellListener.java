package fr.univmlv.IG.BipBip.Table;

/**
 * Listener for the ActionCell component
 */
public interface ActionCellListener {
	public void eventLocate(int index);
	public void eventEdit(int index);
	public void eventDelete(int index);
}
