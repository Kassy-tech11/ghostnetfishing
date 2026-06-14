package de.ghostnet.web;

import de.ghostnet.model.GhostNet;
import de.ghostnet.service.GhostNetService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class OpenGhostNetsBean {

    @Inject
    private GhostNetService ghostNetService;

    private List<GhostNet> openGhostNets;

    @PostConstruct
    public void init() {
        openGhostNets = ghostNetService.findOpenGhostNets();
    }

    public List<GhostNet> getOpenGhostNets() {
        return openGhostNets;
    }

    public boolean isEmpty() {
        return openGhostNets == null || openGhostNets.isEmpty();
    }
}