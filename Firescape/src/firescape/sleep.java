package firescape;

import org.osbot.rs07.utility.ConditionalSleep;

import java.util.function.BooleanSupplier;

public final class sleep extends ConditionalSleep {

    private final BooleanSupplier condition;

    public sleep(final BooleanSupplier condition, final int timeout) {
        super(timeout);
        this.condition = condition;
    }

    public sleep(final BooleanSupplier condition, final int timeout, final int interval) {
        super(timeout, interval);
        this.condition = condition;
    }

    public static boolean sleepUntil(final BooleanSupplier condition, final int timeout) {
        return new sleep(condition, timeout).sleep();
    }

    public static boolean sleepUntil(final BooleanSupplier condition, final int timeout, final int interval) {
        return new sleep(condition, timeout, interval).sleep();
    }

    @Override
    public boolean condition() throws InterruptedException {
        return condition.getAsBoolean();
    }
}