package com.hradecek.ticket.cdi;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class FacesContextProducer {

    @Produces
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
