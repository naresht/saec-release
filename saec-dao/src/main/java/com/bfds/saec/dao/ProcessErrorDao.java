package com.bfds.saec.dao;

import com.bfds.saec.domain.ProcessError;

/**
 * Dao for ProcessError CRUD
 */
public interface ProcessErrorDao {
    
    public void save(ProcessError error);
    
    public ProcessError read(Long id);
}
