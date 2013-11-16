package simulator.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import simulator.agent.Agent;
import simulator.agent.TimeServer;
import simulator.model.Light.lightState;
import simulator.util.Animator;
import simulator.util.MP;
import simulator.util.Util;



public final class TimeServerLinked extends Observable implements TimeServer {
	
	private static final class Node {
		final double waketime;
		final Agent agent;
		Node next;

		public Node(double waketime, Agent agent, Node next) {
			this.waketime = waketime;
			this.agent = agent;
			this.next = next;
		}
	}
	
	private double _currentTime;
	private int _size;
	private Node _head;
	
	//Traffic Simulator stuff
	private Animator _animator;
	private boolean _disposed;
	
	private LinkedList<Source> sources = new LinkedList<Source>();
	private LinkedList<LightController> lightControllers = new LinkedList<LightController>();
	
	
	
	/*
	 * Invariant: _head != null
	 * Invariant: _head.agent == null
	 * Invariant: (_size == 0) iff (_head.next == null)
	 */
	public TimeServerLinked() {
		_size = 0;
		_head = new Node(0, null, null);
	}

	public TimeServerLinked(AnimatorBuilder builder, int rows, int columns){
		_size = 0;
		_head = new Node(0, null, null);

		if (rows < 0 || columns < 0 || (rows == 0 && columns == 0)) {
			throw new IllegalArgumentException();
		}
		if (builder == null) {
			builder = new NullAnimatorBuilder();
		}
		setup(builder, rows, columns);
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
		
		// Add Lights
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				Light nsLight = new Light();
				Light ewLight = new Light();
				this.lightControllers.add(new LightController(nsLight,ewLight));
				builder.addLight(nsLight, i, j);
			}
		}

		// Add Horizontal Roads
		for (int i=0; i<rows; i++) {

			boolean eastToWest = false;
			Road firstRoad = null;
			Road lastRoad = null;

			for (int j=0; j<=columns; j++) {
				Road oldLast = lastRoad;
				lastRoad = new Road();
				lastRoad._nextAcceptor = null;

				if(firstRoad == null){
					firstRoad = lastRoad;
					Source source = new Source(firstRoad);
					this.sources.add(source);
				}
				else oldLast._nextAcceptor = lastRoad;

				builder.addHorizontalRoad(lastRoad, i, j, eastToWest);
				roads.add(lastRoad);
			}
			eastToWest = !eastToWest;
			Sink sink = new Sink();
			lastRoad._nextAcceptor = sink;
		}

		// Add Vertical Roads
		for (int j=0; j<columns; j++) {
			boolean southToNorth = false;
			Road firstNSRoad = null;
			Road lastNSRoad = null;
			for (int i=0; i<=rows; i++) {
				Road oldNSRoad = lastNSRoad;
				lastNSRoad = new Road();
				lastNSRoad._nextAcceptor = null;
				if(firstNSRoad == null){
					firstNSRoad = lastNSRoad;
					Source source = new Source(firstNSRoad);
					this.sources.add(source);
				}
				else oldNSRoad._nextAcceptor = lastNSRoad;
				builder.addVerticalRoad(lastNSRoad, i, j, southToNorth);
				roads.add(lastNSRoad);
			}
			Sink sink = new Sink();
			lastNSRoad._nextAcceptor = sink;
			southToNorth = !southToNorth;
		}
		for(Source s : sources){
			this.enqueue(1, s.generateCar(this));
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node node = _head.next;
		String sep = "";
		while (node != null) {
			sb.append(sep).append("(").append(node.waketime).append(",")
			.append(node.agent).append(")");
			node = node.next;
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
		if (waketime <= _currentTime)
			throw new IllegalArgumentException();
		Node prevElement = _head;
		while ((prevElement.next != null) &&
				(prevElement.next.waketime <= waketime)) {
			prevElement = prevElement.next;
		}
		Node newElement = new Node(waketime, agent, prevElement.next);
		prevElement.next = newElement;
		_size++;
			}

	Agent dequeue()
			throws IndexOutOfBoundsException
			{
		if (_size < 1)
			throw new IndexOutOfBoundsException();
		Agent rval = _head.next.agent;
		_head.next = _head.next.next;
		_size--;
		return rval;
			}

	int size() {
		return _size;
	}

	boolean empty() {
		return size() == 0;
	}

	public void run(double duration) {
		double endtime = _currentTime + duration;
		
		while ((!empty()) && (_head.next.waketime <= endtime)) {
			for (LightController lc :  lightControllers) {
				lc.run(_currentTime);
			}
			_currentTime = _head.next.waketime;
			while ((!empty()) && (_head.next.waketime == _currentTime)) {
				dequeue().run(_currentTime);
				super.setChanged();
				super.notifyObservers();
			}

		}
		_currentTime = endtime;
	}


}
