/*
 * The MIT License
 *
 * Copyright 2014 emerson.
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

package br.uece.lotus.about;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author emerson
 */
public class AboutController implements Initializable {

    @FXML
    Label mLblVersion;
    @FXML
    TextArea mTxtLicense;
    @FXML
    TextArea mTxtTeam;    
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (InputStream inVersion = getClass().getResourceAsStream("/strings/version.txt")) {
            mLblVersion.setText(convertStreamToString(inVersion));            
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inLicense = getClass().getResourceAsStream("/strings/license.txt")) {
            mTxtLicense.setText(convertStreamToString(inLicense));            
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inTeam = getClass().getResourceAsStream("/strings/team.txt")) {
            mTxtTeam.setText(convertStreamToString(inTeam));            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    @FXML
    public void close() {
        mTxtLicense.getScene().getWindow().hide();
    }
    
}
