package firescape;

import org.osbot.rs07.script.MethodProvider;

public class definitions extends MethodProvider {
    static Timer runTime;
    public static class Timer {
        private long period;
        private long start;
        public Timer(long period) {
            this.period = period;
            start = System.currentTimeMillis();
        }
        public long getElapsed() {
            return System.currentTimeMillis() - start;
        }
        public long getRemaining() {
            return period - getElapsed();
        }
        public boolean isRunning() {
            return getElapsed() <= period;
        }
        public void reset() {
            start = System.currentTimeMillis();
        }
        public void stop() {
            period = 0;
        }
    }
    public static final String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60;
        m %= 60;
        h %= 24;
        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }
    public static int getPerHour(int value) {
        if (runTime != null && runTime.getElapsed() > 0) {
            return (int) (value * 3600000d / runTime.getElapsed());
        } else return 0;
    }
}