package com.robertsanek.ankigen;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.robertsanek.data.etl.remote.lastfm.Artist;
import com.robertsanek.data.etl.remote.lastfm.ArtistEtl;

public class ArtistGenerator extends BaseGenerator {

  private static List<String> IGNORABLE_PEOPLE = Lists.newArrayList(
      "Deeb",
      "baaskaT",
      "JSan",
      "Above Beyond"
  );
  private static final long MINIMUM_PLAY_COUNT = 100;

  @Override
  public List<PersonNote> getPersons() throws Exception {
    return getArtists().stream()
        .filter(artist -> !artist.isFoundInAnki())
        .filter(artist -> !IGNORABLE_PEOPLE.contains(artist.getName()))
        .filter(artist -> artist.getPlayCount() >= MINIMUM_PLAY_COUNT)
        .map(artist -> PersonNote.PersonNoteBuilder.aPersonNote()
            .withName(artist.getName())
            .withImage(Lists.newArrayList(URI.create(artist.getImageUrl())))
            .withContext("artist")
            .withSource(getSource())
            .build())
        .collect(Collectors.toList());
  }

  public List<Artist> getArtists() throws Exception {
    return new ArtistEtl().getObjects();
  }

  public String getSource() {
    return "Generated by core.ArtistGenerator on " + ZonedDateTime.now();
  }

}
