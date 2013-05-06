package com.bfds.saec.security.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Simple non-Spring standalone LDAP Authenticator
 * 
 * <p>
 * Universal getGroups make extensive use of global categorg(GC) on
 * Active Directory. It can contain members from any Windows 2000
 * domain in the forest. This class accepts a user id and retrieves
 * all groups that they are a member of.
 * 
 * <p/>
 * Usage:
 * LDAPAuthenticator auth = new LDAPAuthenticator("userid");
 * auth.isUserInfo("groupname");
 * </p>
 * <p>
 *     
 * Note: The prior version provided information about a user
 * specified in the constructor. It used the Java Native Interface
 * to call code in USERINFO.DLL to query the native system
 * (Windows NT). USERINFO.DLL had to be present in order for
 * it to work. Any DLLs that USERIFNO.DLL required had to also
 * be present. (Currently: ACTIVEDS.DLL and ADSIID.DLL)
 * <p/>
 * 
 * The original class was created by  W.J.McMahon
 */

@Named
public class LDAPAuthenticatorImpl implements LDAPAuthenticator {

    static final private Logger logger = LoggerFactory.getLogger(LDAPAuthenticatorImpl.class);

    String init_cont_fac = "com.sun.jndi.ldap.LdapCtxFactory";
    String sec_auth = "simple";
    String searchbase; //="DC=bostonfinancial, DC=biz";

    private String prov_url;
    private String sec_prin;
    private String sec_cred;
    private String filter;
    private String groups[];
    private String user_id;
    private String domain;

    // The UserInfo attribute will give you the groups.
    private static String attrs_arr[] = {"memberOf"};

    public LDAPAuthenticatorImpl() {
        
    }
    
    /**
     * Constructor, gather information about an already authenticated
     * user.Internet Explorer will automatically authenticate a Windows
     * NT user and put the user ID in CGI variable "AUTH_USER", if and
     * only if the web server (IIS) is configured to prevent anonymous
     * log in.
     *
     * @param authUser The authenticated user id, retrieved from
     *                  "AUTH_USER".
     */
    public LDAPAuthenticatorImpl(String authUser) {
        int delimiterPos = authUser.lastIndexOf("/");
        if (delimiterPos < 0) {
            delimiterPos = authUser.lastIndexOf("\\");
        }
        if (delimiterPos < 0) {
            domain = "";
            user_id = authUser;
        } else {
            domain = authUser.substring(0, delimiterPos);
            user_id = authUser.substring(delimiterPos + 1);
        }

        getProp(domain, user_id); // get ldap properties
    }

