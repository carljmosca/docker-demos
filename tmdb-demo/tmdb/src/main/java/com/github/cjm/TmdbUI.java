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
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
    private final HorizontalLayout userSectionLayout;
    private final HorizontalLayout tvShowGridLayout;
    private ComboBox username;
    private ListSelect userFavorites;
    private Grid tvShowGrid;
    private BeanItemContainer<User> users;
    private BeanItemContainer<TvShow> tvShows;
    private BeanItemContainer<TvShow> favorites;

    public TmdbUI() {
        mainLayout = new VerticalLayout();
        buttonLayout = new HorizontalLayout();
        userSectionLayout = new HorizontalLayout();
        tvShowGridLayout = new HorizontalLayout();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        mainLayout.setSpacing(true);
        addButtons();
        addUserGrid();
        addTvShowGrid();
        mainLayout.setExpandRatio(tvShowGridLayout, 2);
        setContent(mainLayout);
    }

    private void addButtons() {
        buttonLayout.setSpacing(true);
        mainLayout.addComponent(buttonLayout);
    }

    private void addUserGrid() {
        userSectionLayout.setSpacing(true);
        userSectionLayout.setWidth("100%");
        username = new ComboBox();
        username.setInputPrompt("name");
        username.setNewItemsAllowed(true);
        username.setTextInputAllowed(true);
        username.addValueChangeListener((Property.ValueChangeEvent event) -> {
            userFavorites.removeAllItems();
            if (username.getValue() != null) {
                User u = (User) username.getValue();
                if (u.getFavoriteTvShows() != null) {
                    for (Integer id : u.getFavoriteTvShows()) {
                        userFavorites.addItem(getTvShow(id));
                    }
                }
            }
        });
        username.setNewItemHandler((String newItemCaption) -> {
            if (newItemCaption != null) {
                User u = findUser(newItemCaption);
                users.addBean(u);
                username.setValue(u);
            }
        });
        users = new BeanItemContainer<>(User.class);
        for (User user : userDao.findAll()) {
            users.addBean(user);
        }
        username.setContainerDataSource(users);
        username.setImmediate(true);
        username.setItemCaptionPropertyId("username");
        userSectionLayout.addComponent(username);
        userFavorites = new ListSelect("Favorites");
        userFavorites.setWidth("70%");
        favorites = new BeanItemContainer<>(TvShow.class);
        userFavorites.setContainerDataSource(favorites);
        userFavorites.setItemCaptionPropertyId("name");
        userSectionLayout.addComponent(userFavorites);
        userSectionLayout.setExpandRatio(username, 1.0f);
        userSectionLayout.setExpandRatio(userFavorites, 2.0f);
        mainLayout.addComponent(userSectionLayout);

    }

    private void addTvShowGrid() {

        tvShowGridLayout.setSpacing(true);
        tvShowGridLayout.setWidth("100%");
        tvShowGrid = new Grid();
        tvShowGrid.setSizeFull();
        tvShows = new BeanItemContainer<>(TvShow.class);
        tvShowGrid.setColumns("name", "popularity", "voteAverage", "firstAirDate");
        tvShowGrid.setHeaderVisible(true);
        tvShowGrid.setContainerDataSource(tvShows);
        HeaderRow filterRow = tvShowGrid.appendHeaderRow();
        // Not really loading "all"; multiple pages up to some reasonable/polite limit 
        // (API is throttled and this is a demo afterall).
        tvShows.addAll(tvShowService.loadAll(TvShowService.RESOURCE_TV_POPULAR, TvShowCollection.class).getResults());
        tvShowGridLayout.addComponent(tvShowGrid);
        mainLayout.addComponent(tvShowGridLayout);

        tvShowGrid.getContainerDataSource()
                .getContainerPropertyIds().stream().forEach((pid) -> {
                    if (tvShowGrid.getColumn(pid) != null) {
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateRenderer dateRenderer = new DateRenderer(dateFormat);
        tvShowGrid.getColumn("firstAirDate").setRenderer(dateRenderer);

        tvShowGrid.addItemClickListener((ItemClickEvent event) -> {
            if (event.isDoubleClick()) {
                User u = (User) username.getValue();
                if (u != null) {
                    users.removeItem(u);
                    TvShow tvShow = getSelectedTvShow();
                    if (tvShow != null) {
                        favorites.addBean(tvShow);
                        List<Integer> i = new ArrayList<>();
                        favorites.getItemIds().stream().forEach((t) -> {
                            i.add(t.getId());
                        });
                        u.setFavoriteTvShows(new Integer[i.size()]);
                        u.setFavoriteTvShows(i.toArray(u.getFavoriteTvShows()));
                        userDao.save(u);
                        users.addBean(u);
                    }
                }
            }
        });

    }

    private TvShow getSelectedTvShow() {
        Object selected;
        if (tvShowGrid.getSelectedRows().isEmpty()) {
            return null;
        } else {
            selected = tvShowGrid.getSelectedRows().toArray()[0];
        }
        TvShow tvShow = null;
        if (selected != null) {
            tvShow = tvShows.getItem(selected).getBean();
        }
        return tvShow;
    }

    private TvShow getTvShow(int id) {
        for (TvShow t : tvShows.getItemIds()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    private User findUser(String username) {
        List<User> userList = userDao.findByUsername(username);
        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            User u = new User();
            u.setUsername(username);
            userDao.save(u);
            return u;
        }
    }
}
