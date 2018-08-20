package com.pltone.ui;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicPopupMenuUI;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 自定义弹出菜单
 *
 * @author chenlong
 * @version 1.0 2018-02-12
 */
public class MyPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public MyPopupMenu() {
		super();
		// 为JPopupMenu设置UI
		setUI(new BasicPopupMenuUI() {
			@Override
			public void paint(Graphics g, JComponent c) {
				super.paint(g, c);
				// 画弹出菜单左侧的灰色背景
				g.setColor(new Color(236, 237, 238));
				g.fillRect(0, 0, 25, c.getHeight());
				// 画弹出菜单右侧的白色背景
				g.setColor(new Color(255, 255, 255));
				g.fillRect(25, 0, c.getWidth() - 25, c.getHeight());
			}
		});
	}
}