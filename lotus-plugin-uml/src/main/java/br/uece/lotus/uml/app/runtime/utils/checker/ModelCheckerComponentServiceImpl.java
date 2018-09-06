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
package br.uece.lotus.uml.app.runtime.utils.checker;

import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.app.ParallelComponentController;
import br.uece.lotus.uml.app.runtime.config.ConfigurationServiceComponent;
import br.uece.lotus.uml.app.runtime.notifier.NotifierComponentService;
import br.uece.lotus.uml.app.runtime.probabilisticReach.ProbabilisticReachAlgorithm;
import br.uece.lotus.uml.app.runtime.utils.checker.conditional.ConditionContext;
import br.uece.lotus.uml.app.runtime.utils.component_service.Component;
import br.uece.lotus.uml.app.runtime.utils.component_service.ComponentManager;
import br.uece.lotus.uml.app.runtime.utils.component_service.ProjectMscServiceComponent;


import java.util.List;
import java.util.logging.Logger;

public class ModelCheckerComponentServiceImpl implements Component, ModelCheckerServiceComponent {
	
	private static final Logger log = Logger.getLogger(ModelCheckerComponentServiceImpl.class.getName());
	
	private ProjectMscServiceComponent modelComponent;
	
	private List<Property> properties;
	
	private ProbabilisticReachAlgorithm reachAlgorithm;
	
	private NotifierComponentService eventBusComponentService;
	
	private ConditionContext conditionContext;
	private br.uece.lotus.Component parallelComponent;

	public ModelCheckerComponentServiceImpl() {
		this.reachAlgorithm = new ProbabilisticReachAlgorithm();
		this.conditionContext = new ConditionContext();
	}
	
	@Override
	public void start(ComponentManager manager) throws Exception {
		ConfigurationServiceComponent configurationComponent = manager.getComponentService(ConfigurationServiceComponent.class);
		this.properties = configurationComponent.getConfiguration().getProperties();
		this.modelComponent = manager.getComponentService(ProjectMscServiceComponent.class);
		this.eventBusComponentService = manager.getComponentService(NotifierComponentService.class);
		parallelComponent = modelComponent.getParallelComponent();

	}
	
	@Override
	public void verifyModel() throws Exception {

		for (Property property : properties) {
			Double probabilityBetween = reachAlgorithm.probabilityBetween(parallelComponent, property.getFirstStateId(), property.getSecondStateId());
			
			if (conditionContext.verify(property.getProbability(), property.getConditionalOperator(), probabilityBetween)) {
				eventBusComponentService.publish(property);
				log.info(property.toString());
			}
		}
	}
	
	@Override
	public void stop(ComponentManager manager) throws Exception {
		manager.uninstallComponent(this);
	}
	
}
