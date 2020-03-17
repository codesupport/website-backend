package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.ContributorEntity;
import dev.codesupport.web.api.data.entity.ContributorListEntity;
import dev.codesupport.web.api.data.entity.ShowcaseEntity;
import dev.codesupport.web.api.data.repository.ContributorListRepository;
import dev.codesupport.web.api.data.repository.ContributorRepository;
import dev.codesupport.web.api.data.repository.ShowcaseRepository;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowcaseServiceImpl implements ShowcaseService {

    private final ContributorListRepository contributorListRepository;
    private final ContributorRepository contributorRepository;
    private final ShowcaseRepository showcaseRepository;
    private final CrudOperations<ShowcaseEntity, Showcase, Long> crudOperations;

    @Autowired
    public ShowcaseServiceImpl(
            ShowcaseRepository showcaseRepository,
            ContributorRepository contributorRepository,
            ContributorListRepository contributorListRepository
    ) {
        this.showcaseRepository = showcaseRepository;
        this.contributorRepository = contributorRepository;
        this.contributorListRepository = contributorListRepository;
        crudOperations = new CrudOperations<>(showcaseRepository, ShowcaseEntity.class, Showcase.class);
    }

    @Override
    public List<Showcase> findAllShowcases() {
        return crudOperations.getAll();
    }

    @Override
    public Showcase getShowcaseById(Long id) {
        return crudOperations.getById(id);
    }

    @Override
    public Showcase createShowcase(Showcase showcase) {
        ContributorListEntity savedContributorList = contributorListRepository.save(new ContributorListEntity());
        showcase.getContributorList().setId(savedContributorList.getId());

        List<ContributorEntity> contributors = MappingUtils.convertToType(showcase.getContributorList().getContributors(), ContributorEntity.class);
        contributors.forEach(contributor -> contributor.setContributorList(savedContributorList));
        contributorRepository.saveAll(contributors);

        ShowcaseEntity showcaseEntity = MappingUtils.convertToType(showcase, ShowcaseEntity.class);

        ShowcaseEntity savedShowcase = showcaseRepository.save(showcaseEntity);

        return MappingUtils.convertToType(savedShowcase, Showcase.class);
    }

    @Override
    public Showcase updateShowcase(Showcase showcase) {
        return crudOperations.updateEntity(showcase);
    }

    @Override
    public VoidMethodResponse deleteShowcase(Showcase showcase) {
        int affectedEntities = crudOperations.deleteEntity(showcase);

        return new VoidMethodResponse("Delete showcase", affectedEntities);
    }

}
