package simulator.main;

import simulator.GUI.swing.SwingAnimatorBuilder;
import simulator.model.Simulator;
import simulator.model.TimeServerLinked;
import simulator.model.TimeServerQueue;

/**
 * A static class to demonstrate the visualization aspect of
 * simulation.
 */
public class Main {
  private Main() {}
  public static void main(String[] args) {
    {
      //Simulator m = new Simulator(new SwingAnimatorBuilder(), 2, 2);
      //TimeServerQueue m = new TimeServerQueue(new SwingAnimatorBuilder(), 2, 2);
      TimeServerLinked m = new TimeServerLinked(new SwingAnimatorBuilder(), 1, 1);	
      m.run(2000); //Runs every agent added to the data structure.
      m.dispose();
    }
    System.exit(0);
  }
}

