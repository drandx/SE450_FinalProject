package simulator.model;
import simulator.agent.Agent;
import simulator.agent.TimeServer;

public class Car implements Agent{

	private double _length = (int)(MP.maxCarLength * Math.random()) + MP.minCarLength;
	private double _maxVelocity = Math.random() * (MP.maxVelocity - MP.minVelocity) + MP.minVelocity;
	private double _velocity = this._maxVelocity;
	private double _breakDistance = Math.random() * (MP.maxBreakDistance - MP.minBreakDistance) + MP.minBreakDistance;
	private double _stopDistance = Math.random() * (MP.maxStopDistance - MP.minStopDistance) + MP.minStopDistance;

	private java.awt.Color _color = new java.awt.Color((int)Math.ceil(Math.random()*255),(int)Math.ceil(Math.random()*255),(int)Math.ceil(Math.random()*255));

	private CarAcceptor _observer; //It's a rreference to the current road
	private TimeServer _timeServer; //It's going to be a refference to the timeserver. !!! 

	private double _frontPositon;

	public Car() {
		this._maxVelocity = (Math.random() * (MP.maxVelocity - MP.minVelocity) + MP.minVelocity);
	}

	public java.awt.Color getColor() {
		return _color;
	}

	public void run(double time) {
		double distanceToObs = this._observer.distanceToObstacle(this._frontPositon);

		if(distanceToObs==0)distanceToObs = Double.POSITIVE_INFINITY; //Allows parallel objects

		_velocity = (_maxVelocity / (_breakDistance - _stopDistance)) * (distanceToObs - _stopDistance);
		_velocity = Math.max(0.0, _velocity);
		_velocity = Math.min(_maxVelocity, _velocity);
		_frontPositon = _frontPositon + _velocity * time;

		if (((Road) _observer).acceptObstacle(this))
			_timeServer.enqueue(_timeServer.currentTime()+MP.getTimeStep(),this);

	}

	public double maxVelocity(){
		return this._maxVelocity;
	}

	public double stopDistance(){

		return this._stopDistance;
	}

	public void setTimeServer(TimeServer ts){
		this._timeServer = ts;
	}

	double backPosition() {
		return _frontPositon-_length;
	}

	public void setFrontPosition(double position){
		this._frontPositon = position;
	}

	public double getPosition() {
		return _frontPositon;
	}

	public double getVelocity() {
		return this._velocity;
	}

	public double get_length() {
		return _length;
	}

	public void set_length(double _length) {
		this._length = _length;
	}

	public CarAcceptor getObserver() {
		return _observer;
	}
	
	public void setObserver(CarAcceptor oberver){
		this._observer = oberver;
	}
	
	

}
