
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String[][] list = Agent.getAgents();
        Agent[] agent = Agent.createAgent(list);
        HtmlBuilder index = new HtmlBuilder();
        index.createIndexHtml(agent);
        HtmlBuilder fileAgent = new HtmlBuilder();
        fileAgent.createFileAgent(agent);
    }
}
