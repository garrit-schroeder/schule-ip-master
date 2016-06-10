package back.componentes;

import java.io.File;
import java.io.IOException;
import java.util.*;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Created by meganoob on 09.06.16.
 */
public class SaveLoad {

    private final String path;
    private Set<Network> networks = new TreeSet<Network>();
    private List<Network> networks2 = new ArrayList<Network>();

    public SaveLoad(String path, Set<Network> networks) {
        this.path = path;
        this.networks = networks;
    }
    public void save(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert object to JSON string and save into a file directly
            mapper.writeValue(new File(path), networks);

            // Convert object to JSON string
            String jsonInString = mapper.writeValueAsString(networks);
            System.out.println(jsonInString);

            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(networks);
            System.out.println(jsonInString);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        ObjectMapper mapper = new ObjectMapper();
        Set<Network> res = new TreeSet<Network>();
        try {

            // Convert JSON string from file to Object
            Set<LinkedHashMap> networks1 = mapper.readValue(new File(path), Set.class);
            for (LinkedHashMap l:networks1
                 ) {
                Network network = new Network(new IPv4Address((String) l.get("NetworkIP")), (Integer) l.get("prefix"));
                ArrayList<LinkedHashMap> l1 = (ArrayList<LinkedHashMap>) l.get("subnets");
                for (LinkedHashMap lh: l1
                     ) {
                    //network.addSubnet();
                }
            }
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Network> setToList(Set<Network> set){
        ArrayList<Network> res = new ArrayList<Network>();
        for (Network n: set) {
            res.add(n);
        }
        return res;
    }
    public Set<Network> listToSet(ArrayList<Network> list){
        Set<Network> res = new TreeSet<Network>();
        for (Network n : list) {
            System.out.println(n);
        }
        return res;
    }

}
