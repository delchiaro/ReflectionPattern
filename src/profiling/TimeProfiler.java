package reflectionPattern.profiling;

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
        return _startTime - System.nanoTime();
    }
}
