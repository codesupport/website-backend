package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ShowcaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowcaseRepository extends JpaRepository<ShowcaseEntity, Long> {

    List<ShowcaseEntity> findAllByUser_IdOrderById(Long userId);

}
