import dovetail.Expectation;
import example.Controller;
import example.Persister;
import org.junit.Test;

import static dovetail.Expect.expect;
import static dovetail.Expect.expectable;


public class ControllerTest {
    class WhenPersisterThrows_Returns400 implements PropertyTestCase {
        public Expectation expectation() {
            Persister persister = new PersisterTest().new WhenInputIsVeryLarge_Throws().expectation().asMock(Persister.class);
            Controller subject = expectable(new Controller(persister));
            return expect(subject.post(1)).toReturn(400);
        }
    }

    class WhenPersisterWorks_Returns200 implements PropertyTestCase {
        public Expectation expectation() {
            Persister persister = new PersisterTest().new WhenInputIsSmall_ReturnsTrue().expectation().asMock(Persister.class);
            Controller subject = expectable(new Controller(persister));
            return expect(subject.post(1)).toReturn(200);
        }
    }

    @Test
    public void runTests(){
        new WhenPersisterThrows_Returns400().expectation().doAssert();
        new WhenPersisterWorks_Returns200().expectation().doAssert();
    }
}
