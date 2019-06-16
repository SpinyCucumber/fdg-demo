
import static org.lwjgl.opengl.GL13.*;

public class GraphRenderer {

	private Graph<? extends Locateable> graph;

	public GraphRenderer(Graph<? extends Locateable> graph) {
		super();
		this.graph = graph;
	}
	
	public void draw() {
		glBegin(GL_POINTS);
		for (Graph.Node<? extends Locateable> node : graph.getNodes()) {
			node.getData().getPosition().glVertex();
		}
		glEnd();
		glBegin(GL_LINES);
		for (Graph.Connection<? extends Locateable> conn : graph.getConns()) {
			conn.getA().getData().getPosition().glVertex();
			conn.getB().getData().getPosition().glVertex();
		}
		glEnd();
	}
	
}
