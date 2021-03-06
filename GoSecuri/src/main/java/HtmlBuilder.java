import java.io.*;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.json.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class HtmlBuilder {
    private StringBuilder _stringBuilder = new StringBuilder();

    public void createIndexHtml(Agent[] agents)  throws IOException {
        File htmlIndexTemplate = new File("/var/jenkins_home/workspace/GoSecuri/templates/index.html");
        String htmlString = FileUtils.readFileToString(htmlIndexTemplate, "UTF8");
        String content = "";
        for (int i = 0; i<agents.length; i++){
            content += "<li><a href='./"+agents[i].id+".html'>"+agents[i].firstName+" "+agents[i].lastName+"</a></li>";
        }
        htmlString = htmlString.replace("$listeAgent",content);
        File newIndex = new File("/docker/www/index.html");
        FileUtils.writeStringToFile(newIndex, htmlString, "UTF8");
    }

    public void createFileAgent(Agent[] agents) throws  IOException{
        for (int i = 0; i<agents.length; i++) {
            File htmlIndexTemplate = new File("/var/jenkins_home/workspace/GoSecuri/templates/agent.html");
            String htmlString = FileUtils.readFileToString(htmlIndexTemplate, "UTF8");
            htmlString = htmlString.replace("$nom", agents[i].lastName);
            htmlString = htmlString.replace("$prenom", agents[i].firstName);
            htmlString = htmlString.replace("$role", agents[i].role);
            htmlString = htmlString.replace("$img", agents[i].image);
            String material = "";
            for (int j = 0; j<agents[i].materials.length; j++){
                material += "<div><input type='checkbox' checked disabled>"+agents[i].materials[j]+"</div>";
            }
            htmlString = htmlString.replace("$listeMateriel", material);
            File newIndex = new File("/docker/www/"+agents[i].id+".html");
            FileUtils.writeStringToFile(newIndex, htmlString, "UTF8");
        }
    }

}
