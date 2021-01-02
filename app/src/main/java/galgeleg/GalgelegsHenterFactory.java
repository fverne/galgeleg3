package galgeleg;

public class GalgelegsHenterFactory {
    public GalgelegsHenter getGalgelegsHenter(String henterType) {
        if (henterType == null) {
            return null;
        }
        if (henterType.equalsIgnoreCase("DR")){
            return new GalgelegsHenterDR();
        }
        if (henterType.equalsIgnoreCase("Regneark")){
            return new GalgelegsHenterRegneark();
        }

        return null;
    }
}
