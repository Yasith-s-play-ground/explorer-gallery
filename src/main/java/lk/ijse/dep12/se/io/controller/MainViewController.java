package lk.ijse.dep12.se.io.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class MainViewController {
    public TreeView treeView;
    public TilePane tilePane;
    private ArrayList<Path> treeItemPaths = new ArrayList<>();

    public void initialize() throws IOException {
        TreeItem<String> rootItem = new TreeItem<>("Root");
        treeView.setRoot(rootItem);

        Path rootPath = FileSystems.getDefault().getRootDirectories().iterator().next();
        System.out.println(rootPath);

        rootPath = Paths.get(System.getenv("HOME"));

        addTreeItems(rootPath, rootItem);
    }

    private void addTreeItems(Path parent, TreeItem<String> parentTreeItem) throws IOException {
        DirectoryStream<Path> paths = Files.newDirectoryStream(parent);
        for (Path path : paths) {
            //to create tree items for non-hidden files
            if (Files.isDirectory(path) && !(path.getFileName() + "").startsWith(".")) {
                TreeItem<String> branch = new TreeItem<>(path.getFileName() + "");
                // TreeItem<Path> branch = new TreeItem<>(path);
                treeItemPaths.add(path);
                parentTreeItem.getChildren().add(branch);
                addTreeItems(path, branch);
            }
        }
    }

    private void loadPhotosOfFolder(){
        Path folderPath = null;
        TreeItem selectedTreeItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        String value = (String) selectedTreeItem.getValue();
        for (Path treeItemPath : treeItemPaths) {
            if (treeItemPath.getFileName().equals(value)) {
                folderPath = treeItemPath;
                break;
            }
        }
    }
    public void treeViewOnContextMenuRequested(ContextMenuEvent contextMenuEvent) {
        loadPhotosOfFolder();
    }

    public void treeViewOnMouseClicked(MouseEvent mouseEvent) {
        loadPhotosOfFolder();
    }
}
