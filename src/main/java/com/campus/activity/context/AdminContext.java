// com.campus.activity.context.AdminContext.java
package com.campus.activity.context;

public class AdminContext {
    private static final ThreadLocal<Long> ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> NAME_HOLDER = new ThreadLocal<>();

    public static void set(Long id, String name) {
        ID_HOLDER.set(id);
        NAME_HOLDER.set(name);
    }

    public static Long getId() { return ID_HOLDER.get(); }
    public static String getName() { return NAME_HOLDER.get(); }

    public static void clear() {
        ID_HOLDER.remove();
        NAME_HOLDER.remove();
    }
}