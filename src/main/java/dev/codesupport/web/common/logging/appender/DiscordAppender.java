package dev.codesupport.web.common.logging.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.google.common.annotations.VisibleForTesting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class DiscordAppender extends AppenderBase<ILoggingEvent> {

    private final OkHttpClient client = new OkHttpClient();

    //Temporary solution
    private String lastMessage;

    private String webhookUrl;
    private String username;
    private String avatarUrl;

    private static final Map<Integer, Color> levelColorMap;
    static {
        levelColorMap = new HashMap<>();
        levelColorMap.put(Level.TRACE_INTEGER, Color.LIGHT_GRAY);
        levelColorMap.put(Level.DEBUG_INTEGER, Color.BLUE);
        levelColorMap.put(Level.INFO_INTEGER, Color.GREEN);
        levelColorMap.put(Level.WARN_INTEGER, Color.YELLOW);
        levelColorMap.put(Level.ERROR_INTEGER, Color.RED);
    }

    @Override
    protected void append(ILoggingEvent event) {
        updateMessage(event);

        String messageData = event.getLevel().toString() + event.getMessage() + event.getFormattedMessage();
        if (!messageData.equalsIgnoreCase(lastMessage)) {
            //Prevent duplicate posts
            lastMessage = messageData;
            String description = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeStamp()), ZoneId.systemDefault()).toString();
            String embed = new EmbedBuilder()
                    .setColor(levelColorMap.get(event.getLevel().toInteger()))
                    .setAuthor(username, null, avatarUrl)
                    .setTitle(event.getLevel().toString())
                    .setDescription(description)
                    .addField(
                            event.getMessage(),
                            event.getFormattedMessage(),
                            false
                    )
                    .build()
                    .toData()
                    .toString();

            embed = "{\"embeds\":[" + embed + "]}";
            post(embed);
        } else {
            lastMessage = null;
        }
    }

    @VisibleForTesting
    void updateMessage(final ILoggingEvent event) {
        Optional<IThrowableProxy> optional = findException(event);

        if (optional.isPresent()) {
            String message;
            String formattedMessage = optional.get().getMessage();
            Object throwable = ReflectionTestUtils.getField(optional.get(), "throwable");
            if (throwable != null) {
                message = throwable.getClass().getSimpleName();
            } else {
                message = "";
            }
            // Shouldn't do this, but I did, three times.
            ReflectionTestUtils.setField(event, "message", message);
            ReflectionTestUtils.setField(event, "formattedMessage", formattedMessage);
            ReflectionTestUtils.setField(event, "throwableProxy", null);
        }
    }

    @VisibleForTesting
    Optional<IThrowableProxy> findException(final ILoggingEvent event) {
        IThrowableProxy exception = event.getThrowableProxy();

        while (exception != null && exception.getCause() != null) {
            exception = exception.getCause();
        }

        return exception == null ? Optional.empty() : Optional.of(exception);
    }

    @VisibleForTesting
    void post(String json) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() > 300) {
                if (response.code() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                    //TODO: Add to queue to try again later
                } else {
                    //Cry?
                }
            }
        } catch (IOException e) {
            // well... crap.
        }
    }

}
