package com.pltone.ui;

import javax.swing.JTextField;
import javax.swing.text.Document;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 自定义数字输入框
 *
 * @author chenlong
 * @version 1.0 2018-08-17
 */
public class MyNumberField extends JTextField {
    private static final long serialVersionUID = 1L;

    public MyNumberField() {
        super();
        this.addKeyListener(getNumberKeyListener());
    }

    public MyNumberField(Document doc, String text, int columns) {
        super(doc, text, columns);
        this.addKeyListener(getNumberKeyListener());
    }

    public MyNumberField(int columns) {
        super(columns);
        this.addKeyListener(getNumberKeyListener());
    }

    public MyNumberField(String text, int columns) {
        super(text, columns);
        this.addKeyListener(getNumberKeyListener());
    }

    public MyNumberField(String text) {
        super(text);
        this.addKeyListener(getNumberKeyListener());
    }

    /**
     * 获取数字键监听事件
     *
     * @return {@link KeyListener}
     */
    private KeyListener getNumberKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // 不能重写keyPressed方法，否则删除等操作键无效
                // 如果你取得字符不是数字字符就取消事件
                // 思路:先获取字符,判断字符,取消事件
                // public void consume():使用此事件，以便不会按照默认的方式由产生此事件的源代码来处理此事件。
                char ch = e.getKeyChar();
                if (ch < '0' || ch > '9') {
                    e.consume();
                }
            }
        };
    }

    /**
     * 设置数值
     *
     * @param number {@link Integer}
     */
    public void setNumber(int number) {
        this.setText(Integer.toString(number));
    }

    /**
     * 获取数值
     *
     * @param defaultValue {@link Integer} 默认值
     * @return {@link Integer}
     */
    public int getNumber(int defaultValue) {
        String text = this.getText();
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(text, 10);
    }
}
