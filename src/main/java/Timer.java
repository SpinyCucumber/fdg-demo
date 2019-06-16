import org.lwjgl.glfw.GLFW;

public class Timer {

	private double time;
	
	public Timer() {
		time = GLFW.glfwGetTime();
	}
	
	public double getDeltaTime() {
		double newTime = GLFW.glfwGetTime();
		double dt = newTime - time;
		time = newTime;
		return dt;
	}
	
}
