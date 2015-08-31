package dovetail;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EqualityExpectation<T> implements Expectation {
    private Invocation invocation;
    private final T returnValue;

    public EqualityExpectation(Invocation i, T returnValue) {
        invocation = i;
        this.returnValue = returnValue;
    }

    public void doAssert(){
        assertThat(invocation.invoke()).isEqualTo(returnValue);
    }

    @Override
    public <T> T asMock(Class<T> tClass) {
        T mock = (T) mock(invocation.getInstance().getClass());
        when(invocation.invokeOn(mock, invocation.mockedArgs()))
                .thenReturn(returnValue);
        return mock;
    }


}
