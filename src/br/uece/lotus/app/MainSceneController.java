package br.uece.lotus.app;

import br.uece.lotus.util.EditableTab;
import br.uece.lotus.model.ProjectModel;
import br.uece.lotus.model.ComponentModel;
import br.uece.lotus.model.Model;
import br.uece.lotus.model.StateModel;
import br.uece.lotus.model.TransitionModel;
import br.uece.lotus.plugins.GeracaoCasosTeste;
import br.uece.lotus.view.ComponentEditor;
import br.uece.lotus.view.PropertyEditor;
import br.uece.lotus.view.PropertySheet;
import br.uece.lotus.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

public class MainSceneController implements Initializable {

    //Barra de ferramentas
    @FXML
    protected Button mBtnNovoComponente;
    @FXML
    protected Button mBtnNovoProjeto;
    @FXML
    protected Button mBtnAbrirProjeto;
    @FXML
    protected Button mBtnSalvar;
    //Area do canvas
    @FXML
    protected BorderPane mEditorWrapper;
    //Abas de componentes
    @FXML
    protected TabPane mTabComponentes;
    //Barra de edição de componente    
    @FXML
    protected ToolBar mBarFerramentas;
    @FXML
    protected ToggleButton mBtnDefault;
    @FXML
    protected ToggleButton mBtnVertice;
    @FXML
    protected ToggleButton mBtnTransicao;
    @FXML
    protected ToggleButton mBtnApagar;
    @FXML
    private ListView mLstComponents;
    @FXML
    private AnchorPane mPropriedadesWrapper;
    // Componentes Visuais
    private PropertyEditor mPropertyEditor;
    private ComponentEditor mEditor;
    //Especificações e modelos
    private ProjectModel mProjeto;
    private boolean mProjetoModificado;
    private File mProjetoFile;
    private PropertySheet mStatePropertySheet;
    private PropertySheet mTransitionPropertySheet;
    ////////////////////////////////////////////////////////
    // Exibição dos Componentes
    ////////////////////////////////////////////////////////
    private EventHandler<ActionEvent> mMudarNomeComponenteHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            EditableTab tab = ((EditableTab) t.getSource());
            ((ComponentModel) tab.getUserData()).setName(tab.getLabel());
            mLstComponents.setItems(null);
            mLstComponents.setItems(mProjeto.getComponents());
            setProjetoModificado(true);
        }
    };
    private ChangeListener<Tab> mAoSelecionarAba = new ChangeListener<Tab>() {
        @Override
        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
            changeComponent(t1 == null ? null : (ComponentModel) t1.getUserData());
        }
    };
    private EventHandler<? super MouseEvent> mAoDuploCliqueListaComponentes = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getButton().equals(MouseButton.PRIMARY)) {
                if (t.getClickCount() == 2) {
                    ComponentModel c = (ComponentModel) mLstComponents.getSelectionModel().getSelectedItem();
                    changeComponent(c);
                }
            }
        }
    };
    ////////////////////////////////////////////////////////
    // Bind Editor com o painel de propriedades
    ////////////////////////////////////////////////////////
    private ComponentEditor.OnSelectionChange mAoSelecionarModel = new ComponentEditor.OnSelectionChange() {
        @Override
        public void onSelectionChange(ComponentEditor e) {
            View v = e.getSelectedView();
            if (v == null) {
                mPropertyEditor.setSpec(null);
                mPropertyEditor.setModel(null);
            } else {
                Model m = v.getModel();
                if (m instanceof StateModel) {
                    mPropertyEditor.setSpec(mStatePropertySheet);
                } else if (m instanceof TransitionModel) {
                    mPropertyEditor.setSpec(mTransitionPropertySheet);
                }
                mPropertyEditor.setModel(m);
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup g = new ToggleGroup();
        mBtnDefault.setToggleGroup(g);
        mBtnVertice.setToggleGroup(g);
        mBtnTransicao.setToggleGroup(g);
        mBtnApagar.setToggleGroup(g);

        mTabComponentes.getSelectionModel().selectedItemProperty().addListener(mAoSelecionarAba);
        mLstComponents.setOnMouseClicked(mAoDuploCliqueListaComponentes);

        mEditor = new ComponentEditor();
        mEditorWrapper.setCenter(mEditor);

        mPropertyEditor = new PropertyEditor();
        mStatePropertySheet = new PropertySheet();
        mStatePropertySheet.newProperty("Default", "default");
        mTransitionPropertySheet = new PropertySheet();
        mTransitionPropertySheet.newProperty("Label", ComponentEditor.TAG_LABEL);
        mTransitionPropertySheet.newProperty("Guard", ComponentEditor.TAG_GUARD);
        mTransitionPropertySheet.newProperty("Probability", ComponentEditor.TAG_PROBABILIDADE);

        mPropriedadesWrapper.getChildren().add(mPropertyEditor);
        AnchorPane.setTopAnchor(mPropertyEditor, 0D);
        AnchorPane.setLeftAnchor(mPropertyEditor, 0D);
        AnchorPane.setBottomAnchor(mPropertyEditor, 0D);
        AnchorPane.setRightAnchor(mPropertyEditor, 0D);
        mEditor.setOnSelectionChange(mAoSelecionarModel);

        handleNovoProjeto(null);
    }

    private void changeProject(ProjectModel p) {
        mProjeto = p;
        if (mProjeto != null) {
            mLstComponents.setItems(mProjeto.getComponents());
            mTabComponentes.getTabs().clear();
            if (p.getComponents().size() > 0) {
                changeComponent(p.getComponents().get(0));
            } else {
                changeComponent(null);
            }
        }
    }

    @FXML
    protected void handleNovoProjeto(ActionEvent event) {
        if (verificarModificacoesNaoSalvas("criar um novo projeto")) {
            return;
        }
        ProjectModel p = new ProjectModel();
        p.setName("Untitled");
        p.newComponent("Component1");
        changeProject(p);
        setProjetoModificado(false);
    }

    @FXML
    protected void handleNovoComponente(ActionEvent event) {
        mProjetoModificado = true;
        String name = "Component" + mProjeto.getComponents().size();
        ComponentModel c = mProjeto.newComponent(name);
        changeComponent(c);
        setProjetoModificado(true);
    }

    @FXML
    protected void handleAbrirProjeto(ActionEvent event) {
        if (verificarModificacoesNaoSalvas("abrir")) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        try (FileInputStream in = new FileInputStream(file)) {
            ProjectModel p = GrafoSerializer.parseStream(in);
            p.setName(file.getName());
            changeProject(p);
            mProjetoFile = file;
            setProjetoModificado(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
        }
    }

    @FXML
    protected void handleSalvarProjeto(ActionEvent event) {
        if (mProjetoFile == null) {
            FileChooser fileChooser = new FileChooser();
            mProjetoFile = fileChooser.showSaveDialog(null);
            if (mProjetoFile == null) {
                return;
            }
        }
        try (FileOutputStream out = new FileOutputStream(mProjetoFile)) {
            GrafoSerializer.toStream(mProjeto, out);
            mProjeto.setName(mProjetoFile.getName());
            setProjetoModificado(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
        }
    }

    @FXML
    protected void handleFerramentaDefault(ActionEvent event) {
        mEditor.setModo(ComponentEditor.MODO_NENHUM);
    }

    @FXML
    protected void handleFerramentaVertice(ActionEvent event) {
        mEditor.setModo(ComponentEditor.MODO_VERTICE);
    }

    @FXML
    protected void handleFerramentaTransicao(ActionEvent event) {
        mEditor.setModo(ComponentEditor.MODO_TRANSICAO);
    }

    @FXML
    protected void handleFerramentaApagar(ActionEvent event) {
        mEditor.setModo(ComponentEditor.MODO_REMOVER);
    }

    @FXML
    protected void handleSair(ActionEvent event) {
        if (verificarModificacoesNaoSalvas("fechar")) {
            return;
        }
        Platform.exit();
    }

    private void changeComponent(ComponentModel c) {
        int i = mProjeto.getComponents().indexOf(c);
        mLstComponents.getSelectionModel().select(i);
        mEditor.setModel(c);
        mEditor.setVisible(c != null);
        mBarFerramentas.setDisable(c == null);
        if (c == null) {
            return;
        }

        //ajustar aba
        for (Tab tab : mTabComponentes.getTabs()) {
            if (tab.getUserData() == c) {
                mTabComponentes.getSelectionModel().select(tab);
                return;
            }
        }

        EditableTab aux = new EditableTab(c.getName());
        aux.setUserData(c);
        aux.setOnChange(mMudarNomeComponenteHandler);
        mTabComponentes.getTabs()
                .add(aux);
        mTabComponentes.getSelectionModel().select(aux);
    }

    private void setProjetoModificado(boolean a) {
        mProjetoModificado = a;
        if (mProjeto != null) {
            Startup.setAppTitle((a ? "*" : "") + mProjeto.getName());
        } else {
            Startup.setAppTitle(null);
        }
    }

    private boolean verificarModificacoesNaoSalvas(String acao) {
        if (mProjeto != null && mProjetoModificado) {
            String msg = "Salvar as alterações para o projeto \"" + mProjeto.getName() + "\" antes de " + acao + "?";
            int r = JOptionPane.showConfirmDialog(null, msg, null, JOptionPane.YES_NO_CANCEL_OPTION);
            if (r == JOptionPane.CANCEL_OPTION) {
                return true;
            } else if (r == JOptionPane.YES_OPTION) {
                handleSalvarProjeto(null);
            }
        }
        return false;
    }
    
    @FXML
    protected void handleGeracaoCasosTestes() {
        new GeracaoCasosTeste().run();
    }
}
