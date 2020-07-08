package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.ShowcaseService;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowcaseControllerImpl implements ShowcaseController {

    private final ShowcaseService service;

    @Autowired
    public ShowcaseControllerImpl(ShowcaseService service) {
        this.service = service;
    }

    @Override
    public List<Showcase> findAllShowcases() {
        return service.findAllShowcases();
    }

    @Override
    public List<Showcase> findAllShowcasesByUser(Long userid) {
        return service.findAllShowcasesByUser(userid);
    }

    @Override
    public List<Showcase> findAllShowcasesByAlias(String alias) {
        return service.findAllShowcasesByAlias(alias);
    }

    @Override
    public Showcase getShowcaseById(Long id) {
        return service.getShowcaseById(id);
    }

    @Override
    public Showcase createShowcase(Showcase showcase) {
        return service.createShowcase(showcase);
    }

    @Override
    public Showcase updateShowcase(Showcase showcase) {
        return service.updateShowcase(showcase);
    }

    @Override
    public VoidMethodResponse deleteShowcase(Showcase showcase) {
        return service.deleteShowcase(showcase);
    }

}
