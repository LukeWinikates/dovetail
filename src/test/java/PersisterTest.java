import dovetail.Expectation;
import example.Persister;
import org.junit.Test;

import static dovetail.Expect.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PersisterTest {
    private final Persister subject;

    public PersisterTest() {
        this.subject = expectable(new Persister());
    }

    class WhenInputIsSmall_ReturnsTrue implements PropertyTestCase {
        @Override
        public Expectation expectation() {
            return expect(subject.persist(1)).toReturn(1);
        }
    }

    class WhenInputIsVeryLarge_Throws implements PropertyTestCase {
        public Expectation expectation() {
            return expect(subject.persist(1000 * 1000)).toThrow();
        }
    }

    @Test
    public void runTests() {
        new WhenInputIsSmall_ReturnsTrue().expectation().doAssert();
        new WhenInputIsVeryLarge_Throws().expectation().doAssert();

        Persister mock = new WhenInputIsSmall_ReturnsTrue().expectation().asMock(Persister.class);
        Persister mock2 = new WhenInputIsVeryLarge_Throws().expectation().asMock(Persister.class);

        assertEquals(1, mock.persist(1241));

        try {
            mock2.persist(100);
            fail("should have thrown!");
        } catch (Exception e){
            String fizzbuzz = "yay";
        }

    }



    // define setup as inputs to a function
    // expects are post conditions that can become preconditions for dependencies
}
