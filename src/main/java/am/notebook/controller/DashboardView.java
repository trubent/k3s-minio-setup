package am.notebook.controller;

import java.io.Serializable;
import java.time.LocalDateTime;

import am.notebook.persistence.entity.NoteEntity;
import am.notebook.service.NoteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.dashboard.DashboardModel;
import org.primefaces.model.dashboard.DashboardWidget;
import org.primefaces.model.dashboard.DefaultDashboardModel;
import org.primefaces.model.dashboard.DefaultDashboardWidget;
import org.springframework.beans.factory.annotation.Autowired;

@Named
@ViewScoped
public class DashboardView implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String RESPONSIVE_CLASS = "col-12 lg:col-6 xl:col-6";
    
    @Autowired
    private transient NoteService noteService;

    private DashboardModel responsiveModel;
    @Getter
    @Setter
    private String note;
    @Getter
    @Setter
    private LazyDataModel<NoteEntity> notes;
    
    public void save(){
        noteService.save(new NoteEntity(null, note, LocalDateTime.now()));
        note = null;
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Success");
        message.setDetail("Note Saved");
        addMessage(message);
    }

    @PostConstruct
    public void init() {
        // responsive
        responsiveModel = new DefaultDashboardModel();
        responsiveModel.addWidget(new DefaultDashboardWidget("bar", RESPONSIVE_CLASS));
        responsiveModel.addWidget(new DefaultDashboardWidget("stacked", RESPONSIVE_CLASS));

        notes = new LazyNoteDataModel(noteService);
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        String result = String.format("Dragged index: %d, Dropped Index: %d, Widget Index: %d",
                event.getSenderColumnIndex(),  event.getColumnIndex(), event.getItemIndex());
        message.setDetail(result);

        addMessage(message);
    }

    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Panel Closed",
                "Closed panel ID:'" + event.getComponent().getId() + "'");

        addMessage(message);
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Toggled",
                "Toggle panel ID:'" + event.getComponent().getId() + "' Status:" + event.getVisibility().name());

        addMessage(message);
    }

    /**
     * Dashboard panel has been resized.
     *
     * @param widget the DashboardPanel
     * @param size the new size CSS
     */
    public void onDashboardResize(final String widget, final String size) {
        final DashboardWidget dashboard = responsiveModel.getWidget(widget);
        if (dashboard != null) {
            final String newCss = dashboard.getStyleClass().replaceFirst("xl:col-\\d+", size);
            dashboard.setStyleClass(newCss);
        }
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public DashboardModel getResponsiveModel() {
        return responsiveModel;
    }
}