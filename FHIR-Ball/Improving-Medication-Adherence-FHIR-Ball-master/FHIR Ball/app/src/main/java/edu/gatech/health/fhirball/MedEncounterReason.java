package edu.gatech.health.fhirball;

import java.util.*;

public enum MedEncounterReason {

    TAKEN(0, "I've taken the meds!", null),

    AWAY(1, "Unable because I'm away", "b"),
    ASLEEP(2, "Unable because I'm asleep", "c"),
    VOMIT(3, "I took but then vomited it out", "d"),
    OTHER(4, "Unable for other reason", "a"),

    INCORRECTLY_RECEIVED(5, "Incorrectly notified", null),

    NO_RESPONSE_RECEIVED(100, "", null);

    private final int localCode;
    private final String display;
    private final String fhirCode;
    private static final Map<Integer, MedEncounterReason> localCodeToEnum;
    private static final Map<String, MedEncounterReason> displayToEnum;
    private static final Map<String, MedEncounterReason> fhirCodeToEnum;

    MedEncounterReason(int localCode, String display, String fhirCode) {
        this.localCode = localCode;
        this.display = display;
        this.fhirCode = fhirCode;
    }

    public int getLocalCode() {
        return localCode;
    }

    static {
        Map<Integer, MedEncounterReason> map = new HashMap<>();
        for (MedEncounterReason instance : MedEncounterReason.values()) {
            map.put(instance.getLocalCode(),instance);
        }
        localCodeToEnum = Collections.unmodifiableMap(map);
    }

    static {
        Map<String, MedEncounterReason> map = new HashMap<>();
        for (MedEncounterReason instance : MedEncounterReason.values()) {
            map.put(instance.getDisplayName(),instance);
        }
        displayToEnum = Collections.unmodifiableMap(map);
    }

    static {
        Map<String, MedEncounterReason> map = new HashMap<>();
        for (MedEncounterReason instance : MedEncounterReason.values()) {
            map.put(instance.getFhirCode(),instance);
        }
        fhirCodeToEnum = Collections.unmodifiableMap(map);
    }

    public static MedEncounterReason get (int code) {
        return localCodeToEnum.get(code);
    }

    public static MedEncounterReason fromString(String s) {
        return displayToEnum.get(s);
    }

    public String getDisplayName() {
        return display;
    }

    public String getFhirCode() {
        return fhirCode;
    }

    public static List<MedEncounterReason> getUserAvailableReasons() {
        List<MedEncounterReason> userReasons = new ArrayList<>(Arrays.asList(values()));
        userReasons.remove(NO_RESPONSE_RECEIVED);
        return userReasons;
    }

    @Override
    public String toString(){
        return display;
    }
}
