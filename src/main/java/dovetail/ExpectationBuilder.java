package dovetail;

public class ExpectationBuilder<T> {


    private final T subject;
    private final Invocation invocation;

    public ExpectationBuilder(T t, Invocation invocation) {
        this.subject = t;
        this.invocation = invocation;
    }

    public Expectation toThrow() {
        return new ToThrowExpectation(subject, invocation);
    }

    public Expectation toReturn(T returnValue) {
        return new EqualityExpectation(invocation, returnValue);
    }
}
