package dovetail;

public interface Expectation {
    void doAssert();
    <T> T asMock(Class<T> tClass);
}
