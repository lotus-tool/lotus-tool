package br.uece.gamut.view;

import br.uece.gamut.model.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class PropertyEditor extends TableView<PropertySpec> {

    private PropertySheet mPropertySheet;
    private Model mModel;

    public PropertyEditor() {
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(
                new PropertyValueFactory<PropertySpec, String>("name"));

        TableColumn colValue = new TableColumn("Value");
        colValue.setCellValueFactory(
                new PropertyValueFactory<PropertySpec, String>("value"));

        //--- Add for Editable Cell of Value field, in Double
//        Callback<TableColumn, TableCell> cellFactory =
//                new Callback<TableColumn, TableCell>() {
//            public TableCell call(TableColumn p) {
//                TableCell t = new EditingCell();
////                t.
//            }
//        };
//        colValue.setCellFactory(cellFactory);
        colValue.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<PropertySpec, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PropertySpec, Double> t) {
//                ((PropertySpec) t.getTableView().getItems().get(
//                        t.getTablePosition().getRow())).setFieldValue(t.getNewValue());
                System.out.println("editou!");
            }
        });
        getColumns().setAll(colName, colValue);
        setEditable(true);
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

    private TextField textField;
    private EventHandler mOnCommit;

    public EditingCell() {
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }

        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.selectAll();
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

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {                    
//                    mOnCommit.handle();
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}