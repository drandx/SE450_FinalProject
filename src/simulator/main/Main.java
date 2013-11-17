package simulator.main;

import simulator.GUI.swing.elements.PopupUI;
import simulator.GUI.swing.elements.TextUI;
import simulator.GUI.swing.elements.UI;

/**
 * A static class to demonstrate the visualization aspect of
 * simulation.
 */
public class Main {
	private Main() {}
	public static void main(String[] args) {
		{
			//UI ui = new PopupUI();
			UI ui =  new TextUI();
			Control control = new Control(ui);
			control.run();
		}
	}
}

