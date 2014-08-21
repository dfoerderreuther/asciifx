package de.eleon.asciifx.fx.controller;

public interface SubController<P, C> {

    void initParent(P parent);

    void initControl(C control);

}
