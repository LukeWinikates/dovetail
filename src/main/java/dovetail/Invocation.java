package dovetail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.Matchers.any;

public class Invocation {
    private Object instance;
    private final Object self;
    private final Method thisMethod;
    private final Method proceed;
    private final Object[] args;

    public Invocation(Object instance, Object self, Method thisMethod, Method proceed, Object[] args) {
        this.instance = instance;
        this.self = self;
        this.thisMethod = thisMethod;
        this.proceed = proceed;
        this.args = args;
    }

    public Object invoke(){
        return invokeOn(instance, args);
    }

    public Object getInstance(){
        return this.instance;
    }

    public Object invokeOn(Object target, Object[] args) {
        try {
            return thisMethod.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] mockedArgs () {
        return Arrays.asList(args).stream().map(ar -> any(ar.getClass())).collect(Collectors.toList()).toArray();
    }
}
