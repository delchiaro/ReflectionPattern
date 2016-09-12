package profiling;

import java.util.Timer;

/**
 * Created by nagash on 09/09/16.
 */
public class TimeProfiler {
    private long _startTime = 0;
    public TimeProfiler() {}

    public final void resetStart() {
        this._startTime = System.nanoTime();
    }

    public final long elapsed() {
        return  System.nanoTime() - _startTime;
    }

    public final double elapsedMs() {
        return  (((double)System.nanoTime()) - _startTime) / (1000000);
    }
}
