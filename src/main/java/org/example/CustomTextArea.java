package org.example;

import javax.swing.*;
import java.awt.*;

public class CustomTextArea extends JTextArea {
    private Image background;

    public CustomTextArea(Image backgroundImage) {
        this.background = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
}
