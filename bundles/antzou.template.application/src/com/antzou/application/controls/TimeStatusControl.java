package com.antzou.application.controls;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

public class TimeStatusControl {
    
    private Label timeLabel;
    // 修改时间格式为 yyyy-mm-dd HH:mi:ss
    private SimpleDateFormat timeFormatWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Inject
    public TimeStatusControl() {
        // Constructor for DI
    }
    
    @PostConstruct
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        
        timeLabel = new Label(composite, SWT.RIGHT);
        timeLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // 更新时间显示
        updateTimeDisplay();
        parent.getDisplay().timerExec(1000, new Runnable() {
            @Override
            public void run() {
                if (!composite.isDisposed()) {
                    updateTimeDisplay();
                    parent.getDisplay().timerExec(1000, this);
                }
            }
        });
    }
    
    private void updateTimeDisplay() {
        if (timeLabel.isDisposed()) {
            return;
        }
        
        String timeText = timeFormatWithSeconds.format(new Date());
        timeLabel.setText(timeText);
    }
    
    /**
     * 设置是否显示秒数
     */
    public void setShowSeconds(boolean showSeconds) {
        if (!timeLabel.isDisposed()) {
            updateTimeDisplay();
        }
    }
    
    /**
     * 设置时间显示格式
     */
    public void setTimeFormat(String formatWithSeconds, String formatWithoutSeconds) {
        this.timeFormatWithSeconds = new SimpleDateFormat(formatWithSeconds);
        if (!timeLabel.isDisposed()) {
            updateTimeDisplay();
        }
    }
}