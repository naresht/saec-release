package com.bfds.saec.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

/*
* Get and set properties used by the LDAP server
*/
public class CommonProperties {
	
	static final private Logger logger = LoggerFactory.getLogger(CommonProperties.class);
	
    private static String ndmshare ;
    



    /*
    * Get properties used to set Ldap server
    * @param 	filename for ldap server properties
    */
    public Hashtable loadProperties()
            throws FileNotFoundException, IOException {
       
        
        final String filename = "/META-INF/spring/common.properties" ;

        Properties props = new Properties();       
        
        ClassPathResource res = new ClassPathResource(filename);

        if (res == null) {
            throw new IOException("Property: can't get resource " + filename);
        }
        // props.load(is);
        props.load(res.getInputStream());
        res.getInputStream().close();

        Hashtable hash = new Hashtable();
        ndmshare = props.getProperty("ndm.server.path");

        
        if (ndmshare == null)
        {
        	logger.error("ndm property is null in common.properties"); ;
        }
        
        return hash;
    }

	public static String getNdmshare() {
		return ndmshare;
	}

	public static void setNdmshare(String ndmshare) {
		CommonProperties.ndmshare = ndmshare;
	}
}
