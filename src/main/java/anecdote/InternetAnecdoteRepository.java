package anecdote;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import utils.Randomizer;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Представляет собой репозиторий, который состоит из анекдотов
 * и который выдает их один за другим в случайной неповторяющейся последовательности.
 * Анекдоты берутся с некоторого сайта анекдотов, а также из локального хранилища.
 * Анекдоты такого репозитория поддерживают оценивание и добавление концовки.
 */
public class InternetAnecdoteRepository extends RandomRatableUnfinishedAnecdoteRepository {
    // todo: serialize when bot stopped

    private final String uri = "http://rzhunemogu.ru/Rand.aspx?CType=1";
    private final int requestTimeout = 1500;

    private final CloseableHttpClient client;

    public InternetAnecdoteRepository() {
        super(AnecdotesConfiguration.deserializeAnecdotesConfig());
        var requestConfig = RequestConfig.custom().setConnectTimeout(requestTimeout).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    @Override
    public boolean hasAnecdotes() {
        return true;
    }

    @Override
    public IAnecdote getNextAnecdote() {
        // todo: prevent from collisions
        try {
            if (pickNextFromInternet())
                return getAnecdoteFrom(uri);
            else
                return super.getNextAnecdote();
        } catch (Exception ex) {
            return super.getNextAnecdote();
        }
    }

    private boolean pickNextFromInternet() {
        return Randomizer.getRandomNumber(2) == 0;
    }

    private IAnecdote getAnecdoteFrom(String uri) throws IOException, ConnectTimeoutException {
        var response = sendRequestAnecdote(uri);
        var contents = readHttpContentsOrNull(response.getEntity());
        var text = extractAnecdoteTextOrNull(contents);
        response.close();

        var anecdote = new RatableAnecdote(text);
        toldAnecdotes.add(anecdote);
        listenRatableAnecdote(anecdote);
        if (bannedAnecdotes.contains(anecdote))
            return super.getNextAnecdote();
        return anecdote;
    }

    private CloseableHttpResponse sendRequestAnecdote(String uri) throws IOException, ConnectTimeoutException {
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