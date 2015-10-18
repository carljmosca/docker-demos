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
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
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
    private Button btnRefresh;
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
        buttonLayout.setSpacing(true);
        btnRefresh = new Button("Refresh");
        buttonLayout.addComponent(btnRefresh);
        mainLayout.addComponent(buttonLayout);

        addUserGrid();
        addMovieGrid();

        setContent(mainLayout);
    }
    
    private void addUserGrid() {
        userGridLayout.setSpacing(true);
        userGridLayout.setWidth("100%");
        userGridLayout.setHeight("25%");
        HorizontalLayout gridLayout = new HorizontalLayout();
        HorizontalLayout fieldLayout = new HorizontalLayout();
        userGrid = new Grid();
        userGrid.setSizeFull();
        gridLayout.addComponent(userGrid);
        username = new TextField();
        fieldLayout.addComponent(username);
        users = new BeanItemContainer<>(User.class);
        for (User user : userDao.findAll()) {
            users.addBean(user);
        }
        userGridLayout.addComponent(gridLayout);        
        userGridLayout.addComponent(fieldLayout);
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
        movieGrid.appendHeaderRow();
        tvShows.addAll(tvShowService.loadAll(TvShowService.RESOURCE_TV_POPULAR, TvShowCollection.class).getResults());
        movieGridLayout.addComponent(movieGrid);
        mainLayout.addComponent(movieGridLayout);

    }
}
