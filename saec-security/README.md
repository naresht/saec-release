# Active Directory Integration

**Goals** 
Enable ActiveDirectory authentication with BFDS network, manage roles in SAEC, and integrate with Spring Security Framework. Decoupling the solution for reuse would be nice.

Several strategies researched:

## Requirements
[Non Functional Requirements](https://github.com/BFDS/saec/wiki/LDAP-Security-Technical-Requirements)

## Pojo based
* Use existing legacy pojo LDAP code from SAEC ldap utils
* Works fine to authenticate and get remote group info
* TODO: Integrate with Spring, add NTLM authentication from client (browser) etc.,
* STATUS: incomplete as a comprehensive solution, but standalone class can be used to test ActiveDirectory authentication

## Spring Security LDAP
* [Spring LDAP Security Reference](http://static.springsource.org/spring-security/site/docs/3.1.x/reference/springsecurity-single.html#ldap)
* Spring uses ActiveDirectoryLdapAuthenticationProvider, which delegates the work to LdapAuthenticator and LdapAuthoritiesPopulator which are responsible for authenticating the user and retrieving the user's set of GrantedAuthoritys respectively.UserDetails can be populated using DefaultLdapAuthoritiesPopulator.
* Able to setup a local LDAP instance in an example web app; is easily unit-testable.
* STATUS: With the correct users.ldif (sample ldap user info) app works for authentication/authorization. However single-sign on in production env needs additional filters and customization.

## Tomcat 7 integration
* Not sure how [Tomcat integration](http://tomcat.apache.org/tomcat-7.0-doc/windows-auth-howto.html) is useful since configuring Realms and other server settings is an additional step outside of the App. Each environment has to ensure these settings exist (which means local development and test, stage, prod configuration grows exponentially rather than allowing the app to manage). Also, the authentication object needs to be populated in Spring's context (ie., the application's context). This leads to Spring being aware of Tomcat's Realms and settings +  the web app supporting Tomcat's authentication (SPNEGO) etc., 
* Tomcat integration is geared towards plain servlet apps.
* STATUS: unable to research completely; perhaps, not recommended due to multiple configuration options outside of app 

## Spring Security Kerberos/SPNEGO Extension
* For Kerberos (unix or windows) based, client based (browser) authentication, this extension from Spring is apt
* Needs some knowledge on the server side (setup kerberos etc.,) so may not be "out of the box"
* Reference example is [here](http://blog.springsource.com/2009/09/28/spring-security-kerberos/)
* STATUS: unsure if this works with BFDS Active Directory with NTLM. Might be good for future Kerberos migration

## Kerberos + NTLM with Waffle Filter support with Spring Security
* More flexible than the above example since it supports both Kerberos and Windows NTLM. With Tomcat and an IIS front-end to do authentication only, Waffle will allow you to get rid of IIS.Unlike many other implementations WAFFLE on Windows does not usually require any server-side Kerberos keytab setup, it's a drop-in solution.
* Integrates well with Spring Security
* Project Site is [here](http://waffle.codeplex.com/)
* Reference is [here](http://code.dblock.org/single-sign-on-spring-security-negotiate-filter-kerberos-ntlm-wwaffle)
* Was able to get this working with custom classes that extend Waffle's filter and custom Authentication object. WORKS as a single-sign within BFDS network and roles are correctly mapped into granted authorities. Of course, static data needs to be correct for authorization to work. 

## Implementation Summary
* Added a new module, saec-security that embeds the entire spring security/active-directory related code with minimal coupling to other modules
* The recommended approach is to switch between "simple spring security login form" (dev/qa) or "LDAP-local-security" (qa/demo, if needed) or "waffle-security-filter" (prod) strategies based on the environment. This is manual step for now in saec-ui-jsf/**/saec-applicationContext-parent.xml, and can be automated in a pre-compile step in maven.
* A change of strategy in the future will only require a new authentication strategy (few classes) as the spring authorization piece is generic or can be reused.
* Code is generic enough to support future Spring/active directory implementations as a near "drop-in" solution. 
* The current framework, Waffle, also supports Kerberos (not tested); future changes in our ActiveDirectory implementation should be supported with our current solution. I would expect minimal refactoring.
* IIS Server is not required as a passthrough authentication mechanism before Active Directory, an advantage since this eliminates any configuration setup required for multiple environments.