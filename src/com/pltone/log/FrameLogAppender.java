package com.pltone.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

/**
 * 窗体日志输出器
 *
 * @author chenlong
 * @version 1.0 2018-02-12
 */
public class FrameLogAppender extends AppenderSkeleton {
	private static final String CONVERSION_PATTERN = "[%d{yyyy-MM-dd HH:mm:ss}]:%m%n";
	private static final int FRAME_LOG_SIZE = 1024 * 5;
	private Layout layout;
	private JTextArea textArea;

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public FrameLogAppender() {
		super();
		layout = new PatternLayout(CONVERSION_PATTERN);
	}

	public FrameLogAppender(Layout layout) {
		super();
		this.layout = layout;
	}

	public FrameLogAppender(JTextArea textArea) {
		this();
		this.textArea = textArea;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	public void append(LoggingEvent event) {
		StringBuffer buffer = new StringBuffer();
		String text = textArea.getText();
		// 窗体日志大于5k，清空旧日志
		if (text.length() < FRAME_LOG_SIZE) {
			buffer.append(text);
		}
		String msg = this.layout.format(event);
		buffer.append(msg);
		if (layout.ignoresThrowable()) {
			String[] s = event.getThrowableStrRep();
			if (s != null) {
				int len = s.length;
				for (int i = 0; i < len; i++) {
					buffer.append(s[i]);
					buffer.append(Layout.LINE_SEP);
				}
			}
		}
		textArea.setText(buffer.toString());
		// 在JScrollPane(JTextArea)中增加内容时，滚动条自动滚至底部
		textArea.setSelectionStart(textArea.getText().length());
	}
}