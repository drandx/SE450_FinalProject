package simulator.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public class CarTEST extends TestCase{
	
	public void testConstructor(){
		Car myCar = new Car(12, 23, 78, 90);
		Assert.assertTrue(myCar.maxVelocity() == 12);
		Assert.assertTrue(myCar.brakeDistance() == 23);
		Assert.assertTrue(myCar.stopDistance() == 78);
		Assert.assertTrue(myCar.getPosition() == 90);
	}
	
	public void testRun(){
		Car myCar = new Car(12, 23, 3, 1);
		myCar.run(0);
		Assert.assertTrue(myCar.getPosition() >= 0);
	}

}
