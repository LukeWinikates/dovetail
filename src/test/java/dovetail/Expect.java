package dovetail;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

public class Expect {
    static final Queue<Invocation> invocations = new LinkedList<>();

    public static <T> T expectable(final T instance) {
        return createInstance(instance, instance.getClass());
    }

    private static <T> T createInstance(T instance, Class<?> superclass) {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(superclass);
        factory.setFilter(notInheredFromObject());
        MethodHandler handler = rememberCall(instance);

        try {
            return (T) (factory.create(new Class<?>[0], new Object[0], handler));
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static MethodFilter notInheredFromObject() {
        return method -> method.getDeclaringClass() != Object.class;
    }

    private static <T> MethodHandler rememberCall(T instance) {
        return (self, thisMethod, proceed, args) -> {
            invocations.add(new Invocation(instance, self, thisMethod, proceed, args));
            System.out.println("Handling " + thisMethod + " via the method handler");
            return 1;
        };
    }

    public static <T> ExpectationBuilder<T> expect(T thing) {
        return new ExpectationBuilder<>(thing, invocations.poll());
    }
}
