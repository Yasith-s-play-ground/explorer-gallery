package lk.ijse.dep12.se.io.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
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
        TreeItem<String> rootNode = new TreeItem<>("This PC");
        treeView.setRoot(rootNode);
        rootNode.setGraphic(getIcon("pc"));
        Path rootPath = FileSystems.getDefault().getRootDirectories().iterator().next();
        System.out.println(rootPath);

        rootPath = Paths.get(System.getenv("HOME"));

        rootNode.setExpanded(true);

        for (Path disk : FileSystems.getDefault().getRootDirectories()) {
            TreeItem<String> diskNode = new TreeItem<>(disk.getFileName().toString());
            diskNode.setGraphic(getIcon("disk"));
            rootNode.getChildren().add(diskNode);
        }

        //addTreeItems(rootPath, rootItem);
    }

    private ImageView getIcon(String icon) {
        ImageView imageView = new ImageView(switch (icon) {
            case "pc" -> "/icon/computer.png";
            case "disk" -> "/icon/harddisk.png";
            case "folder" -> "/icon/folder-filled.png";
            case "folder-open" -> "/icon/open-folder.png";
            case null, default -> throw new RuntimeException("Invalid Icon");
        });
        imageView.setFitWidth(24);
        imageView.setPreserveRatio(true);
        return imageView;
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

    private void loadPhotosOfFolder() {
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
