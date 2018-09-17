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

import br.uece.lotus.Transition;
import br.uece.lotus.uml.app.runtime.config.ConfigurationServiceComponent;
import br.uece.lotus.uml.app.runtime.notifier.NotifierComponentService;
import br.uece.lotus.uml.app.runtime.probabilisticReach.DefaultProbabilisticReachAlgorithm;
import br.uece.lotus.uml.app.runtime.probabilisticReach.AfterAndAndNotProbabilisticReachAlgorithm;
import br.uece.lotus.uml.app.runtime.probabilisticReach.ProbabilisticReachAlgorithmStrategy;
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
	
	private ProbabilisticReachAlgorithmStrategy reachAlgorithm;
	
	private NotifierComponentService eventBusComponentService;
	
	private ConditionContext conditionContext;
	private br.uece.lotus.Component parallelComponent;

	public ModelCheckerComponentServiceImpl() {
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

            Double probabilityBetween = null;
			if(property.getTemplate().equals(Template.DEFAULT.toString())){

				reachAlgorithm = new DefaultProbabilisticReachAlgorithm();
                 probabilityBetween = reachAlgorithm.probabilityBetween(
                        parallelComponent,
                        property.getFirstStateId(),
                        property.getSecondStateId());


			}else if(property.getTemplate().equals(Template.AFTER.toString())){

				reachAlgorithm = new AfterAndAndNotProbabilisticReachAlgorithm();
                probabilityBetween = reachAlgorithm.probabilityBetween(
                        parallelComponent,
                        property.getSecondStateId(),
                        property.getFirstStateId(),
                        -1,
                        property.getFirstStateId());

			}else if (property.getTemplate().equals(Template.AND_NOT.toString())){
                reachAlgorithm = new AfterAndAndNotProbabilisticReachAlgorithm();

              /*  br.uece.lotus.Component changedComponent = parallelComponent.clone();
				Transition transition = changedComponent.getStateByID(property.getSecondStateId()).getOutgoingTransitionsList().get(0);
               	transition.setProbability(0.0);

				State currentState = changedComponent.getStateByID(property.getSecondStateId());

				while (currentState != null){
					Transition currentTrasition = null;

					if(!currentState.getOutgoingTransitionsList().isEmpty()){
						currentTrasition = currentState.getOutgoingTransitionsList().get(0);
					}else {
						break;
					}

					currentState = currentTrasition.getDestiny();

				}

                probabilityBetween = reachAlgorithm.probabilityBetween(
						changedComponent,
                        0,
                        property.getSecondStateId(),
                        -1,
						*//*changedComponent.getStateByID(property
								.getFirstStateId())
								.getOutgoingTransitionsList()
								.get(0)
								.getDestiny()
								.getID()*//*
						currentState.getID()
						);*/

                br.uece.lotus.Component changedComponent = parallelComponent.clone();
                Transition transition = changedComponent.getStateByID(property.getSecondStateId()).getOutgoingTransitionsList().get(0);
                transition.setProbability(0.0);


                probabilityBetween = reachAlgorithm.probabilityBetween(
                        changedComponent,
                        0,
                        property.getFirstStateId(),
                        -1,
						property.getSecondStateId()
                );
            }




			if (!conditionContext.verify(
					(double)property.getProbability(),
					property.getConditionalOperator(),
					(double)probabilityBetween)) {
				eventBusComponentService.publish(property);
				log.info(property.toString());

				System.out.println("violou");
			}

			System.out.println("prob"+ probabilityBetween);

		}
	}
	
	@Override
	public void stop(ComponentManager manager) throws Exception {
		manager.uninstallComponent(this);
	}
	
}
