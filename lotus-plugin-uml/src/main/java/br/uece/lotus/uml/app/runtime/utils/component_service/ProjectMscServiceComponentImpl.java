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

import br.uece.lotus.uml.api.ds.StandardModeling;


import br.uece.lotus.uml.app.runtime.config.ConfigurationServiceComponent;
import br.uece.lotus.uml.app.runtime.model.custom.ProjectMSCCustom;
import br.uece.lotus.uml.app.runtime.model.custom.StantardModelingCustom;
import br.uece.lotus.uml.app.runtime.monitor.ProbabilisticAnnotator;
import br.uece.lotus.uml.app.runtime.utils.ProjectMSCConverter;
import br.uece.lotus.uml.app.runtime.utils.ProjectObjectLotusProjectMSCCustomConverter;
import com.google.common.base.Throwables;
import net.engio.mbassy.listener.Synchronized;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.Logger;

import static com.google.common.base.Strings.isNullOrEmpty;

public class ProjectMscServiceComponentImpl implements Component, ProjectMscServiceComponent {
	
	private static final Logger log = Logger.getLogger(ProjectMscServiceComponentImpl.class.getName());
	
	private Path projectFile;
	
	private ProjectMSCCustom projectMSCCustom;
	
	private ProbabilisticAnnotator annotator;
	
	private ProjectMSCConverter mscConverter;

	private StandardModeling standardModeling;



	public ProjectMscServiceComponentImpl() {
      //  RuntimePlugin plugin = new RuntimePlugin();

		this.mscConverter = new ProjectObjectLotusProjectMSCCustomConverter();
		this.annotator = new ProbabilisticAnnotator();
		//this.standardModeling = plugin.getStandardModeling();
	}

	@Override
	public void start(ComponentManager manager) throws Exception {
		ConfigurationServiceComponent configurationComponent = manager.getComponentService(ConfigurationServiceComponent.class);
		if(configurationComponent.getConfiguration().getProjectFile() == null){
			this.standardModeling = configurationComponent.getConfiguration().getStandardModeling();
		}else {
			this.projectFile = Paths.get(configurationComponent.getConfiguration().getProjectFile());
		}

		loadProject();
	}
	
	@Override
	public void stop(ComponentManager manager) throws Exception {
		//saveProject();
		updateProbabilityProject();
		manager.uninstallComponent(this);
	}

	private void updateProbabilityProject(){


		try {
			mscConverter.toUpdate(projectMSCCustom, standardModeling);
		} catch (Exception e) {
			log.severe(e.getMessage());
			Throwables.propagate(e);
		}
	}

	private void saveProject() {
		try {
			mscConverter.toUndo(projectMSCCustom);
		} catch (Exception e) {
			log.severe(e.getMessage());
			Throwables.propagate(e);
		}
	}
	
	private void loadProject() {
		try  {
			projectMSCCustom = mscConverter.toConverter(standardModeling);

			if(projectFile == null){
				projectMSCCustom.putValue("file","");
			}else {
				projectMSCCustom.putValue("file", projectFile.toFile());
			}
			
			if (isNullOrEmpty(projectMSCCustom.getName())) {
				projectMSCCustom.setName("Untitled");
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			Throwables.propagate(e);
		}
	}
	
	@Override
	@Synchronized
	public void updateProject(LinkedList<String> trace) {
		this.annotator.annotate(getStantardModelingCustom(), trace);
		updateProbabilityProject();
		//saveProject();
	}
	
	@Override
	@Synchronized
	public ProjectMSCCustom getProjectMSCCustom() {
		return this.projectMSCCustom;
	}

	@Synchronized
	public StantardModelingCustom getStantardModelingCustom() {
		return this.projectMSCCustom.getStantardModelingCustom(0);
	}
	
}
