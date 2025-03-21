package am.notebook.controller;

import java.util.List;
import java.util.Map;

import am.notebook.persistence.entity.NoteEntity;
import am.notebook.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class LazyNoteDataModel extends LazyDataModel<NoteEntity> {

    private static final long serialVersionUID = 1L;

    private final NoteService noteService;

    @Override
    public NoteEntity getRowData(String rowKey) {
        return noteService.findById(Long.valueOf(rowKey));
    }

    @Override
    public String getRowKey(NoteEntity note) {
        return String.valueOf(note.getId());
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return 0;
    }

    @Override
    public List<NoteEntity> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        Pageable pageable;
        if(sortBy != null && !sortBy.isEmpty()){
            Map.Entry<String, SortMeta> entry = sortBy.entrySet().iterator().next();
            Sort sort = Sort.by(SortOrder.ASCENDING.equals(entry.getValue().getOrder()) ? Sort.Direction.ASC : Sort.Direction.DESC, entry.getKey());
            pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        } else {
            pageable = PageRequest.of(offset / pageSize, pageSize);
        }
        var data = noteService.search(pageable);
        setRowCount((int) data.getTotalElements());
        recalculateFirst(offset, pageSize, data.getNumberOfElements());
        return data.toList();
    }


}