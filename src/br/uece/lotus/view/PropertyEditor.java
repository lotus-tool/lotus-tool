package br.uece.lotus.view;

import br.uece.lotus.model.Model;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class PropertyEditor extends TableView<PropertySpec> {

    private PropertySheet mPropertySheet;
    private Model mModel;
    private EventHandler onCommit = new EventHandler() {
        @Override
        public void handle(Event t) {
            PropertySpec spec = mPropertySheet.propertiesProperty().get(getSelectionModel().getSelectedIndex());
            String val = ((EditingCell) t.getSource()).getValue();
            System.out.println(spec.nameProperty().getValue() + " <- " + val);
            spec.setValue(mModel, val);            
        }
    };

    public PropertyEditor() {        
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(
                new PropertyValueFactory<PropertySpec, String>("name"));        

        TableColumn colValue = new TableColumn("Value");
        colValue.setCellValueFactory(new Callback<CellDataFeatures<PropertySpec, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<PropertySpec, String> p) {
                ObservableValue<String> val = p.getValue().getValue(mModel);
                System.out.println("col value " + p.getValue().nameProperty().getValue() + val.getValue());
                return val;
            }
        });

        colValue.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell(onCommit);
            }
        });
//        colValue.setOnEditCommit(new EventHandler<CellEditEvent<PropertySpec, String>>() {
//            @Override
//            public void handle(CellEditEvent<PropertySpec, String> t) {
//                PropertySpec spec = (PropertySpec) t.getTableView().getItems();
//                String val = t.getNewValue();
//                System.out.println(spec.nameProperty().getValue() + " <- " + val);
//                spec.setValue(mModel, val);
//            }
//        });
        getColumns()
                .setAll(colName, colValue);
        setEditable(
                true);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setSpec(PropertySheet propertySheet) {
        mPropertySheet = propertySheet;
    }

    public void setModel(Model m) {
        mModel = m;
        setItems(null);
        if (m != null) {
            setItems(mPropertySheet.propertiesProperty());
        }
    }
}

class EditingCell extends TableCell<Object, String> {

    private TextField mTxtValor;
    private EventHandler mOnCommit;    

    public EditingCell(EventHandler onCommit) {
        mOnCommit = onCommit;
        mTxtValor = new TextField();
        mTxtValor.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        mTxtValor.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    mOnCommit.handle(new ActionEvent(EditingCell.this, null));
                    commitEdit(mTxtValor.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();

        setGraphic(mTxtValor);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        mTxtValor.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(String.valueOf(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        System.out.println("update Item " + isEditing());

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                mTxtValor.setText(getString());
                setGraphic(mTxtValor);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    public String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    public String getValue() {
        return mTxtValor.getText();
    }
}