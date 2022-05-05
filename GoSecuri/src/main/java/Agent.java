
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;

public class Agent {
    public String id;
    public String firstName;
    public String lastName;
    public String role;
    public String image;
    public String[] materials;

    public Agent(String id,String firstName, String lastName, String role, String image,String[] materials){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.materials = materials;
        this.image = image;
    }

    public static Agent[] createAgent(String[][] listAgents) throws IOException {
        int l = listAgents.length;
        Agent[] list = new Agent[l];
        for (int i = 0; i< l; i++){
            String[] materiel = getMateriel(listAgents[i][0]);
            list[i] = new Agent(listAgents[i][0], listAgents[i][1], listAgents[i][2], listAgents[i][3], listAgents[i][4], materiel);
        }
        return list;
    }

    public static String[][] getAgents() throws IOException {
        String url_txt = "https://github.com/JulienOo/GoSecuri/blob/main/staff.txt";
        Document doc = Jsoup.connect("https://github.com/JulienOo/GoSecuri/blob/main/staff.txt").get();
        Element body = doc.body();
        Elements table = body.getElementsByClass("blob-code blob-code-inner js-file-line");
        List<String> list_agent = table.eachText();
        String[][] agents = new String[list_agent.size()][];
        for (int i = 0; i < list_agent.size(); i++) {
            agents[i] = new String[5];
            agents[i][0] = list_agent.get(i);
        }
        String base_url = "https://github.com/JulienOo/GoSecuri/blob/main/Agents/";
        String base_photo = "https://github.com/JulienOo/GoSecuri/blob/main/Photos/";
        for (int i = 0; i < agents.length; i++) {
            Element body_agent = Jsoup.connect(base_url+agents[i][0]+".txt").get().body();
            Elements info_agent = body_agent.getElementsByClass("blob-code blob-code-inner js-file-line");
            List<String> list_info = info_agent.eachText();
            agents[i][1]=list_info.get(0); //
            agents[i][2]=list_info.get(1); //
            agents[i][3]=list_info.get(2); //
            agents[i][4]= base_photo+agents[i][0]+".jpg?raw=true"; // image
        }
        return agents;
    }

    public static String[] getMateriel(String id) throws IOException {
        String url_agent = "https://github.com/JulienOo/GoSecuri/blob/main/Agents/"+id+".txt";
        String url_liste = "https://github.com/JulienOo/GoSecuri/blob/main/liste.txt";
        Element body_agent = Jsoup.connect(url_agent).get().body();
        Element body_liste = Jsoup.connect(url_liste).get().body();
        Elements info_agent = body_agent.getElementsByClass("blob-code blob-code-inner js-file-line");
        Elements info_liste = body_liste.getElementsByClass("blob-code blob-code-inner js-file-line");
        List<String> list_info = info_agent.eachText();
        String[] array_info = list_info.toArray(new String[list_info.size()]);
        List<String> list_liste = info_liste.eachText();
        String[] array_liste = list_liste.toArray(new String[list_liste.size()]);
        String[][] array_materiels = new String[array_liste.length][];
        for (int i = 0; i < array_liste.length; i++) {
            String[] mat = array_liste[i].split(" ");
            array_materiels[i] = new String[2];
            array_materiels[i][0] = mat[0];
            String nom = "";
            for (int j = 1; j < mat.length; j++) {
                nom = nom + mat[j] + " ";
            }
            array_materiels[i][1] = nom;
        }
        String[] res = Arrays.copyOfRange(array_info, 4, array_info.length);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < array_materiels.length; j++) {
                if(Objects.equals(res[i], array_materiels[j][0])){
                    res[i] = array_materiels[j][1];
                }
            }
        }
        return res;
    }
}


