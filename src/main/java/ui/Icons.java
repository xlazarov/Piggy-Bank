package ui;

import javax.swing.*;
import java.awt.*;

final class Icons {

    static final Icon DELETE_ICON = createIcon("delete-icon.png");
    static final Icon EDIT_ICON = createIcon("edit-icon.png");
    static final Icon ADD_ICON = createIcon("add-icon.png");
    static final Image PIGGY_IMAGE = createImage("/ui/app-icon.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        return new ImageIcon(MainFrame.class.getResource(name));
    }

    private static Image createImage(String name){
        return new ImageIcon(MainFrame.class.getResource(name)).getImage();
    }


}
