package gr.ntua.ivml.mint.util;

public interface Lockable {
	// an id String that is usefull for Lock management
	public String lockId();
	// a String that can be displayed in a UI
	public String lockName();
}
