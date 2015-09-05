# Dovetail

Named for wooden joints that are carefully cut to lock together securely, Dovetail is a testing library that handles assertions a little differently. Instead of the traditional `assert` or `assertThat`, which is called to create a *side effect*, Dovetail asserts and matchers return *object values*. This allows assertions to be named, reused, and transformed. The most valuable transformation this enables is turning asserts -- the *end* states of tests -- into test setup in the form of doubles -- ingredients for other tests.

## Motivation

A downside to unit testing with test doubles is the burden of keeping those doubles in sync with actual implementations. A client that uses a particular dependency but is tested independently of that dependency is desirably loosely coupled, but also poses a challenge if the contract with the dependency changes over time. ([1][rainsberger1], [2][rainsberger2])

In addition to the accuracy problem, there's also a completeness problem. If a dependency has several outcomes (e.g., it returns `true` or `false` under normal circumstances but throws exceptions under others), then all those cases ought to be consided, and possibly tested when building a class that depends on it. Since setting up test scenarios is work, creating first-class representations of a class's contract stands to reduce duplication and increases the visibility of changes and missed edge cases in the class's contract.

## Assertions using Dovetail expect

If we make assertions a first-class concept in our test suite, we can transform assertions into mocks and even build completeness-checkers. Dovetail makes this possible this by modifying the JUnit formula in two ways:

* Where scenarios are *methods* in JUnit, they are *classes* in dovetail
* JUnit methods return `void`, but the Dovetail interface `PropertyTestCase.expectation()` returns an `Expectation`, which can be transformed into an assertion or into a mock.



```Java
public class PersisterTest {
    private final Persister subject;

    public PersisterTest() {
        this.subject = expectable(new Persister());
    }

    class WhenInputIsVeryLarge_Throws implements PropertyTestCase {
        public Expectation expectation() {
            return expect(subject.persist(1000 * 1000)).toThrow();
        }
    }
 }

public class ControllerTest {
    class WhenPersisterThrows_Returns400 implements PropertyTestCase {
        private PropertyTestCase precondition = new PersisterTest().
            new WhenInputIsVeryLarge_Throws();
    
        public Expectation expectation() {
            Expectation expectation = precondition.expectation();
            Persister persister = expectation.asMock(Persister.class);
            Controller subject = expectable(new Controller(persister));
            return expect(subject.post(1)).toReturn(400);
        }
    }
}
``` 

## Work in progress
This library is a proof of concept, and should not be considered complete or reliable. 

## Possible extensions:


A syntax for, perhaps in the form of an annotation like `@DependencyContract(PersisterTest.class)`, which asserts that all PropertyTestCases in PersisterTest are complemented by test cases in the annotated class.

Multiple mocking modes --> stubbing versus verification.

Capturing setup to enable preconditions to be composed, allowing integration test setup through the composition of unit tests.

A more abstract `QuickCheck`, aware of the behaviors of a dependency. The traditional `QuickCheck` [3][quickcheck] available in Haskell or Erlang statically analyzes functions that take input parameters and attempts to find problems by generating interesting values that break the function or violate an invariant -- for function that takes `ints`, interesting vlaues might be Int32.MaxValue, 0, -1, etc. Fuzzing can be done with functions that take primitive inputs, but is difficult with dependencies that have behavior, because there is no way of generating a range of relevant inputs.

[rainsberger1]: http://blog.thecodewhisperer.com/2009/10/08/who-tests-the-contract-tests/ "Who tests the contract tests" 
[rainsberger2]: http://blog.thecodewhisperer.com/2010/10/16/integrated-tests-are-a-scam/ "Integrated tests are a scam"
[quickcheck]: http://book.realworldhaskell.org/read/testing-and-quality-assurance.html "Quickcheck"