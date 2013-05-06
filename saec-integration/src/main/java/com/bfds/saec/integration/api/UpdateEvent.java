package com.bfds.saec.integration.api;

import java.io.Serializable;

/**
 * A generic application event, representing a change to some domain level data on which 
 * we may be listening to trigger various processes.
 * 
 * All classes that implement this interface should be serializable, as they will be 
 * candidates for persistence.
 */
public interface UpdateEvent extends Serializable {

}
