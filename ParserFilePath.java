import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

public class ParserFilePath {
    private String file_id;
    private String file_path;
    private String botToken;
    ParserFilePath(String botToken, String file_id){
        this.botToken=botToken;
        this.file_id=file_id;
    }

    public String getfile_path() throws MalformedURLException, IOException {
        String finalUrl="https://api.telegram.org/bot"+ botToken +"/getFile?file_id=" + file_id;
        URL url = new URL(finalUrl);

        URLConnection con = null;

        con=url.openConnection();

        InputStream is = con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line=br.readLine())!=null){
            sb.append(line);
        }
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONObject jsonResult = jsonObject.getJSONObject("result");
        this.file_path = jsonResult.getString("file_path");
        return file_path;
    }
}
