package simulator.GUI.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import simulator.model.AnimatorBuilder;
import simulator.model.Car;
import simulator.model.Light;
import simulator.model.MP;
import simulator.model.Road;
import simulator.model.Light.lightState;
import simulator.util.Animator;
import simulator.util.SwingAnimator;
import simulator.util.SwingAnimatorPainter;

/** 
 * A class for building Animators.
 */
public class SwingAnimatorBuilder implements AnimatorBuilder 	{
	MyPainter _painter;
	public SwingAnimatorBuilder() {
		_painter = new MyPainter();
	}
	public Animator getAnimator() {
		if (_painter == null) { throw new IllegalStateException(); }
		Animator returnValue = new SwingAnimator(_painter, "Traffic Simulator",
				VP.displayWidth, VP.displayHeight, VP.displayDelay);
		_painter = null;
		return returnValue;
	}
	private static double skipInit = VP.gap;
	private static double skipRoad = VP.gap + MP.roadDrawLength;
	private static double skipCar = VP.gap + VP.elementWidth;
	private static double skipRoadCar = skipRoad + skipCar;

	public void addLight(Light d, int i, int j) {
		double x = skipInit + skipRoad + j*skipRoadCar;
		double y = skipInit + skipRoad + i*skipRoadCar;
		Translator t = new TranslatorWE(x, y, MP.carLength, VP.elementWidth, VP.scaleFactor);
		_painter.addLight(d,t);
	}
	public void addHorizontalRoad(Road l, int i, int j, boolean eastToWest) {
		double x = skipInit + j*skipRoadCar;
		double y = skipInit + skipRoad + i*skipRoadCar;
		Translator t = eastToWest ? new TranslatorEW(x, y, MP.roadDrawLength, VP.elementWidth, VP.scaleFactor)
		: new TranslatorWE(x, y, MP.roadDrawLength, VP.elementWidth, VP.scaleFactor);
		_painter.addRoad(l,t);
	}
	public void addVerticalRoad(Road l, int i, int j, boolean southToNorth) {
		double x = skipInit + skipRoad + j*skipRoadCar;
		double y = skipInit + i*skipRoadCar;
		Translator t = southToNorth ? new TranslatorSN(x, y, MP.roadDrawLength, VP.elementWidth, VP.scaleFactor)
		: new TranslatorNS(x, y, MP.roadDrawLength, VP.elementWidth, VP.scaleFactor);
		_painter.addRoad(l,t);
	}


	/** Class for drawing the Model. */
	private static class MyPainter implements SwingAnimatorPainter {

		/** Pair of a model element <code>x</code> and a translator <code>t</code>. */
		private static class Element<T> {
			T x;
			Translator t;
			Element(T x, Translator t) {
				this.x = x;
				this.t = t;
			}
		}

		private List<Element<Road>> _roadElements;
		private List<Element<Light>> _lightElements;
		
		MyPainter() {
			_roadElements = new ArrayList<Element<Road>>();
			_lightElements = new ArrayList<Element<Light>>();
		}    
		void addLight(Light x, Translator t) {
			_lightElements.add(new Element<Light>(x,t));
		}
		void addRoad(Road x, Translator t) {
			_roadElements.add(new Element<Road>(x,t));
		}

		public void paint(Graphics g) {
			// This method is called by the swing thread, so may be called
			// at any time during execution...

			// First draw the background elements
			for (Element<Light> e : _lightElements) {
				lightState tmpState = e.x.getState();
				
				if ( tmpState.equals(lightState.GREEN)) {
					g.setColor(Color.GREEN);
				} 
				else if(tmpState.equals(lightState.RED)) {
					g.setColor(Color.RED);
				} 
				else if(tmpState.equals(lightState.YELLOW)) {
					g.setColor(Color.YELLOW);
				}
				XGraphics.fillOval(g, e.t, 0, 0, MP.carLength, VP.elementWidth);
			}
			g.setColor(Color.BLACK);
			for (Element<Road> e : _roadElements) {
				XGraphics.fillRect(g, e.t, 0, 0, MP.roadDrawLength, VP.elementWidth);
			}

			// Then draw the foreground elements
			for (Element<Road> e : _roadElements) {
				// iterate through a copy because e.x.getCars() may change during iteration...
				for (Car d : (Car[])((Road)e.x).getCars().toArray(new Car[0])) {
					g.setColor(d.getColor());
					//XGraphics.fillOval(g, e.t, d.getPosition(), 0, MP.carLength, VP.elementWidth);
					//Paints the size of the car
					XGraphics.fillOval(g, e.t, d.getPosition() / (((Road) d.getObserver()).getEndPosition() / MP.roadDrawLength), 0.0D, d.get_length() / (((Road) d.getObserver()).getEndPosition() / MP.roadDrawLength), VP.elementWidth);
				}
			}
			
			
		}
	}
}

