package com.asdflj.ae2thing.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class NeCharUtil {

    public static final NeCharUtil INSTANCE = new NeCharUtil();

    private Method m;
    private Object o;

    public NeCharUtil() {
        try {
            Class<?> c = Class.forName("net.moecraft.nechar.NotEnoughCharacters");
            Field f = c.getField("CONTEXT");
            this.o = f.get(null);
            this.m = this.o.getClass()
                .getMethod("contains", String.class, String.class);
        } catch (Exception e) {

        }
    }

    private boolean _contains(String input, String text) {
        try {
            return (boolean) this.m.invoke(this.o, text, input);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean contains(String input, String text) {
        if (ModAndClassUtil.NECHAR) {
            return this._contains(input, text);
        } else {
            return text.contains(input);
        }
    }

    public boolean matcher(Pattern p, CharSequence text) {
        if (ModAndClassUtil.NECHAR) {
            return this._contains(p.pattern(), (String) text);
        } else {
            return p.matcher(text)
                .find();
        }
    }
}
