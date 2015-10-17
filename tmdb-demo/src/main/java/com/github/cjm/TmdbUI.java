/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm;

import com.github.cjm.resource.TvShow;
import com.github.cjm.resource.TvShowCollection;
import com.github.cjm.service.TvShowService;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author moscac
 */
@SpringUI
@Theme("valo")
public class TmdbUI extends UI {

    @Autowired
    TvShowService tvShowService;

    private final VerticalLayout mainLayout;
    private final HorizontalLayout buttonLayout;
    private final HorizontalLayout gridLayout;
    private Button btnRefresh;
    private Grid grid;
    private BeanItemContainer<TvShow> tvShows;

    public TmdbUI() {
        mainLayout = new VerticalLayout();
        buttonLayout = new HorizontalLayout();
        gridLayout = new HorizontalLayout();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        mainLayout.setSpacing(true);
        buttonLayout.setSpacing(true);
        btnRefresh = new Button("Refresh");
        buttonLayout.addComponent(btnRefresh);
        mainLayout.addComponent(buttonLayout);

        addGrid();

        setContent(mainLayout);
    }

    private void addGrid() {

        gridLayout.setSpacing(true);
        gridLayout.setWidth("100%");
        grid = new Grid();
        grid.setSizeFull();
        tvShows = new BeanItemContainer<>(TvShow.class);
        grid.setColumns("name", "popularity", "voteAverage", "firstAirDate");
        grid.setHeaderVisible(true);
        grid.setContainerDataSource(tvShows);
        grid.appendHeaderRow();
        tvShows.addAll(tvShowService.loadAll(TvShowService.RESOURCE_TV_POPULAR, TvShowCollection.class).getResults());
        gridLayout.addComponent(grid);
        mainLayout.addComponent(gridLayout);

    }
}
