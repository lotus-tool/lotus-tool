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
package br.uece.lotus.msc.app.runtime.utils.checker.conditional;

import br.uece.lotus.msc.app.runtime.utils.checker.ConditionalOperator;

import java.util.HashMap;
import java.util.Map;

public class ConditionContext {
	
	private Map<ConditionalOperator, ConditionChecker> checkers;
	
	public ConditionContext() {
		this.checkers = new HashMap<ConditionalOperator, ConditionChecker>();
		
		checkers.put(ConditionalOperator.EQUAL_TO, new ConditionEqualTo());
		checkers.put(ConditionalOperator.GREATER_THAN, new ConditionGreaterThan());
		checkers.put(ConditionalOperator.GREATER_THAN_OR_EQUAL_TO, new ConditionGreaterThanOrEqualTo());
		checkers.put(ConditionalOperator.LESS_THAN, new ConditionLessThan());
		checkers.put(ConditionalOperator.LESS_THAN_OR_EQUAL_TO, new ConditionLessThanOrEqualTo());
		checkers.put(ConditionalOperator.NOT_EQUAL_TO, new ConditionNotEqualTo());
		
	}

	public boolean verify(Double userProbability, ConditionalOperator operator, Double probabilityBetween) {
		return checkers.get(operator).verify(userProbability, probabilityBetween);
	}
	
}
