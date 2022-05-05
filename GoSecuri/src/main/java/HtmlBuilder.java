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

    public String toString() {
        return _stringBuilder.toString();
    }

    public void append(String str) {
        _stringBuilder.append(str);
    }

    private static String GetResourceByName(String viewName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(viewName);
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }


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
                material += "<div><input type='checkbox' checked>"+agents[i].materials[j]+"</div>";
            }
            htmlString = htmlString.replace("$listeMateriel", material);
            File newIndex = new File("/docker/www/"+agents[i].id+".html");
            FileUtils.writeStringToFile(newIndex, htmlString, "UTF8");
        }
    }

    public static String getHTML(String urlToRead) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
