package com.kealamakia.main;

import com.kealamakia.entity.Player;
import com.kealamakia.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
  final int orignalTileSize = 16; // 16x16 tile
  final int scale = 3;

  public final int tileSize = orignalTileSize * scale; // 48x48 title
  public final int maxScreenCol = 16;
  public final int maxScreenRow = 12;
  public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
  public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

  // WORLD SETTINGS
  public final int maxWorldCol = 50;
  public final int maxWorldRow = 50;
  public final int worldWidth = tileSize * maxWorldCol;
  public final int worldHeight = tileSize * maxWorldRow;

  int FPS = 60;

  TileManager tileManager = new TileManager(this);
  KeyHandler keyH = new KeyHandler();
  Thread gameThread;
  public Player player = new Player(this, keyH);

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
    player.update();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    tileManager.draw(g2);
    player.draw(g2);
    g2.dispose();
  }
}
