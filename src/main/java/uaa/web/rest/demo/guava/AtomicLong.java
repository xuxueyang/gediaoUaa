package uaa.web.rest.demo.guava;

import sun.misc.Unsafe;

public class AtomicLong extends Number implements java.io.Serializable {
    private static final long serialVersionUID = 1927816293512124184L;

    // setup to use Unsafe.compareAndSwapLong for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;

    /**
     * Records whether the underlying JVM supports lockless
     * compareAndSwap for longs. While the Unsafe.compareAndSwapLong
     * method works in either case, some constructions should be
     * handled at Java level to avoid locking user-visible locks.
     */
    static final boolean VM_SUPPORTS_LONG_CAS = VMSupportsCS8();

    /**
     * Returns whether underlying JVM supports lockless CompareAndSet
     * for longs. Called only once and cached in VM_SUPPORTS_LONG_CAS.
     */
    private static native boolean VMSupportsCS8();

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicLong.class.getDeclaredField("value"));//获取一个内存地址的偏移量
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile long value;

    //构造值为 initialValue的AtomicLong对象
    public AtomicLong(long initialValue) {
        value = initialValue;
    }

    //默认构造函数
    public AtomicLong() {
    }

    //获取当前值
    public final long get() {
        return value;
    }
    //设置为newValue
    public final void set(long newValue) {
        value = newValue;
    }

    //延迟设为newValue。值期间会改变，最终设置为给定的值。
    public final void lazySet(long newValue) {
        unsafe.putOrderedLong(this, valueOffset, newValue);//设置对象中valueOffset偏移地址对应的object型field的值为指定值
    }

    //原子方式设置为当前值，返回原先的值。
    public final long getAndSet(long newValue) {
        while (true) {
            long current = get();
            if (compareAndSet(current, newValue))
                return current;
        }
    }

    // 如果当前值 == expect，则以原子方式将该值设置为update。更改成功返回true，否则返回false且不修改原值。
    public final boolean compareAndSet(long expect, long update) {
        return unsafe.compareAndSwapLong(this, valueOffset, expect, update);
    }

    //其实和上面的方法一样。
    public final boolean weakCompareAndSet(long expect, long update) {
        return unsafe.compareAndSwapLong(this, valueOffset, expect, update);
    }

    //原子自增，返回原值
    public final long getAndIncrement() {
        while (true) {
            long current = get();
            long next = current + 1;
            if (compareAndSet(current, next))
                return current;
        }
    }

    //原子自减，返回原值
    public final long getAndDecrement() {
        while (true) {
            long current = get();
            long next = current - 1;
            if (compareAndSet(current, next))
                return current;
        }
    }

    //原子增delta，返回原值
    public final long getAndAdd(long delta) {
        while (true) {
            long current = get();
            long next = current + delta;
            if (compareAndSet(current, next))
                return current;
        }
    }

    //原子自增，返回新值
    public final long incrementAndGet() {
        for (;;) {
            long current = get();
            long next = current + 1;
            if (compareAndSet(current, next))
                return next;
        }
    }

    //原子自减，返回新值
    public final long decrementAndGet() {
        for (;;) {
            long current = get();
            long next = current - 1;
            if (compareAndSet(current, next))
                return next;
        }
    }

    //原子增delta，返回新值
    public final long addAndGet(long delta) {
        for (;;) {
            long current = get();
            long next = current + delta;
            if (compareAndSet(current, next))
                return next;
        }
    }

    //返回String类型
    public String toString() {
        return Long.toString(get());//对象转换
    }

    //返回int类型数值
    public int intValue() {
        return (int)get();
    }
    //返回long类型数值
    public long longValue() {
        return get();
    }
    //返回float类型数值
    public float floatValue() {
        return (float)get();
    }
    //返回double类型数值
    public double doubleValue() {
        return (double)get();
    }

}
