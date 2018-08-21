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
package br.uece.lotus.uml.app.runtime.monitor;



import br.uece.lotus.uml.app.runtime.config.ConfigurationServiceComponent;
import br.uece.lotus.uml.app.runtime.utils.checker.ModelCheckerServiceComponent;
import br.uece.lotus.uml.app.runtime.utils.component_service.Component;
import br.uece.lotus.uml.app.runtime.utils.component_service.ComponentManager;
import br.uece.lotus.uml.app.runtime.utils.component_service.ProjectMscServiceComponent;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.Logger;

public class MonitorComponentServiceImpl implements Component, MonitorComponentService {
	
	private static final Logger log = Logger.getLogger(MonitorComponentServiceImpl.class.getName());
	
	private Path traceFile;
	
	private ProjectMscServiceComponent lotusModelComponent;
	
	private ModelCheckerServiceComponent modelCheckerComponent;
	
	private TraceWatcherHelper traceWatcherHelper;

	private Long milliseconds;
	

	@Override
	public void start(ComponentManager manager) throws Exception {
		log.info("Starting the TraceWatcherComponent");
		
		ConfigurationServiceComponent configurationComponent = manager.getComponentService(ConfigurationServiceComponent.class);
		this.milliseconds = configurationComponent.getConfiguration().getMilliseconds();
		this.traceFile = Paths.get(configurationComponent.getConfiguration().getTraceFile());
		
		this.lotusModelComponent = manager.getComponentService(ProjectMscServiceComponent.class);
		this.modelCheckerComponent = manager.getComponentService(ModelCheckerServiceComponent.class);
		this.traceWatcherHelper = new TraceWatcherHelper(this, milliseconds);
		
		new Thread(traceWatcherHelper).start();
	}

	@Override
	public void stop(ComponentManager manager) throws Exception {
		traceWatcherHelper.stop();
		manager.uninstallComponent(this);
	}
	
	@Override
	public void processTrace(LinkedList<String> trace) {
		this.lotusModelComponent.updateProject(trace);
		this.modelCheckerComponent.verifyModel();
	}

	@Override
	public Path getTracePath() {
		return this.traceFile;
	}

}


