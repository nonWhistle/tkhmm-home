package com.tkhmm.application.views.cvview;

import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

public class CvViewData {

    private static final Logger log = Logger.getLogger(CvViewData.class.getSimpleName());

    /*
    The joke that will be displayed inside Cv View Rest section one
     */
    @Getter @Setter
    private String aJoke;

    /*
    The message to be displayed in Cv view Rest section two
     */
    @Getter @Setter
    private String aMessage;
}
