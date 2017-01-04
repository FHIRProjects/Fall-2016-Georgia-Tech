package edu.gatech.mass.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.mass.model.MedicationOrder;

public class SampleMedicalOrderDataProvider {
    public static List<MedicationOrder> medicationOrderList;
    public static Map<String, MedicationOrder> medicationOrderMap;

    static {
        medicationOrderList = new ArrayList<>();
        medicationOrderMap = new HashMap<>();
        addMedicationOrder(new MedicationOrder(null, "232850", "Mark L Braunstein, MD", "00339647811",
                "24 HR Metformin hydrochloride 500 MG Extended Release Tablet [Glucophage]",
                60.0, "mg", "2011-04-23T00:00:00-04:00", "2011-04-23T00:00:00-04:00", 4, 90.0));

        addMedicationOrder(new MedicationOrder(null, "999999", "Dr. DooLittle", "99999999999",
                "This is just some test data",
                999.0, "mg", "2011-04-23T00:00:00-04:00", "2011-04-23T00:00:00-04:00", 999, 999.0));
    }

    private static void addMedicationOrder(MedicationOrder medicationOrder){
        medicationOrderList.add(medicationOrder);
        medicationOrderMap.put(medicationOrder.getId(), medicationOrder);
    }

}
