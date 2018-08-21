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

import java.util.logging.Logger;


public class TraceWatcherHelper implements Runnable {
	
	private static final Logger log = Logger.getLogger(TraceWatcherHelper.class.getName());
	
	private MonitorComponentService monitorService;
	
	// The length of time to sleep in milliseconds
	private Long milliseconds;
	
	private boolean stopFlag;
	
	
	public TraceWatcherHelper(MonitorComponentServiceImpl monitorService, Long milliseconds) {
		this(monitorService);
		this.milliseconds = milliseconds;
	}
	
	public TraceWatcherHelper(MonitorComponentServiceImpl monitorService) {
		stopFlag = false;
		this.monitorService = monitorService;
	}

	@Override
	public void run() {
		
		try {
			FileWatcher fileWatcher = new FileWatcher(monitorService);
			
			if (milliseconds == null) {
				
				// Primeiro, realizar a leitura de todo o arquivo.
				fileWatcher.readTrace();

				while(!stopFlag) {
					fileWatcher.processEvents();
				}
			}
			
			else {
				while(!stopFlag) {
					// Primeiro, ler todo o arquivo
					fileWatcher.readTrace();
					
					// Esperar um periodo determinado. Para que depois, realize uma nota tentativa de leitura do arquivo de trace
					Thread.sleep(milliseconds);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getMessage());
		}
	}
	
	public void stop() {
		stopFlag = true;
	}
	
}
