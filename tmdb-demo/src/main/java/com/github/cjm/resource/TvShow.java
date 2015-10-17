/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 *
 * @author moscac
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class TvShow implements Serializable {

    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("first_air_date")
    private Date firstAirDate;
    @JsonProperty("genre_ids")
    private Integer[] genreIds;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_name")
    private String originalName;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("origin_country")
    private String[] originCountry;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("popularity")
    private BigDecimal popularity;
    @JsonProperty("name")
    private String name;
    @JsonProperty("vote_average")
    private BigDecimal voteAverage;
    @JsonProperty("vote_count")
    private Integer voteCount;
    
    public TvShow() {
        
    }
    
}
