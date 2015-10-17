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
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 *
 * @author moscac
 * @param <T>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class ResourceCollection<T> implements Serializable {
    
    @JsonProperty("page")
    private int page;
    @JsonProperty("results")
    private Collection<T> results;
    
    public ResourceCollection() {
        //results = new ArrayList<>();
    }
}
