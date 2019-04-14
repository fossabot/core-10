package com.robertsanek.data.etl.local.sqllite.anki;

import com.google.common.annotations.VisibleForTesting;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class AnkiConnectUtils {

  private static String ANKI_CONNECT_HOST = "localhost";
  private static int ANKI_CONNECT_PORT = 8765;
  private static String ANK_CONNECT_PRETTY_URL = String.format("%s:%d", ANKI_CONNECT_HOST, ANKI_CONNECT_PORT);
  private static final URIBuilder ANKI_CONNECT_BASE_URI = new URIBuilder()
      .setScheme("http")
      .setHost(ANK_CONNECT_PRETTY_URL);
  private static final int ANKI_CONNECT_VERSION = 6;
  private static Log log = Logs.getLog(AnkiConnectUtils.class);

  public static boolean loadProfile(String profileToLoad) {
    openAnki();
    HttpPost changeProfilePost = new HttpPost(getUri());
    changeProfilePost.setEntity(new ByteArrayEntity(
        String.format("{\"action\": \"loadProfile\", \"params\": {\"name\": \"%s\"}, \"version\": %s}",
            profileToLoad, ANKI_CONNECT_VERSION).getBytes(StandardCharsets.UTF_8)));
    log.info("Loading profile '%s'...", profileToLoad);
    String profileResponse = Unchecked.get(() -> EntityUtils.toString(
        CommonProvider.getHttpClient().execute(changeProfilePost).getEntity()));
    if (!profileResponse.contains("true")) {
      log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", profileResponse);
      return false;
    } else {
      log.info("Successfully loaded profile '%s'.", profileToLoad);
      return true;
    }
  }

  public static boolean triggerSync() {
    openAnki();
    HttpPost syncPost = new HttpPost(getUri());
    syncPost.setEntity(new ByteArrayEntity(
        String.format("{\"action\": \"sync\", \"version\": %s}", ANKI_CONNECT_VERSION)
            .getBytes(StandardCharsets.UTF_8)));
    log.info("Syncing current profile...");
    String syncResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(syncPost).getEntity()));
    if (!syncResponse.equals("{\"result\": null, \"error\": null}")) {
      log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", syncResponse);
      return false;
    } else {
      log.info("Successfully synced current profile.");
      return true;
    }
  }

  private static void openAnki() {
    if (getAnkiExecutablePath().isPresent()) {
      File ankiExecutable = new File(getAnkiExecutablePath().orElseThrow());
      log.info("Opening Anki...");
      try {
        Desktop.getDesktop().open(ankiExecutable);
        Thread.sleep(Duration.ofSeconds(5).get(ChronoUnit.MILLIS));
      } catch (IOException | InterruptedException ignored) {
      }
    } else {
      log.info("Anki application path was not provided so application could not be started. " +
          "Assuming Anki is open and attempting to connect anyway....");
    }
  }

  private static URI getUri() {
    return Unchecked.get(ANKI_CONNECT_BASE_URI::build);
  }

  @VisibleForTesting
  static Optional<String> getAnkiExecutablePath() {
    return CrossPlatformUtils.getPlatform().getAnkiExecutable().map(Path::toString);
  }

}
