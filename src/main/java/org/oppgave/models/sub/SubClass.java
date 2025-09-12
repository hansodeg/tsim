package org.oppgave.models.sub;

public enum SubClass {
    LA("LA", 110, 10, 20, 33),
    OH("OH", 170, 13, 20, 25),
    YT("TY", 175, 23, 20, 27),
    O2("O2", 154, 18.2, 15, 32),
    V3("V3", 107, 10.8, 15, 30),
    AL("AL", 81, 9.5, 20, 41),
    RS("RS", 73.6, 7.6, 15, 25),
    T2("T2", 60, 6.2, 11, 21);

    private final Sub sub;

    SubClass(String Id, double length, double width, double cruiseSpeed, double maxSpeed) {
        this.sub = new Sub(Id, length, width, cruiseSpeed, maxSpeed);
    }

    public Sub get() {
        return sub;
    }


}