    public String getUserId() {
        return user_id;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * Gets the properties specified in the file named ldapserver.properties
     */
    private void getProp(String domain, String userid) {
        String filename = "ldap.properties";

        if ((domain != null) && domain.length() > 0) {
            filename = domain.toLowerCase() + "_" + filename;
        }
        try {
            Property p = new Property();

            p.getProperties(filename);

            sec_prin = p.getUser();
            sec_cred = p.getPassword();
            prov_url = p.getUrl();
            filter = p.getFilter();
            filter = filter.replaceFirst("XX", userid);
            searchbase = p.getSearchbase();

        } catch (FileNotFoundException e) {
            logger.error("Error," + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error," + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates the Initial Context to perform
     * directory operations
     *
     * @returns a reference to a DirContext object
     */
    private DirContext getContext()
            throws NamingException, IOException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, init_cont_fac);
        env.put(Context.PROVIDER_URL, prov_url);
        env.put(Context.SECURITY_AUTHENTICATION, sec_auth);
        env.put(Context.SECURITY_PRINCIPAL, sec_prin);
        env.put(Context.SECURITY_CREDENTIALS, sec_cred);
        DirContext ctx = null;
        ctx = new InitialDirContext(env);
        // DirContext ctx = (DirContext) initial.lookup(prov_url);
        return ctx;
    }

    /**
     * Core Authenticate function
     * The filter to search by:
     * objectcategory=person and sAMAccountName='userid'
     */
    @Override
    public void authenticate() {
        String[] grps = new String[100];

        int namecount = 0;
        int groupcount = 0;

        try {
            DirContext ctx = getContext();
            SearchControls ctrls = new SearchControls();
            ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration results = ctx.search(searchbase, filter, ctrls);

            boolean more = false;
            if (results != null) {
                more = results.hasMore();
            }
            
            while (more) {
                SearchResult result = (SearchResult) results.next();
                String dn = result.getName() + ", " + searchbase;

                Attributes attrs = ctx.getAttributes(dn, attrs_arr);

                if (attrs != null) {
                    namecount++;
                    for (int i = 0; i < attrs_arr.length; i++) {
                        Attribute attr = attrs.get(attrs_arr[i]);
                        if (attr != null) {
                            for (NamingEnumeration e = attr.getAll();
                                 e.hasMoreElements(); ) {
                                String attrib =
                                        (String) e.nextElement();
                                grps[groupcount] =
                                        extractGroupName(
                                                attrib);
                                groupcount++;
                            }
                        }
                    }
                }
                try {
                    more = results.hasMore();
                } catch (NamingException e) {
                    more = false; // Just assume we are done
                }
            }
        } catch (AuthenticationException ae) {
            logger.error("Incorrect Password or UserName!\n" + ae);
            ae.printStackTrace();
            logger.error(this.toString());
        } catch (NamingException e) {
            logger.error("Naming Exception Found!\n" + e);
            e.printStackTrace();
            logger.error(this.toString());
        } catch (Exception e) {
            logger.error("Error accessing LDAP!\n" + e);
            e.printStackTrace();
            logger.error(this.toString());
        }

        String[] groups = new String[groupcount];
        for (int i = 0; i < groupcount; i++) {
            groups[i] = grps[i];
        }
        if (namecount == 0) {
            this.groups = null;
        } else {
            this.groups = groups;
        }
    }

    /**
     * Checks whether the user is a member of the specified group.
     *
     * @return true if success
     */
    public boolean IsMemberOf(String group) {
        group = group.toUpperCase();

        if (groups == null) {
            return false;
        }
        for (int i = 0; i < groups.length; ++i) {
            if (group.equalsIgnoreCase(groups[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the groups to which the user belongs, as one
     * long string using the specified quote and delimiter characters.
     *
     * @parm quote Character(s) to surround each groups name with.
     * @parm delimiter Character(s) to put between each groups name.
     */
    public String GroupsInList(String quote, String delimiter) {
        String list = "";

        if (groups == null) {
            return null;
        }
        for (int i = 0; i < groups.length; ++i) {
            if (list.length() > 0) {
                list += delimiter;
            }
            list += quote + groups[i] + quote;
        }
        return list;
    }

    /**
     * Returns the groups to which the user belongs, as one
     * long single quoted, comma delimited string ready for
     * use in an SQL "IN" clause.
     * (e.g. "'Group one', 'Group two', 'Group three'")
     */
    public String getGroupsInList() {
        return GroupsInList("'", ", ");
    }

    /**
     * Returns the groups to which the user belongs,
     * as an array of strings.
     */
    public String[] getGroups() {
        return groups;
    }

    public String toString() {

        final String endline = "\r\n";
        StringBuffer sb = new StringBuffer();

        sb.append("UserInfo class toString(): ");
        sb.append(endline);
        sb.append("     init_cont_fac: ");
        sb.append(init_cont_fac);
        sb.append(endline);
        sb.append("     sec_auth: ");
        sb.append(sec_auth);
        sb.append(endline);
        sb.append("     searchbase: ");
        sb.append(searchbase);
        sb.append(endline);
        sb.append("     prov_url: ");
        sb.append(prov_url);
        sb.append(endline);
        sb.append("     sec_prin: ");
        sb.append(sec_prin);
        sb.append(endline);
        // sec_cred print out may be a security risk
        //sb.append("     sec_cred: ");
        //sb.append(sec_cred);
        //sb.append(endline);
        sb.append("     filter: ");
        sb.append(filter);
        sb.append(endline);
        sb.append("     user_id: ");
        sb.append(user_id);
        sb.append(endline);
        sb.append("     domain: ");
        sb.append(domain);
        sb.append(endline);

        if (groups != null) {
            for (int i = 0, l = groups.length; i < l; i++) {
                sb.append("     groups[");
                sb.append(i);
                sb.append("]: ");
                sb.append(groups[i]);
                sb.append(endline);
            }
        } else {
            sb.append("     groups[]: == null");
            sb.append(endline);
        }

        if (attrs_arr != null) {
            for (int i = 0, l = attrs_arr.length; i < l; i++) {
                sb.append("     attrs_arr[");
                sb.append(i);
                sb.append("]: ");
                sb.append(attrs_arr[i]);
                sb.append(endline);
            }
        } else {
            sb.append("     attrs_arr[]: == null");
            sb.append(endline);
        }

        return sb.toString();
    }

    /*
    * Extract group names from a DN
    * @param 	dn   distinguished name
    * @return universal type
    */
    public String extractGroupName(String dn) {
        dn = dn.toUpperCase();
        if (dn.startsWith("CN")) {
            int first = (dn.indexOf("=") + 1);
            String firstdn = dn.substring(first);

            int last = (dn.indexOf(","));
            dn = dn.substring(first, last);
        }
        return dn;
    }
    
}
