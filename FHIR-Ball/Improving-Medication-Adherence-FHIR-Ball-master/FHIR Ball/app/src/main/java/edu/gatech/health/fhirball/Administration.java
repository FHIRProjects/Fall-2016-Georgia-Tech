package edu.gatech.health.fhirball;

public class Administration {

    private final String medicine;
    private final long time;
    private final MedEncounterReason reason;

    public Administration(String medicine, long time, MedEncounterReason reason) {
        this.medicine = medicine;
        this.time = time;
        this.reason = reason;
    }

    public String getMedicine() {
        return medicine;
    }

    public long getTime() {
        return time;
    }

    public MedEncounterReason getReason() {
        return reason;
    }
}
