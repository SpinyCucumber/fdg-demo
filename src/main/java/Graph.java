import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Graph<T> {
	
	public static class Node<T> {
		
		private Set<Node<T>> neighbors;
		private T data;

		public Node() {
			super();
			neighbors = new HashSet<>();
		}

		public Node(T data) {
			this();
			this.data = data;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public Set<Node<T>> getNeighbors() {
			return neighbors;
		}
		
	}
	
	public static class Connection<T> {
		
		private Node<T> a, b;

		public Connection(Node<T> a, Node<T> b) {
			super();
			this.a = a;
			this.b = b;
		}

		public Node<T> getA() {
			return a;
		}

		public Node<T> getB() {
			return b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((a == null) ? 0 : a.hashCode());
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Connection<?> other = (Connection<?>) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Connection [a=" + a + ", b=" + b + "]";
		}
		
	}
	
	private Set<Node<T>> nodes;
	private Set<Connection<T>> conns;
	private Class<T> clazz;
	
	public Set<Node<T>> getNodes() {
		return nodes;
	}

	public Set<Connection<T>> getConns() {
		return conns;
	}
	
	public void addConnection(Connection<T> conn) {
		conns.add(conn);
		conn.a.neighbors.add(conn.b);
		conn.b.neighbors.add(conn.a);
	}
	
	public void addAll(Graph<T> other) {
		nodes.addAll(other.nodes);
		conns.addAll(other.conns);
	}
	
	public void addNode(Node<T> node) {
		nodes.add(node);
	}
	
	public Node<T> createNewNode() {
		Node<T> result = new Node<>();
		try {
			result.setData(clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			FDGTest.log.error("Error initializing node data", e);
		}
		return result;
	}

	public Graph(Class<T> clazz) {
		super();
		nodes = new HashSet<>();
		conns = new HashSet<>();
		this.clazz = clazz;
	}
	
	public static class TreeResult<T> {
		
		public Node<T> root;
		public Graph<T> graph;
		
		public TreeResult(Node<T> root, Graph<T> graph) {
			super();
			this.root = root;
			this.graph = graph;
		}
		
	}
	
	public void clear() {
		conns.clear();
		nodes.clear();
	}
	
	public Node<T> createTree(int nLevels, int nChildren) {
		clear();
		Node<T> root = createNewNode();
		addNode(root);
		if (nLevels > 1) {
			FDGTest.log.info("Creating children");
			for (int iChild = 0; iChild < nChildren; iChild++) {
				Graph<T> subGraph = new Graph<>(clazz);
				Node<T> subRoot = subGraph.createTree(nLevels - 1, nChildren);
				addAll(subGraph);
				addConnection(new Connection<>(root, subRoot));
			}
		}
		return root;
	}
	
	public void createRandomGraph(int nodes, int edges) {
		clear();
		int nNodes = nodes, nEdges = edges;
		// Create initial node
		Node<T> initial = createNewNode();
		addNode(initial);
		nNodes--;
		// Create nodes and connect them randomly
		for (; nNodes > 0; nNodes--) {
			Node<T> newNode = createNewNode();
			Connection<T> newConn = new Connection<>(newNode, choice(getNodes(), FDGTest.rand));
			FDGTest.log.info("Creating node and connecting " + newConn);
			addNode(newNode);
			addConnection(newConn);
			nEdges--;
		}
		// Create random edges for remaining edges
		while (nEdges > 0) {
			Connection<T> newConn = new Connection<>(choice(getNodes(), FDGTest.rand), choice(getNodes(), FDGTest.rand));
			if (getConns().contains(newConn)) continue; // Avoid duplicates. Probably a better way to do this
			addConnection(newConn);
			nEdges--;
		}
	}
	
	public static void randomizePositions(Graph<? extends Locateable> graph, float minX, float maxX, float minY, float maxY) {
		for(Node<? extends Locateable> node : graph.getNodes()) {
			node.getData().setPosition(Vec2f.randomVector(minX, maxX, minY, maxY));
		}
	}
	
	// Thanks Stack Overflow
	public static <E> E choice(Collection<? extends E> coll, Random rand) {
	    if (coll.size() == 0) {
	        return null; // or throw IAE, if you prefer
	    }

	    int index = rand.nextInt(coll.size());
	    if (coll instanceof List) { // optimization
	        return ((List<? extends E>) coll).get(index);
	    } else {
	        Iterator<? extends E> iter = coll.iterator();
	        for (int i = 0; i < index; i++) {
	            iter.next();
	        }
	        return iter.next();
	    }
	}
	
}
