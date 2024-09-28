package com.kealamakia.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
  final int orignalTileSize = 16; // 16x16 tile
  final int scale = 3;

  final int tileSize = orignalTileSize * scale; // 48x48 title
  final int maxScreenCol = 16;
  final int maxScreenRow = 12;
  final int screenWidth = tileSize * maxScreenCol; // 768 pixels
  final int screenHeight = tileSize * maxScreenRow; // 576 pixels

  int FPS = 60;

  KeyHandler keyH = new KeyHandler();
  Thread gameThread;

  int playerX = 100;
  int playerY = 100;
  int playerSpeed = 4;

  public GamePanel() {
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    this.addKeyListener(keyH);
    this.setFocusable(true);
  }

  public void startGameThread() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  @Override
  public void run() {
    double drawInterval = 1_000_000_000/FPS;
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;
    long timer = 0;
    int drawCount = 0;

    while (gameThread != null) {
      currentTime = System.nanoTime();

      delta += (currentTime - lastTime) / drawInterval;
      timer += (currentTime - lastTime);
      lastTime = currentTime;
      if (delta >= 1) {
        update();
        repaint();
        delta--;
        drawCount++;
      }
      if (timer >= 1_000_000_000) {
        System.out.println("FPS:" + drawCount);
        drawCount = 0;
        timer = 0;
      }
    }
  }

//  @Override
//  public void run() {
//    double drawInterval = 1_000_000_000/FPS;
//    double nextDrawTime = System.nanoTime() + drawInterval;
//
//    while (gameThread != null) {
//      update();
//      repaint();
//
//      try {
//        double remainingTime = nextDrawTime - System.nanoTime();
//        remainingTime = remainingTime / 1_000_000;
//
//        if (remainingTime < 0) {
//          remainingTime = 0;
//        }
//
//        Thread.sleep((long)remainingTime);
//
//        nextDrawTime += drawInterval;
//
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//    }
//  }

  public void update() {
    if (keyH.upPressed == true) {
      playerY -= playerSpeed;
    } else if (keyH.downPressed == true) {
      playerY += playerSpeed;
    } else if (keyH.leftPressed == true) {
      playerX -= playerSpeed;
    } else if (keyH.rightPressed == true) {
      playerX += playerSpeed;
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.WHITE);
    g2.fillRect(playerX, playerY, tileSize, tileSize);
    g2.dispose();
  }
}
