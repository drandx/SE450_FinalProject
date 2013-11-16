package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.PriorityQueue;

import simulator.agent.Agent;
import simulator.agent.TimeServer;
import simulator.util.Animator;
import simulator.util.MP;
import simulator.util.Util;


public final class TimeServerQueue extends Observable implements TimeServer {

	private static final class Node implements Comparable<Node> {
		final double waketime;
		final Agent agent;

		public Node(double waketime, Agent agent) {
			this.waketime = waketime;
			this.agent = agent;
		}

		public int compareTo(Node that) {
			return (int) (Math.signum(this.waketime - that.waketime));
		}
	}

	private double _currentTime;
	private PriorityQueue<Node> _queue;


	//Traffic Simulator stuff
	private Animator _animator;
	private boolean _disposed;
	//private double _time; //replaced by _currentTime

	public TimeServerQueue() {
		_queue = new PriorityQueue<Node>();
	}


	public TimeServerQueue(AnimatorBuilder builder, int rows, int columns){
		_queue = new PriorityQueue<Node>();

		if (rows < 0 || columns < 0 || (rows == 0 && columns == 0)) {
			throw new IllegalArgumentException();
		}
		if (builder == null) {
			builder = new NullAnimatorBuilder();
		}
		//_agents = new ArrayList<Agent>(); Replaced for a Priority Queue
		setup(builder, rows, columns); //Adds lights, roads, cars to the World
		_animator = builder.getAnimator();
		super.addObserver(_animator);

	}

	/**
	 * Throw away this model.
	 */
	public void dispose() {
		_animator.dispose();
		_disposed = true;
	}

	/**
	 * Construct the model, establishing correspondences with the visualizer.
	 */
	private void setup(AnimatorBuilder builder, int rows, int columns) {
		List<Road> roads = new ArrayList<Road>();
		Light[][] intersections = new Light[rows][columns];
		Boolean reverse;

		/*
    // Add Lights
    for (int i=0; i<rows; i++) {
      for (int j=0; j<columns; j++) {
        intersections[i][j] = new Light();
        builder.addLight(intersections[i][j], i, j);
        _agents.add(intersections[i][j]); //Creates Lights
      }
    }*/

		// Add Horizontal Roads
		boolean eastToWest = false;
		for (int i=0; i<rows; i++) {
			for (int j=0; j<=columns; j++) {
				Road l = new Road();
				builder.addHorizontalRoad(l, i, j, eastToWest);
				roads.add(l);
			}
			eastToWest = !eastToWest;
		}

		// Add Vertical Roads
		boolean southToNorth = false;
		for (int j=0; j<columns; j++) {
			for (int i=0; i<=rows; i++) {
				Road l = new Road();
				builder.addVerticalRoad(l, i, j, southToNorth);
				roads.add(l);
			}
			southToNorth = !southToNorth;
		}


		// Add Cars
		for (Road l : roads) {
			Util.setRandomSeed(10);
			int randomCars = (int)Math.round(Util.nextRandom(1, MP.maxRoadCars));
			for(int i=0; i<=randomCars;i++){ //Loop to create random number of cars per road
				Car car = new Car(0,0,0,0);
				Util.setRandomSeed(i*10);
				_queue.add(new Node(0, car)); //TODO: Figureout the max wakeuptime.
				l.accept(car);
			}
			System.out.println("");//TODO: Remove this flag
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		String sep = "";
		Node[] nodes = _queue.toArray(new Node[0]);
		java.util.Arrays.sort(nodes);
		for (Node node : nodes) {
			sb.append(sep).append("(").append(node.waketime).append(",")
			.append(node.agent).append(")");
			sep = ";";
		}
		sb.append("]");
		return (sb.toString());
	}

	public double currentTime() {
		return _currentTime;
	}

	public void enqueue(double waketime, Agent agent)
			throws IllegalArgumentException
			{
		if (waketime < _currentTime)
			throw new IllegalArgumentException();
		_queue.add(new Node(waketime, agent));
			}

	Agent dequeue()
	{
		return _queue.remove().agent;
	}

	int size() {
		return _queue.size();
	}

	boolean empty() {
		return _queue.isEmpty();
	}

	public void run(double duration) {
	    double endtime = _currentTime + duration;
	    while ((!empty()) && (_queue.peek().waketime <= endtime)) {
	      if ((_currentTime - _queue.peek().waketime) > 1e-09) {
	        super.setChanged();
	        super.notifyObservers();
	      }
	      _currentTime = _queue.peek().waketime;
	      dequeue().run(0);
	    }
	    _currentTime = endtime;
	  }


}



