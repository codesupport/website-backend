package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;

import java.util.List;

public interface ShowcaseService {

    List<Showcase> findAllShowcases();

    Showcase getShowcaseById(Long id);

    Showcase createShowcase(Showcase showcase);

    Showcase updateShowcase(Showcase showcase);

    VoidMethodResponse deleteShowcase(Showcase showcase);

}
