/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.uml.app.runtime.utils.component_service;


import br.uece.lotus.uml.app.runtime.config.Configuration;
import br.uece.lotus.uml.app.runtime.config.ConfigurationServiceComponent;
import br.uece.lotus.uml.app.runtime.config.ConfigurationServiceComponentImpl;
import br.uece.lotus.uml.app.runtime.monitor.MonitorComponentServiceImpl;
import br.uece.lotus.uml.app.runtime.notifier.NotifierComponentServiceImpl;
import br.uece.lotus.uml.app.runtime.notifier.ViolationHandler;
import br.uece.lotus.uml.app.runtime.utils.checker.ModelCheckerComponentServiceImpl;

public class Runtime {

	private Configuration configuration;
	
	private ViolationHandler violationHandler;
	
	private ComponentManager manager;
	
	public Runtime(Configuration configuration, ViolationHandler violationHandler) {
		this.configuration = configuration;
		this.violationHandler = violationHandler;
		this.manager = new ComponentManagerImpl();
	}
	
	public void start() throws Exception {
		
		// Configuration
		ConfigurationServiceComponent configurationServiceComponent = new ConfigurationServiceComponentImpl();
		configurationServiceComponent.setConfiguration(configuration);
		configurationServiceComponent.setViolationHandler(violationHandler);
		
		manager.installComponent((Component)configurationServiceComponent);
		manager.installComponent(new NotifierComponentServiceImpl());
		manager.installComponent(new ProjectMscServiceComponentImpl());
		manager.installComponent(new MonitorComponentServiceImpl());
		manager.installComponent(new ModelCheckerComponentServiceImpl());
		
		manager.start(manager);
	}

	public void stop() throws Exception {
		manager.stop(manager);

	}

}
