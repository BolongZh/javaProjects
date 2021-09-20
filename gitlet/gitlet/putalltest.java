package gitlet;

import java.util.HashMap;

public class putalltest {
    public static void main(String[] args) {
        HashMap<String, String> forBlob = new HashMap<>();
        HashMap<String, String> log = new HashMap<>();
        forBlob.put("k","k");
        log.putAll(forBlob);
        log.put("j","j");
        System.out.print(forBlob.keySet().toString());
    }
}
