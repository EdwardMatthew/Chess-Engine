package com.company;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class UserInterface extends JPanel implements MouseInputListener {
    static int x = 0, y = 0;
    public UserInterface() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.YELLOW);
        g.setColor(Color.BLUE);
        g.fillRect(x - 20, y-20, 40, 40);
        g.setColor(Color.YELLOW);
        g.fillRect(40, 20, 80, 80);
        Image chessPiece = new ImageIcon("ChessPieces.png").getImage();
        g.drawImage(chessPiece, x, y, x+45, y+45, 0, 0, 45, 45, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
