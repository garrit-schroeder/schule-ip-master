package back.componentes;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Created by meganoob on 09.06.16.
 */
public class SaveLoad {

    private final String path;

    private Set<Network> networks = new TreeSet<Network>();

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

    }


}
