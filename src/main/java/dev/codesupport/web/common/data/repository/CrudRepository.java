package dev.codesupport.web.common.data.repository;

import dev.codesupport.web.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrudRepository<T, I> extends JpaRepository<T, I> {

    default T getById(I id) {
        Optional<T> entity = findById(id);

        if (!entity.isPresent()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }

        return entity.get();
    }

}
