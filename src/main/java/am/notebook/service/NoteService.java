package am.notebook.service;

import am.notebook.persistence.entity.NoteEntity;
import am.notebook.persistence.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    
    @Transactional
    public void save(NoteEntity noteEntity) {
        log.info("Saving note: {}", noteEntity.getNote());
        noteRepository.save(noteEntity);
    }
    
    public Page<NoteEntity> search(Pageable page){
        return noteRepository.findAll(page);
    }
    
    public NoteEntity findById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }
    
}
