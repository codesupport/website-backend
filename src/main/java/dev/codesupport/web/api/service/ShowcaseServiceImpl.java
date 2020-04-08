package dev.codesupport.web.api.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.api.data.entity.ContributorEntity;
import dev.codesupport.web.api.data.entity.ContributorListEntity;
import dev.codesupport.web.api.data.entity.ShowcaseEntity;
import dev.codesupport.web.api.data.repository.ContributorListRepository;
import dev.codesupport.web.api.data.repository.ContributorRepository;
import dev.codesupport.web.api.data.repository.ShowcaseRepository;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.Contributor;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        List<Showcase> showcases = crudOperations.getAll();

        populateContributorList(showcases);

        return showcases;
    }

    @Override
    public List<Showcase> findAllShowcasesByUser(Long userId) {
        List<ShowcaseEntity> showcaseEntities = showcaseRepository.findAllByUser_IdOrderById(userId);

        List<Showcase> showcases = MappingUtils.convertToType(showcaseEntities, Showcase.class);

        populateContributorList(showcases);

        return showcases;
    }

    @Override
    public Showcase getShowcaseById(Long id) {
        Showcase showcase = crudOperations.getById(id);

        populateContributorList(showcase);

        return showcase;
    }

    @Override
    public Showcase createShowcase(Showcase showcase) {
        // Create new contributor list
        ContributorListEntity savedContributorList = contributorListRepository.save(new ContributorListEntity());
        // Set new generated ID in showcase
        showcase.getContributorList().setId(savedContributorList.getId());

        // Showcases are disabled by default
        showcase.setApproved(false);

        // Create all new contributors
        List<ContributorEntity> contributors = MappingUtils.convertToType(showcase.getContributorList().getContributors(), ContributorEntity.class);
        contributors.forEach(contributor -> {
            contributor.setId(null);
            contributor.setContributorList(savedContributorList);
        });
        contributorRepository.saveAll(contributors);

        // Create showcase
        ShowcaseEntity showcaseEntity = MappingUtils.convertToType(showcase, ShowcaseEntity.class);
        showcaseEntity.setId(null);

        ShowcaseEntity savedShowcase = showcaseRepository.save(showcaseEntity);

        return MappingUtils.convertToType(savedShowcase, Showcase.class);
    }

    @Override
    public Showcase updateShowcase(Showcase showcase) {
        ShowcaseEntity updatedShowcase;
        Optional<ShowcaseEntity> oldShowcaseOptional = showcaseRepository.findById(showcase.getId());

        // Showcases are disabled by default
        showcase.setApproved(false);

        // Check if showcase exists
        if (oldShowcaseOptional.isPresent()) {
            // Delete old contributors based on old showcase contributor list id
            ShowcaseEntity oldShowcase = oldShowcaseOptional.get();
            List<ContributorEntity> oldContributors = contributorRepository.findAllByContributorList_Id(oldShowcase.getContributorList().getId());
            contributorRepository.deleteAll(oldContributors);

            // Create new contributors based on old showcase contributor list id
            ContributorListEntity contributorListEntity = MappingUtils.convertToType(oldShowcase.getContributorList(), ContributorListEntity.class);
            List<ContributorEntity> newContributors = MappingUtils.convertToType(showcase.getContributorList().getContributors(), ContributorEntity.class);
            newContributors.forEach(newContributor -> {
                newContributor.setId(null);
                newContributor.setContributorList(contributorListEntity);
            });
            contributorRepository.saveAll(newContributors);

            // Update showcase
            ShowcaseEntity showcaseEntity = MappingUtils.convertToType(showcase, ShowcaseEntity.class);
            updatedShowcase = showcaseRepository.save(showcaseEntity);
        } else {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }

        return MappingUtils.convertToType(updatedShowcase, Showcase.class);
    }

    @Override
    public VoidMethodResponse deleteShowcase(Showcase showcase) {
        int affectedEntities = 0;
        Optional<ShowcaseEntity> oldShowcaseOptional = showcaseRepository.findById(showcase.getId());

        // Check if showcase exists
        if (oldShowcaseOptional.isPresent()) {
            // Delete contributors based on existing showcase contributor list id
            ShowcaseEntity oldShowcase = oldShowcaseOptional.get();
            List<ContributorEntity> oldContributors = contributorRepository.findAllByContributorList_Id(oldShowcase.getContributorList().getId());
            contributorRepository.deleteAll(oldContributors);
            affectedEntities += oldContributors.size();

            // Delete showcase
            showcaseRepository.delete(oldShowcase);
            affectedEntities++;

            // Delete contributorList
            contributorListRepository.delete(oldShowcase.getContributorList());
            affectedEntities++;
        } else {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }
        return new VoidMethodResponse("Delete showcase", affectedEntities);
    }

    @VisibleForTesting
    void populateContributorList(List<Showcase> showcases) {
        showcases.forEach(this::populateContributorList);
    }

    @VisibleForTesting
    void populateContributorList(Showcase showcase) {
        List<ContributorEntity> contributorEntities = contributorRepository.findAllByContributorList_Id(showcase.getContributorList().getId());

        List<Contributor> contributors = MappingUtils.convertToType(contributorEntities, Contributor.class);

        showcase.getContributorList().setContributors(contributors);
    }

}
