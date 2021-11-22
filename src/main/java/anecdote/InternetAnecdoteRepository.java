package anecdote;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InternetAnecdoteRepository extends RandomRatableAnecdoteRepository {

    private final String uri = "http://rzhunemogu.ru/Rand.aspx?CType=1";
    private final int requestTimeout = 1500;

    private final CloseableHttpClient client;

    public InternetAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new RatableAnecdote(x)).collect(Collectors.toList()));
    }

    public InternetAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        super(anecdotes);
        var config = RequestConfig.custom().setConnectTimeout(requestTimeout).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    @Override
    public boolean hasAnecdotes() {
        return true;
    }

    @Override
    public IAnecdote getNextAnecdote() {
        try {
            var response = sendRequestAnecdote();
            var contents = readHttpContentsOrNull(response.getEntity());
            var text = extractAnecdoteTextOrNull(contents);
            response.close();

            var anecdote = new RatableAnecdote(text);
            toldAnecdotes.add(anecdote);
            listenRatableAnecdote(anecdote);
            if (bannedAnecdotes.contains(anecdote))
                return super.getNextAnecdote();
            return anecdote;
        } catch (Exception ex) {
            return super.getNextAnecdote();
        }
    }

    private CloseableHttpResponse sendRequestAnecdote() throws IOException, ConnectTimeoutException {
        var request = new HttpGet(uri);
        var response = client.execute(request);
        return response;
    }

    private String readHttpContentsOrNull(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException ex) {
            return null;
        }
    }

    private String extractAnecdoteTextOrNull(String httpContents) {
        if (httpContents == null || httpContents.isBlank())
            return null;

        var p = ".*?<content.*?>(.*?)</content>.*?";
        var pattern = Pattern.compile(p, Pattern.DOTALL);
        var matcher = pattern.matcher(httpContents);

        if (matcher.find())
            return matcher.group(1);

        return null;
    }

}