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
package br.uece.lotus.msc.app.runtime.app;

import br.uece.lotus.msc.app.runtime.config.Configuration;
import br.uece.lotus.msc.app.runtime.utils.checker.ConditionalOperator;
import br.uece.lotus.msc.app.runtime.utils.checker.Property;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String[] args) throws Exception {

		// File paths
		Path traceFile = Paths.get("/home/lucas-vieira/Desktop/exemplo-2/trace1-exemplo2.csv");
		Path lotusFile = Paths.get("/home/lucas-vieira/Desktop/exemplo-2/exemplo2.xml");


		// Condictions that I want to verify
		List<Property> properties = new ArrayList<>();


		properties.add(new Property.PropertyBuilder()
				.firstStateId(0)
				.secondStateId(9)
				.conditionalOperator(ConditionalOperator.GREATER_THAN)
				.probability(0.9)
				.build());

		properties.add(new Property.PropertyBuilder()
				.firstStateId(0)
				.secondStateId(1)
				.conditionalOperator(ConditionalOperator.EQUAL_TO)
				.probability(1.0)
				.build());


		MyHandler handler = new MyHandler();


		Configuration configuration = new Configuration.ConfigurationBuilder()
				.traceFile(traceFile.toString())
				.milliseconds(2000L)
				.projectFile(lotusFile.toString())
				.properties(properties)
				.build();

//		Runtime runTime = new Runtime(configuration, handler);
//
//		runTime.start();

	}

}
