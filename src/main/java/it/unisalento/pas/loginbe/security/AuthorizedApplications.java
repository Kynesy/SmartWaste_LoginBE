package it.unisalento.pas.loginbe.security;

import java.util.HashMap;
import java.util.Map;

public class AuthorizedApplications {
    private final Map<String, String> APPLICATION_ID = new HashMap<>();

    public AuthorizedApplications(){
        APPLICATION_ID.put("CityHall", "O1fwrQULUYhvrKil6GhR3PV0X9Np3cT/2VgKMUvq8PFjRC0rNJJxIR8WYS/1tkuc");
        APPLICATION_ID.put("WasteAgency", "DMweNbhCNYxCqOrnTwglWnX2yIZRChC0wc1W+UaqT/7Zr+dlEz00Od1nHDAKB/13");
    }

    public boolean isApplicationIDnotValid(String applicationId){
        return !APPLICATION_ID.containsValue(applicationId);
    }
}
