package search_engine;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.Setter;
import search_engine.algorithm.Myr;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

class FileSystemTree {
    @Setter
    String rootFolder = "";
    private TreeView<File> treeView;
    private TreeItem<File> rootTreeItem;


    FileSystemTree() {
        treeView = new TreeView<>();
    }

    FileSystemTree(String root) {
        this();
        rootFolder = root;
        createTree();
    }

    void createTree() {
        // create tree structure recursively
        rootTreeItem = createTreeRoot();
        treeView.setRoot(rootTreeItem);
        createTree(rootTreeItem);
        // sort tree structure by name
        rootTreeItem.getChildren().sort(Comparator.comparing(t -> t.getValue().toString().toLowerCase()));
    }

    private static void createTree(TreeItem<File> rootItem) {


        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(rootItem.getValue().getPath()))) {
            for (Path path : directoryStream) {
                TreeItem<File> newItem = new TreeItem<>(new File(String.valueOf(path)));
                newItem.setExpanded(true);

                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) {
                    createTree(newItem);
                }
            }
        }
        // catch exceptions, e. g. java.nio.file.AccessDeniedException: c:\System Volume Information, c:\$RECYCLE.BIN
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void filter(TreeItem<File> root, String filter, TreeItem<File> filteredRoot, String searchString) throws IOException {

        for (TreeItem<File> child : root.getChildren()) {
            TreeItem<File> filteredChild = new TreeItem<>(child.getValue());
            filteredChild.setExpanded(true);
            filter(child, filter, filteredChild, searchString);
            if (!filteredChild.getChildren().isEmpty()) {
                filteredRoot.getChildren().add(filteredChild);
            }
            if (isMatch(filteredChild.getValue(), filter)) {

                var path = Paths.get(filteredChild.getValue().getPath());
                if (Myr.searchFor(searchString, path).isFound())
                    filteredRoot.getChildren().add(filteredChild);
            }
        }
    }

    private boolean isMatch(File value, String filter) {
        boolean b = value.toString().toLowerCase().endsWith(filter.toLowerCase());
        if (b)
            System.out.println("Найден файл: " + value.getAbsolutePath() + " Filter: " + filter);
        return b;
    }

    TreeItem<File> filterChanged(String filterExt, String searchWord) throws IOException {
        if (filterExt.isEmpty()) {
            treeView.setRoot(rootTreeItem);
        } else {
            TreeItem<File> filteredRoot = createTreeRoot();
            filter(rootTreeItem, filterExt, filteredRoot, searchWord);
            treeView.setRoot(filteredRoot);
        }
        return treeView.getRoot();
    }

    private TreeItem<File> createTreeRoot() {
        TreeItem<File> root = new TreeItem<>(new File(rootFolder));
        root.setExpanded(true);
        return root;
    }


}