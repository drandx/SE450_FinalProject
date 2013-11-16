package simulator.util;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UtilTEST extends TestCase {
  public UtilTEST(String name) {
    super(name);
  }
  public void testequals() {
    Assert.assertTrue (Util.isEquals(0.3, 0.3+0.5*Util.EPSILON));
    Assert.assertFalse(Util.isEquals(0.3, 0.3+2.0*Util.EPSILON));
    Assert.assertFalse(Util.isLess(0.3, 0.3+0.5*Util.EPSILON));
    Assert.assertTrue (Util.isLess(0.3, 0.3+2.0*Util.EPSILON));
    Assert.assertTrue (Util.isLessOrEquals(0.3, 0.3+0.5*Util.EPSILON));
    Assert.assertTrue (Util.isLessOrEquals(0.3, 0.3+2.0*Util.EPSILON));

    Assert.assertTrue (Util.isEquals(0.3+0.5*Util.EPSILON, 0.3));
    Assert.assertFalse(Util.isEquals(0.3+2.0*Util.EPSILON, 0.3));
    Assert.assertFalse(Util.isLess(0.3+0.5*Util.EPSILON, 0.3));
    Assert.assertFalse(Util.isLess(0.3+2.0*Util.EPSILON, 0.3));
    Assert.assertTrue (Util.isLessOrEquals(0.3+0.5*Util.EPSILON, 0.3));
    Assert.assertFalse(Util.isLessOrEquals(0.3+2.0*Util.EPSILON, 0.3));
  }
  public void testrandom() {
    for (int i=0; i<100; i++) {
      double x=Util.nextRandom(i,i*i);
      Assert.assertTrue (Util.isLessOrEquals(i,x));
      Assert.assertTrue (Util.isLessOrEquals(x,i*i));
    }
  }
}
