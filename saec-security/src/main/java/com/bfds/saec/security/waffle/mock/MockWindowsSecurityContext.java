package com.bfds.saec.security.waffle.mock;

/*******************************************************************************
* Waffle (http://waffle.codeplex.com)
* 
* Copyright (c) 2010 Application Security, Inc.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Application Security, Inc.
*******************************************************************************/
import java.util.ArrayList;
import java.util.List;

import waffle.windows.auth.IWindowsIdentity;
import waffle.windows.auth.IWindowsImpersonationContext;
import waffle.windows.auth.IWindowsSecurityContext;

import com.sun.jna.platform.win32.Sspi.CtxtHandle;
import com.sun.jna.platform.win32.Sspi.SecBufferDesc;

/**
 * @author dblock[at]dblock[dot]org
 */
public class MockWindowsSecurityContext implements IWindowsSecurityContext {

	private IWindowsIdentity _identity = null;
	
	public MockWindowsSecurityContext(String username) {
		List<String> groups = new ArrayList<String>();
		groups.add("Users");
		groups.add("Everyone");
		_identity = new MockWindowsIdentity(username, groups);		
	}
	
	public void dispose() {
		
	}

	public boolean getContinue() {
		return false;
	}

	public CtxtHandle getHandle() {
		return new CtxtHandle();
	}

	public IWindowsIdentity getIdentity() {
		return _identity;
	}

	public String getPrincipalName() {
		return _identity.getFqn();
	}

	public String getSecurityPackage() {
		return "Mock";
	}

	public byte[] getToken() {
		return null;
	}

	public IWindowsImpersonationContext impersonate() {
		return new MockWindowsImpersonationContext();
	}

	public void initialize() {
		
	}

	public void initialize(CtxtHandle continueCtx, SecBufferDesc continueToken, String targetPrincipalName) {
		
	}
}