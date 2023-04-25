package com.tkhmm.application.views.cvview;

import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

public class CvViewData {

    private static final Logger log = Logger.getLogger(CvViewData.class.getSimpleName());

    /*
    The joke that will be displayed inside Cv View
     */
    @Getter @Setter
    private String aJoke;
}
