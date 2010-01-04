package com.crawljax.core.plugin;

import com.crawljax.core.configuration.ProxyConfiguration;

/**
 * Plugin interface to provide a proxy with settings to Crawljax.
 * 
 * @author Frank Groeneveld <frankgroeneveld@gmail.com>
 * @version $Id: ProxyServerPlugin.java 6394 2009-12-29 14:06:00Z frank $
 */
public interface ProxyServerPlugin extends Plugin {

	/**
	 * Starts the proxy server and provides Crawljax with the correct settings such as port number.
	 * Warning the config argument is not a clone, changes will influence the behaviour of the
	 * Browser. Changes should be returned as new Object.
	 * 
	 * @param config
	 *            Proxy configuration to set a port and hostname.
	 */
	void proxyServer(ProxyConfiguration config);
}