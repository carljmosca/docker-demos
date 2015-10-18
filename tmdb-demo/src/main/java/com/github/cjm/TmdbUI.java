/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm;

import com.github.cjm.dao.UserDao;
import com.github.cjm.entity.User;
import com.github.cjm.resource.TvShow;
import com.github.cjm.resource.TvShowCollection;
import com.github.cjm.service.TvShowService;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
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
    UserDao userDao;
    @Autowired
    TvShowService tvShowService;

    private final VerticalLayout mainLayout;
    private final HorizontalLayout buttonLayout;
    private final VerticalLayout userGridLayout;
    private final HorizontalLayout movieGridLayout;
    private Button btnAddUser;
    private Button btnAddFavorite;
    private TextField username;
    private Grid userGrid;
    private Grid movieGrid;
    private BeanItemContainer<User> users;
    private BeanItemContainer<TvShow> tvShows;

    public TmdbUI() {
        mainLayout = new VerticalLayout();
        buttonLayout = new HorizontalLayout();
        userGridLayout = new VerticalLayout();
        movieGridLayout = new HorizontalLayout();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        mainLayout.setSpacing(true);

        addButtons();
        addUserGrid();
        addMovieGrid();

        mainLayout.setExpandRatio(userGridLayout, 1);
        mainLayout.setExpandRatio(movieGridLayout, 2);

        setContent(mainLayout);
    }

    private void addButtons() {
        buttonLayout.setSpacing(true);
        btnAddUser = new Button("Add User");
        btnAddFavorite = new Button("Add Favorite");
        buttonLayout.addComponent(btnAddUser);
        buttonLayout.addComponent(btnAddFavorite);
        mainLayout.addComponent(buttonLayout);
    }

    private void addUserGrid() {
        userGridLayout.setSpacing(true);
        userGridLayout.setWidth("100%");
        userGridLayout.setHeight("230px");
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.setWidth("100%");
        HorizontalLayout fieldLayout = new HorizontalLayout();
        userGrid = new Grid();
        userGrid.setWidth("100%");
        userGrid.setColumns("username");
        userGrid.appendHeaderRow();
        gridLayout.addComponent(userGrid);
        username = new TextField();
        username.setInputPrompt("username");
        fieldLayout.addComponent(username);
        // NOTE: We are not using JPAContainer which would probably be a bit nicer here; 
        // not yet had time to look at/work out Spring Boot compatibility.
        users = new BeanItemContainer<>(User.class);
        for (User user : userDao.findAll()) {
            users.addBean(user);
        }
        userGridLayout.addComponent(fieldLayout);
        userGridLayout.addComponent(gridLayout);
        userGridLayout.setExpandRatio(fieldLayout, 1);
        userGridLayout.setExpandRatio(gridLayout, 3);
        mainLayout.addComponent(userGridLayout);
    }

    private void addMovieGrid() {

        movieGridLayout.setSpacing(true);
        movieGridLayout.setWidth("100%");
        movieGrid = new Grid();
        movieGrid.setSizeFull();
        tvShows = new BeanItemContainer<>(TvShow.class);
        movieGrid.setColumns("name", "popularity", "voteAverage", "firstAirDate");
        movieGrid.setHeaderVisible(true);
        movieGrid.setContainerDataSource(tvShows);
        HeaderRow filterRow = movieGrid.appendHeaderRow();
        // Not really loading "all"; multiple pages up to some reasonable/polite limit (API is throttled and this is a demo afterall).
        tvShows.addAll(tvShowService.loadAll(TvShowService.RESOURCE_TV_POPULAR, TvShowCollection.class).getResults());
        movieGridLayout.addComponent(movieGrid);
        mainLayout.addComponent(movieGridLayout);

        movieGrid.getContainerDataSource()
                .getContainerPropertyIds().stream().forEach((pid) -> {
                    if (movieGrid.getColumn(pid) != null) {
                        Grid.HeaderCell cell = filterRow.getCell(pid);

                        TextField filterField = new TextField();
                        filterField.setColumns(8);
                        // Update filter When the filter input is changed
                        filterField.addTextChangeListener(change -> {
                            // Can't modify filters so need to replace
                            tvShows.removeContainerFilters(pid);
                            // (Re)create the filter if necessary
                            if (!change.getText().isEmpty()) {
                                tvShows.addContainerFilter(
                                        new SimpleStringFilter(pid,
                                                change.getText(), true, false));
                            }
                        });
                        filterField.setImmediate(true);
                        cell.setComponent(filterField);

                    }
                });

    }
}
