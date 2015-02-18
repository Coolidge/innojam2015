package de.holger_oehm.xfd.jenkins;

public class DTO {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public State getState() {
        if (value == null) {
            return State.RED;
        }
        System.out.println("value:" + value);
        Integer val = 0;
        try {
            val = Integer.valueOf(value);
        } catch (Exception e) {
        }

        if (val <= 30) {
            return State.RED;
        }

        if (val <= 60) {
            return State.YELLOW;
        }

        return State.GREEN;
    }

}
