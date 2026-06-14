package de.ghostnet.web;

import de.ghostnet.model.GhostNet;
import de.ghostnet.service.GhostNetService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;

@Named
@RequestScoped
public class ReportGhostNetBean {

    @Inject
    private GhostNetService ghostNetService;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal estimatedSizeM2;

    private String reporterName;
    private String reporterPhoneNumber;

    public String save() {
        boolean onlyOneReporterFieldFilled =
                (reporterName != null && !reporterName.isBlank()
                        && (reporterPhoneNumber == null || reporterPhoneNumber.isBlank()))
                || (reporterPhoneNumber != null && !reporterPhoneNumber.isBlank()
                        && (reporterName == null || reporterName.isBlank()));

        if (onlyOneReporterFieldFilled) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Bitte Name und Telefonnummer angeben oder beide Felder leer lassen.",
                            null
                    )
            );
            return null;
        }

        GhostNet ghostNet = new GhostNet();
        ghostNet.setLatitude(latitude);
        ghostNet.setLongitude(longitude);
        ghostNet.setEstimatedSizeM2(estimatedSizeM2);

        ghostNetService.reportGhostNet(ghostNet, reporterName, reporterPhoneNumber);

        return "success.xhtml?faces-redirect=true";
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getEstimatedSizeM2() {
        return estimatedSizeM2;
    }

    public void setEstimatedSizeM2(BigDecimal estimatedSizeM2) {
        this.estimatedSizeM2 = estimatedSizeM2;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterPhoneNumber() {
        return reporterPhoneNumber;
    }

    public void setReporterPhoneNumber(String reporterPhoneNumber) {
        this.reporterPhoneNumber = reporterPhoneNumber;
    }
    
}