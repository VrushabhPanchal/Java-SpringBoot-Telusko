import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubMakeReposPrivate {
    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN"); // Read from env variable
    private static final String USERNAME = System.getenv("GITHUB_USERNAME"); // Read from env variable

    public static void main(String[] args) {
        if (GITHUB_TOKEN == null || USERNAME == null) {
            System.out.println("Error: GITHUB_TOKEN or GITHUB_USERNAME is not set.");
            System.exit(1);
        }

        try {
            JSONArray repos = getPublicRepositories();
            if (repos != null) {
                for (int i = 0; i < repos.length(); i++) {
                    JSONObject repo = repos.getJSONObject(i);
                    String repoName = repo.getString("name");
                    if (!repo.getBoolean("private")) { // Check if it's public
                        makeRepoPrivate(repoName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getPublicRepositories() throws IOException {
        String apiUrl = "https://api.github.com/user/repos?per_page=100";
        HttpURLConnection conn = createConnection(apiUrl, "GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            String jsonResponse = readResponse(conn);
            conn.disconnect();
            return new JSONArray(jsonResponse);
        } else {
            System.out.println("Failed to fetch repositories. HTTP Response Code: " + responseCode);
            return null;
        }
    }

    private static void makeRepoPrivate(String repoName) throws IOException {
        String apiUrl = "https://api.github.com/repos/" + USERNAME + "/" + repoName;
        String jsonInputString = "{\"private\": true}";

        HttpURLConnection conn = createConnection(apiUrl, "PATCH");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            System.out.println("✅ Success: Repository '" + repoName + "' is now private.");
        } else {
            System.out.println("❌ Failed to change '" + repoName + "' to private. HTTP Response Code: " + responseCode);
        }

        conn.disconnect();
    }

    private static HttpURLConnection createConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(method.equals("PATCH"));
        return conn;
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
