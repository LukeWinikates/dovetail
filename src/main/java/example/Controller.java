package example;

public class Controller {
    private Persister persister;

    public Controller(Persister persister) {
        this.persister = persister;
    }

    public Controller(){}

    public int post(int dummy) {
        try{
            persister.persist(dummy);
            return 200;
        } catch (Exception e) {
            return 400;
        }
    }
}
