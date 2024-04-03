package lk.ijse.dep12.se.io.controller;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import lk.ijse.dep12.se.io.GalleryTreeItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class GalleryViewController {
    public TreeView treeView;
    public TilePane tilePane;

    public void initialize() throws IOException {


        Path rootPath = FileSystems.getDefault().getRootDirectories().iterator().next();
        GalleryTreeItem<Path> rootItem = new GalleryTreeItem<>();

        rootPath = Paths.get(System.getenv("HOME"));

        rootItem.setValue(rootPath);
        treeView.setRoot(rootItem);

        addTreeItems(rootPath, rootItem);

    }

    private void addTreeItems(Path parent, GalleryTreeItem<Path> parentTreeItem) throws IOException {
        DirectoryStream<Path> paths = Files.newDirectoryStream(parent);
        for (Path path : paths) {
            //to create tree items for non-hidden files
            if (Files.isDirectory(path) && !(path.getFileName() + "").startsWith(".")) {
                GalleryTreeItem<Path> branch = new GalleryTreeItem<>();
                branch.setValue(path); // set file name as value of tree item
                parentTreeItem.getChildren().add(branch);
                System.out.println(((Path) branch.getValue()).getFileName());
                addTreeItems(path, branch);
            }
        }
    }

    public void treeViewOnContextMenuRequested(ContextMenuEvent contextMenuEvent) {

    }

    public void treeViewOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        tilePane.getChildren().clear();
        GalleryTreeItem selectedTreeItem = (GalleryTreeItem) treeView.getSelectionModel().getSelectedItem();
        if (selectedTreeItem != null) {
            Path folderPath = (Path) selectedTreeItem.getValue();
            for (Path filePath : Files.newDirectoryStream(folderPath)) {
                if (Files.isRegularFile(filePath) && isImage(filePath)) {
                    ImageView imageView = new ImageView(filePath.toUri().toString());// give path as a URL
                    imageView.setFitWidth(200); // set width of image view
                    imageView.setPreserveRatio(true);
                    tilePane.getChildren().add(imageView); // add image view to tile pane

                    imageView.setOnMouseClicked(mouseEvent1 -> {
                        if (mouseEvent1.getButton().equals(MouseButton.SECONDARY)) {
                            final ContextMenu contextMenu = new ContextMenu(); // create context menu
                            final MenuItem item1 = new MenuItem("cut");
                            final MenuItem item2 = new MenuItem("copy");
                            final MenuItem item3 = new MenuItem("paste");
                            final MenuItem item4 = new MenuItem("delete");

                            contextMenu.getItems().addAll(item1, item2, item3, item4);

                            //show context menu
                            contextMenu.show(imageView, mouseEvent1.getScreenX(), mouseEvent1.getScreenY());
                        }
                    });

//                    if (!tilePane.isVisible()) {
//                        //scrollPaneGallery.setVisible(true);
//                        tilePane.setVisible(true); // show tile pane if not visible
//                        // notFoundContainer.setVisible(false); // hide stack pane ( not found images )
//                    }
                }
            }
        }
    }

    private boolean isImage(Path path) {
        String[] imageExtensions = {".jpg", ".jpeg", ".gif", ".png"};
        for (String imageExtension : imageExtensions) {
            if ((path.getFileName() + "").endsWith(imageExtension)) return true;
        }
        return false;
    }
}
