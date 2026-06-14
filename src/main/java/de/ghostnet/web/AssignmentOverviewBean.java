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
public class AssignmentOverviewBean {

    @Inject
    private GhostNetService ghostNetService;

    private List<GhostNet> assignedGhostNets;

    @PostConstruct
    public void init() {
        assignedGhostNets = ghostNetService.findAssignedGhostNets();
    }

    public List<GhostNet> getAssignedGhostNets() {
        return assignedGhostNets;
    }
}