package com.qsocialnow.common.predicate;

public class Foo {

    private Long id;
    private String attr;

    public Foo() {
    }

    public Foo(Long id, String attr) {
        this.id = id;
        this.attr = attr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }
}
