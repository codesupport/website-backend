package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ShowcaseService {

    List<Showcase> findAllShowcases();

    List<Showcase> findAllShowcasesByUser(Long userId);

    List<Showcase> findAllShowcasesByAlias(String alias);

    Showcase getShowcaseById(Long id);

    @PreAuthorize("hasPermission(#showcase, 'create')")
    Showcase createShowcase(Showcase showcase);

    @PreAuthorize("hasPermission(#showcase, 'update')")
    Showcase updateShowcase(Showcase showcase);

    @PreAuthorize("hasPermission(#showcase, 'delete')")
    VoidMethodResponse deleteShowcase(Showcase showcase);

}
