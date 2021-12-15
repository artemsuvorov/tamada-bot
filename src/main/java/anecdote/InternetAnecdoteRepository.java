package anecdote;

import com.google.gson.GsonBuilder;
import event.ActionEvent;
import event.ActionListener;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Представляет собой репозиторий, который состоит из анекдотов
 * и который выдает их один за другим в случайной неповторяющейся последовательности.
 * Анекдоты берутся с некоторого сайта анекдотов, а также из локального хранилища.
 * Анекдоты такого репозитория поддерживают оценивание и добавление концовки.
 */
public class InternetAnecdoteRepository extends RandomRatableUnfinishedAnecdoteRepository
    implements ActionListener {

    private final String uri = "http://rzhunemogu.ru/Rand.aspx?CType=1";
    private final int requestTimeout = 1; // todo: set back to 1500

    private final CloseableHttpClient client;

    protected final long id;
    private final CommonAnecdoteList commonAnecdotes = CommonAnecdoteList.get();

    private final InternetAnecdoteRepositorySerializer serializer = new InternetAnecdoteRepositorySerializer();
    private final InternetAnecdoteRepositoryDeserializer deserializer = new InternetAnecdoteRepositoryDeserializer();

    public InternetAnecdoteRepository(long id, ArrayList<Anecdote> anecdotes, ArrayList<Anecdote> toldAnecdotes,
        ArrayList<Anecdote> bannedAnecdotes, Map<Rating, ArrayList<Anecdote>> ratedAnecdotes) {
        super(anecdotes, toldAnecdotes, bannedAnecdotes, ratedAnecdotes);
        this.id = id;
        var requestConfig = RequestConfig.custom().setConnectTimeout(requestTimeout).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        commonAnecdotes.AnecdoteAddedEvent.addListener(this);
    }

    public InternetAnecdoteRepository(long id) {
        super(AnecdotesConfiguration.deserializeAnecdotesConfig());
        this.id = id;
        var requestConfig = RequestConfig.custom().setConnectTimeout(requestTimeout).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        commonAnecdotes.AnecdoteAddedEvent.addListener(this);
    }

    /**
     * Указывает, является ли этот репозиторий пустым.
     * @return true, если репозиторий пуст, иначе false.
     */
    @Override
    public boolean hasAnecdotes() {
        return true;
    }

    /**
     * С некоторой вероятностью решает, выбрать анекдот из локального хранилища
     * или запросить анекдот запросом GET с некоторого сайта анекдотов.
     * Если анекдот выбран из локального хранилища или запрос анекдота с сайта
     * не удался или превысил время ожидания requestTimeout, то
     * возвращает случайный анекдот из набора еще не рассказанных анекдотов.
     * Когда все анекдоты локального хранилища рассказаны, принимает рассказанные
     * анекдоты как еще не рассказанные.
     * @return Случайный анекдот из локального хранилища или из интернета.
     */
    @Override
    public Anecdote getNextAnecdote() {
        try {
            if (pickNextFromInternet())
                return getAnecdoteFrom(uri);
            else
                return super.getNextAnecdote();
        } catch (Exception ex) {
            return super.getNextAnecdote();
        }
    }

    /*// todo: add javadoc
    @Override
    public void actionPerformed(ActionEvent event) {
        // todo: simplify event infrastructure
        *//*if (event instanceof AnecdoteActionEvent anecdoteEvent)
            addCommonAnecdote(anecdoteEvent.getAnecdote());
        else if (event instanceof AnecdotesActionEvent anecdotesEvent)
            addCommonAnecdotes(anecdotesEvent.getAnecdotes());*//*
    }*/

    public void pullCommonAnecdotes() {
        ArrayList<Anecdote> common = commonAnecdotes.getAnecdotes();
        for (Anecdote anecdote : common) {
            if (contains(anecdote)) continue;
            if (anecdote instanceof UnfinishedAnecdote unfinishedAnecdote && unfinishedAnecdote.hasEnding()) {
                // here, we intentionally copy the anecdote by its ref into user's repo
                anecdotes.add(unfinishedAnecdote);
                listenRatableAnecdote(unfinishedAnecdote);
            }
        }
    }

    /**
     * Сериализует этот репозиторий в Json формат в строку String и возвращает ее.
     * @return Строка String, содержащая сериализованный репозиторий анекдотов,
     * представленный в формате Json.
     */
    public String serialize() {
        var gson = new GsonBuilder()
                .registerTypeAdapter(this.getClass(), serializer)
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    /**
     * Десериализует репозиторий из указанной строки String, содержащей данные в формате Json,
     * и возвращает полученный репозиторий анекдотов InternetAnecdoteRepository.
     * @return Репозиторий анекдотов, десериализованный из Json строки String.
     */
    public InternetAnecdoteRepository deserialize(String json) {
        var gson = new GsonBuilder()
                .registerTypeAdapter(this.getClass(), deserializer)
                .create();
        return gson.fromJson(json, this.getClass());
    }

    /**
     * Указывает, брать ли следующий анекдот из интернета.
     * @return true, если следующий анекдот должен быть взят из интернета, иначе false.
     */
    private boolean pickNextFromInternet() {
        return Randomizer.getRandomNumber(2) == 0;
    }

    /**
     * Создает и отправляет GET запрос к серверу сайта по указанному URI, извлекает текст
     * анекдота из присланного HTTP ответа и возвращает полученный анекдот Anecdote.
     * @param uri URI сайта, к которому будет отправлен GET запрос анекдота.
     * @return Анекдот, извлеченный из присланного HTTP ответа.
     * @throws IOException
     * @throws ConnectTimeoutException
     */
    private Anecdote getAnecdoteFrom(String uri) throws IOException, ConnectTimeoutException {
        String text;
        try (var response = sendRequestAnecdote(uri)) {
            var contents = readHttpContentsOrNull(response.getEntity());
            text = extractAnecdoteTextOrNull(contents);
        }
        var anecdote = new RatableAnecdote(text);
        toldAnecdotes.add(anecdote);
        listenRatableAnecdote(anecdote);
        if (bannedAnecdotes.contains(anecdote))
            return super.getNextAnecdote();
        return anecdote;
    }

    /**
     * Создает и отправляет GET запрос к серверу сайта по указанному URI.
     * @param uri URI сайта, к которому будет отправлен GET запрос анекдота.
     * @return Полученный HTTP ответ.
     * @throws IOException
     * @throws ConnectTimeoutException
     */
    private CloseableHttpResponse sendRequestAnecdote(String uri) throws IOException, ConnectTimeoutException {
        var request = new HttpGet(uri);
        var response = client.execute(request);
        return response;
    }

    /**
     * Считывает полученный HTTP ответ и возвращает его содержание в виде строки String.
     * @param entity HTTP сущность, которая может быть превращена в строку String.
     * @return Содержание HTML страницы полученного HTTP ответа в виде строки String
     * или null, если преобразование HTTP ответа в строку String не удалось.
     */
    private String readHttpContentsOrNull(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Извлекает текст анекдота по определенному HTML тегу из HTML страницы HTTP ответа.
     * @param httpContents содержание HTML страницы полученного HTTP ответа.
     * @return Извлеченный текст анекдота или null, если текст извлечь не удалось.
     */
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

   /* // todo: add javadoc
    private void addCommonAnecdote(Anecdote anecdote) {
        if (anecdote instanceof UnfinishedAnecdote unfinishedAnecdote)
            if (unfinishedAnecdote.hasEnding() && id != unfinishedAnecdote.getAuthorId()) {
                var clonedAnecdote = new UnfinishedAnecdote(unfinishedAnecdote);
                anecdotes.add(clonedAnecdote);
                listenRatableAnecdote(clonedAnecdote);
            }
    }

    // todo: add javadoc
    private void addCommonAnecdotes(ArrayList<Anecdote> anecdotes) {
        for (Anecdote anecdote : anecdotes) {
            if (this.anecdotes.contains(anecdote))
                continue;
            addCommonAnecdote(anecdote);
        }
    }*/
}