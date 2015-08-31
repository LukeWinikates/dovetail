package example;


public class Persister {
    public int persist(int value) {
        if(value > 1000) {
            throw new RuntimeException();
        }
        return value;
    }
}
