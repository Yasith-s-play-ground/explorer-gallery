package lk.ijse.dep12.se.io;

import javafx.scene.control.TreeItem;

import java.nio.file.Path;

public class GalleryTreeItem<T> extends TreeItem {
    @Override
    public String toString() {
        return ((Path) this.getValue()).getFileName() + "";
    }

}
