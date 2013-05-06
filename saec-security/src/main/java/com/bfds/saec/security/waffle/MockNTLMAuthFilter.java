package com.bfds.saec.security.waffle;

import javax.servlet.*;
import java.io.IOException;

/**
 * Mock NTLM Authentication Filter to be used for dev/test
 * TODO
 */
public class MockNTLMAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No Op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       /* NegotiateSecurityFilter _filter = new NegotiateSecurityFilter();
        		_filter.setAuth(new WindowsAuthProviderImpl());
        MockWindowsIdentity mockWindowsIdentity = new MockWindowsIdentity("user", new ArrayList<String>());
        WindowsPrincipal windowsPrincipal = new WindowsPrincipal(mockWindowsIdentity);
        request.setUserPrincipal(windowsPrincipal);
        request.setMethod("POST");
        request.setContentLength(0);
        request.addHeader("Authorization", "NTLM TlRMTVNTUAABAAAABzIAAAYABgArAAAACwALACAAAABXT1JLU1RBVElPTkRPTUFJTg==");
        _filter.doFilter(request, response, chain);*/
    }

    @Override
    public void destroy() {
        // No Op
    }
}
