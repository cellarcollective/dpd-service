package co.cellarcollective.tools.chronopostapiemu.rest;

public class ReplayEventShort {

    private final Integer delay;
    private final String code;

    public ReplayEventShort(Integer delay, String code) {
        this.delay = delay;
        this.code = code;
    }

    public Integer getDelay() {
        return delay;
    }

    public String getCode() {
        return code;
    }
}
