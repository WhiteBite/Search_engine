package search_engine;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldListCell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoaderDoc implements Runnable {
    private TreeItem<File> selectedItem;
    private ListView<String> listView;

    LoaderDoc(TreeItem<File> selectedItem, ListView<String> listView) {
        this.selectedItem = selectedItem;
        this.listView = listView;
    }

    public void run() {
        System.out.println("Thread LoaderDoc run!");
        loadDoc(selectedItem, listView);
    }

    private void loadDoc(TreeItem<File> selectedItem, ListView<String> listView) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setOnEditCommit(t -> listView.getItems().set(t.getIndex(), t.getNewValue()));
        listView.setEditable(true); // change
        listView.getItems().clear();
        int numRom = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(selectedItem.getValue().getAbsolutePath()));
            String s;
            while ((s = in.readLine()) != null) {
                numRom++;
                listView.getItems().add(numRom + ": " + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}