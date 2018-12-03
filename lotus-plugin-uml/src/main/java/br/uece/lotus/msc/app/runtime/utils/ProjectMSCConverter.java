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
package br.uece.lotus.msc.app.runtime.utils;



import br.uece.lotus.msc.api.model.msc.ProjectMSC;


/**
 *
 * @author Lucas Vieira
 */
public interface ProjectMSCConverter<Source> {


    ProjectMSC toConverter(Source s) throws Exception;

    void toUpdate(ProjectMSC projectMSCIn, Source s) throws Exception;

    Source toUndo(ProjectMSC projectMSCIn) throws Exception;





//    ProjectMSCCustom toConverter(InputStream inputStream) throws Exception;
//
//    void toUndo(ProjectMSCCustom projectMSCCustom, OutputStream outputStream) throws Exception;
//
//    void toUpdate(ProjectMSCCustom projectMSCCustom, InputStream inputStream, Path path) throws Exception;
    
}
