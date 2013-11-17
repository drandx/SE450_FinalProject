package simulator.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public class CarTEST extends TestCase
{
  public CarTEST(String name)
  {
    super(name);
  }

  public void testCarCreation()
  {
    Car c = new Car();
    Assert.assertEquals(Double.valueOf(0.0D), Double.valueOf(c.getPosition()));
    Assert.assertEquals(Double.valueOf(c.getVelocity()), Double.valueOf(c.getPosition() + c.getVelocity()));
  }
}