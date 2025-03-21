package am.notebook.persistence.repository;

import am.notebook.persistence.entity.NoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoteRepository extends PagingAndSortingRepository<NoteEntity, Long>,
        CrudRepository<NoteEntity, Long> {
}
