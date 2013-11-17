package simulator.main;

import simulator.GUI.swing.SwingAnimatorBuilder;
import simulator.model.MP;
import simulator.model.Model;

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
      Model m = new Model(new SwingAnimatorBuilder());	
      m.run(MP.simulationRuntime); //Runs every agent added to the data structure.
      m.dispose();
    }
    System.exit(0);
  }
}

