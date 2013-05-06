package com.bfds.saec.security.pojo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

/*
* Get and set properties used by the LDAP server
*/
public class Property {
    private String url;
    private String user;
    private String password;
    private String filter;
    private String searchbase;

    public void usage() throws FileNotFoundException {
        System.out.println("\nIncorrect program arguments!\n");
        throw new FileNotFoundException("Please include property file name..\n" +
                "Example: Javac Property ldap.properties\n");
    }

    /*
    * Get properties used to set Ldap server
    * @param 	filename for ldap server properties
    */
    public Hashtable getProperties(String filename)
            throws FileNotFoundException, IOException {
        if (filename.trim().length() == 0) usage();

        Properties props = new Properties();

        InputStream is = getClass().getResourceAsStream(filename);

        if (null == is) {
            throw new IOException("Property: can't get resource " + filename);
        }
        props.load(is);
        is.close();

        url = props.getProperty("ldap.url");
        user = props.getProperty("ldap.user");
        password = props.getProperty("ldap.password");
        filter = props.getProperty("ldap.filter");
        searchbase = props.getProperty("ldap.searchbase");

        Hashtable hash = new Hashtable();
        hash.put("SECURITY_PRINCIPAL", user);
        hash.put("SECURITY_CREDENTIALS", password);
        hash.put("PROV_URL", url);
        hash.put("FILTER", filter);
        return hash;
    }


    public String getSearchbase() {
        return searchbase;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getFilter() {
        return filter;
    }
   
}
