# Dovetail
Exploring ideas around generating test scenarios for a given module based on the assertions used int the tests of its dependencies

# Motivation

A downside to unit testing with test doubles is the burden of keeping doubles in sync with actual implementations. A client that uses a particular dependency but is tested independently of that dependency may drift over time if the dependency's behavior changes.

In addition to the accuracy problem, there's also a completeness problem. If a dependency has several outcomes (say it returns a `true` or `false` under normal circumstances but throws exceptions under others), then all those cases ought to be consided.

# An observation

Tests that use a dependency often restate the asssertions of that dependency's tests in the form of setup for mocks. For example, if we assert (using a made-up, Jasmine-inspired `expect` syntax):


```Java
@Test
public void save_whenModelIsInvalid_ThrowsValidationError() {
   expect(() -> subject.save(model)).toThrow(ValidationError.class);
}

@Test
public void save_whenModelIsValid_SetsModelId() {
   subject.save(model));
   expect(model.getId()).toBeGreaterThan(0);
}

``` 



we may later set up test cases that are a bit like:


```Java
@Test
public void post_whenSaveSucceeds_Returns200() {
   expect(subject.post().status()).toBe(200);
expect(subject.post().body().getId()).toBe(1);
}

@Test
public void post_whenSaveFails_Returns400() {
   when(repository.save(model)).thenThrow(ValidationError.class)
   expect(subject.post().status()).toBe(400);
}
```

Just picking on the validation error example, there's a restatement of the dependency's behavior. The `when` is a restatement of the `expect`.

If we promote the expectations to a first class object of their own, we could avoid this duplication, and if the specific exception thrown were to change, or if the exception behavior were eliminated in favor of a different pattern, feedback about that change would be immediate. We would have DRY contact tests.
