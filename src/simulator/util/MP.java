package simulator.util;

/**
 * Static class for model parameters.
 */
public class MP {
  private MP() {}
  /** Length of cars, in meters */
  public static double carLength = 10;
  /** Length of roads, in meters */
  public static double roadLength = 300;
  /** Maximum car velocity, in meters/second */
  public static double maxVelocity = 10;
  
  /**Macimum numbers of car per road*/
  public static double maxRoadCars = 1;
  
  public static double sleepSeconds = 5;
  
  public static double greenDurationNS = 55;
  public static double yellowDurationNS = 5;
  public static double greenDurationEW = 25;
  public static double yellowDurationEW = 5;
}  

