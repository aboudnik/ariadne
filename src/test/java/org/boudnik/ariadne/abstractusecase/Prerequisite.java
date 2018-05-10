package org.boudnik.ariadne.abstractusecase;

public class Prerequisite {
    private String parameter1;
    private String parameter2;

    public Prerequisite setParameter1(String parameter1) {
        this.parameter1 = parameter1;
        return this;
    }

    public Prerequisite setParameter2(String parameter2) {
        this.parameter2 = parameter2;
        return this;
    }
}
