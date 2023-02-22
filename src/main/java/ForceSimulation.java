public class ForceSimulation {

	public static class LinearFunc implements FloatFunction {
		
		private float k;

		public LinearFunc(float k) {
			super();
			this.k = k;
		}

		@Override
		public float apply(float x) {
			return k * x;
		}
		
	}
	
	public static class InverseSquare implements FloatFunction {
		
		private float k;

		public InverseSquare(float k) {
			super();
			this.k = k;
		}

		@Override
		public float apply(float x) {
			return k * (1.0f / (x * x));
		}
		
	}
	
	public static class Body implements Locateable {
		
		private Vec2f position, velocity;
		private float invMass = 1.0f;

		public Body() {
			super();
			this.position = new Vec2f();
			this.velocity = new Vec2f();
		}

		public Vec2f getPosition() {
			return position;
		}

		public void setPosition(Vec2f position) {
			this.position = position;
		}

		public Vec2f getVelocity() {
			return velocity;
		}
		
	}
	
	public static interface FloatFunction {
		
		public float apply(float x);
		
	}
	
	private Graph<Body> graph;
	private FloatFunction attractFunc, repulseFunc, dampFunc;
	private Vec2f gravity;
	
	public ForceSimulation(Graph<Body> graph, FloatFunction attractFunc, FloatFunction repulseFunc, FloatFunction dampFunc, Vec2f gravity) {
		super();
		this.graph = graph;
		this.attractFunc = attractFunc;
		this.repulseFunc = repulseFunc;
		this.dampFunc = dampFunc;
		this.gravity = gravity;
	}

	public void update(float dt) {
		for (Graph.Node<Body> node : graph.getNodes()) {
			
			// Calculate force
			Vec2f totalForce = new Vec2f();
			for (Graph.Node<Body> other : graph.getNodes()) {
				if (other.equals(node)) continue; // Ignore ourselves
				Vec2f diff = other.getData().getPosition().minus(node.getData().getPosition());
				float distance = diff.length();
				// If the other node is a neighor, apply an attractive force. If not, repulse.
				Vec2f force = diff.scaledBy(node.getNeighbors().contains(other) ? repulseFunc.apply(distance) : -attractFunc.apply(distance));
				totalForce.add(force);
			}
			// Add gravity
			totalForce.add(gravity);
			// Add damping
			totalForce.add(node.getData().getVelocity().scaledBy(-dampFunc.apply(node.getData().getVelocity().length())));
			
			// Integrate out junk
			node.getData().getVelocity().add(totalForce.scaledBy(node.getData().invMass * dt));
			node.getData().getPosition().add(node.getData().getVelocity().scaledBy(dt));
			
		}
	}
	
}
