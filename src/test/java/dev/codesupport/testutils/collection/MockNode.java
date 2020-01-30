package dev.codesupport.testutils.collection;

import javax.validation.ElementKind;
import javax.validation.Path;

public class MockNode implements Path.Node {

    @Override
    public String getName() {
        return "testNode";
    }

    @Override
    public boolean isInIterable() {
        return true;
    }

    @Override
    public Integer getIndex() {
        return 0;
    }

    @Override
    public Object getKey() {
        return "test";
    }

    @Override
    public ElementKind getKind() {
        return null;
    }

    @Override
    public <T extends Path.Node> T as(Class<T> aClass) {
        return null;
    }

    @Override
    public String toString() {
        return "MockNode";
    }
}
