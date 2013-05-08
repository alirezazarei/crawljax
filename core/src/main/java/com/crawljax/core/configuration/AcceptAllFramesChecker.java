package com.crawljax.core.configuration;

import java.io.Serializable;

/**
 * This class accepts all frames.
 * 
 * @author Stefan Lenselink <slenselink@google.com>
 */
public class AcceptAllFramesChecker implements IgnoreFrameChecker , Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1899153794022545354L;

	@Override
	public boolean isFrameIgnored(String frameId) {
		return false;
	}
}
