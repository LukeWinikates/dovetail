package dovetail;

import static org.fest.assertions.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToThrowExpectation<T> implements Expectation {
    private T subject;
    private Invocation invocation;

    public ToThrowExpectation(T subject, Invocation invocation) {
        this.subject = subject;
        this.invocation = invocation;
    }

    public void doAssert(){
        try {
            invocation.invoke();
            fail("no exception thrown");
        } catch (RuntimeException e){

        }
    }

    public <T> T asMock(Class<T> tClass){
        T mock = (T) mock(invocation.getInstance().getClass());
        when(invocation.invokeOn(mock, invocation.mockedArgs()))
            .thenThrow(RuntimeException.class);
        return mock;
    }
}
