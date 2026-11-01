package server;

/**
 * Interface for servers that support telemetry.
 * Classes implementing this interface must define their own telemetric function.
 */
public interface Monitorable {

    /**
     * Returns telemetric data specific to the implementing server type.
     */
    String monitor();
}
