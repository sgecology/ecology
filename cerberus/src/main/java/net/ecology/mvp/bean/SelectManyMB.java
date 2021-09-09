package net.ecology.mvp.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.ecology.mvp.model.Theme;
import net.ecology.mvp.service.ThemeService;

@ViewScoped
@Named
public class SelectManyMB implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8839206890355624871L;
		private List<String> selectedOptions;
    private List<Theme> selectedThemes;
    private List<Theme> themes;

    @Inject
    private ThemeService service;

    @PostConstruct
    public void init() {
        themes = service.getThemes();
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setService(ThemeService service) {
        this.service = service;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public List<Theme> getSelectedThemes() {
        return selectedThemes;
    }

    public void setSelectedThemes(List<Theme> selectedThemes) {
        this.selectedThemes = selectedThemes;
    }
}

