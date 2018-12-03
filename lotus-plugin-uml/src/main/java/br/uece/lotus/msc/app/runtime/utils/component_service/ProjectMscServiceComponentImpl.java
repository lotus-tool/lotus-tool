/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.msc.app.runtime.utils.component_service;


import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.lotus.msc.app.ParallelComponentController;
import br.uece.lotus.msc.app.runtime.config.ConfigurationServiceComponent;
import br.uece.lotus.msc.app.runtime.monitor.ProbabilisticAnnotator;
import br.uece.lotus.msc.app.runtime.utils.ProjectMSCConverter;
import br.uece.lotus.msc.app.runtime.utils.ProjectXMLProjectMSCMSCConverterMSC;
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

    private ProjectMSC projectMSC;

    private ProbabilisticAnnotator annotator;

    private ProjectMSCConverter mscConverter;

    private HmscComponent standardModeling;
    private ParallelComponentController parallelComponentController;

    private Object sourcObject = null;
    private br.uece.lotus.Component parallelComponent;


    public ProjectMscServiceComponentImpl() {
        //  RuntimePlugin plugin = new RuntimePlugin();
        this.mscConverter = new ProjectXMLProjectMSCMSCConverterMSC();
        this.annotator = new ProbabilisticAnnotator();
        this.parallelComponentController = new ParallelComponentController(standardModeling);
        //this.hmscComponent = plugin.getProjectMSC();
    }

    @Override
    public void start(ComponentManager manager) throws Exception {
        ConfigurationServiceComponent configurationComponent = manager.getComponentService(ConfigurationServiceComponent.class);
        if (configurationComponent.getConfiguration().getProjectFile() == null) {
            this.projectMSC = configurationComponent.getConfiguration().getProjectMSC();
            this.parallelComponent = configurationComponent.getConfiguration().getParallelComponent();
            this.standardModeling = projectMSC.getStandardModeling();
            this.mscConverter = new ProjectDSConverterMSC();
            this.sourcObject = standardModeling;


        } else {
            this.projectFile = Paths.get(configurationComponent.getConfiguration().getProjectFile());
            this.mscConverter = new ProjectXMLProjectMSCMSCConverterMSC();
            this.sourcObject = projectFile;
            loadProject();

        }


    }

    @Override
    public void stop(ComponentManager manager) throws Exception {
        //saveProject();
        updateProbabilityProject();
        manager.uninstallComponent(this);
    }

    private void updateProbabilityProject() {
        try {
            mscConverter.toUpdate(projectMSC, sourcObject);
        } catch (Exception e) {
            log.severe(e.getMessage());
            Throwables.propagate(e);
        }
    }

    private void saveProject() {
        try {
            mscConverter.toUndo(projectMSC);
        } catch (Exception e) {
            log.severe(e.getMessage());
            Throwables.propagate(e);
        }
    }

    private void loadProject() {

        try {

            projectMSC = mscConverter.toConverter(sourcObject);

            if (projectFile == null) {
                projectMSC.putValue("file", "");
            } else {
                projectMSC.putValue("file", projectFile.toFile());
            }

            if (isNullOrEmpty(projectMSC.getName())) {
                projectMSC.setName("Untitled");
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            Throwables.propagate(e);
        }
    }



    @Override
    @Synchronized
    public void updateProject(LinkedList<String> trace) {
        this.annotator.annotate(getStantardModeling(), trace);
        updateProbabilityProject();
        parallelComponentController.trySetProbabilityFromTransitionMSC(parallelComponent, standardModeling.getTransitionMSCList());


        //saveProject();
    }

    @Override
    @Synchronized
    public ProjectMSC getProjectMSC() {
        return this.projectMSC;
    }

    @Synchronized
    public HmscComponent getStantardModeling() {
        return this.standardModeling;
    }

    @Override
    public br.uece.lotus.Component getParallelComponent() {
        return this.parallelComponent;
    }

}
