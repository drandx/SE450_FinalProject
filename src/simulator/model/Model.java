package simulator.model;

import java.util.LinkedList;
import java.util.Observable;

import simulator.agent.Agent;
import simulator.agent.TimeServer;
import simulator.util.Animator;



public final class Model extends Observable implements TimeServer {

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
	//TODO: Before Refactoring
	@SuppressWarnings("unused")
	private boolean _disposed;
	private LightController[][] _lControllers;

	private LinkedList<Source> sources = new LinkedList<Source>();
	//TODO: Before Refactoring
	//private LinkedList<LightController> lightControllers = new LinkedList<LightController>();

	private int _horizontalRoads;
	private int _verticalRoads;



	/*
	 * Invariant: _head != null
	 * Invariant: _head.agent == null
	 * Invariant: (_size == 0) iff (_head.next == null)
	 */
	public Model() {
		_size = 0;
		_head = new Node(0, null, null);
	}

	public Model(AnimatorBuilder builder){

		int rows = MP.getRows();
		int columns = MP.getColumns();

		_size = 0;
		_head = new Node(0, null, null);
		_lControllers =  new LightController[rows][columns];


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
		this._horizontalRoads = rows;
		this._verticalRoads = columns;

		// Add Lights
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				Light nsLight = new Light();
				Light ewLight = new Light();
				_lControllers[i][j] = new LightController(nsLight,ewLight);
				builder.addLight(nsLight, i, j);//This is just for the UI
			}
		}

		WorldFactory.newInstance(MP.isAlternating, this, null, this._lControllers, builder, this);

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
			for (int i=0; i<this._horizontalRoads; i++) {
				for (int j=0; j<this._verticalRoads; j++) {
					_lControllers[i][j].run(_currentTime);
				}
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
