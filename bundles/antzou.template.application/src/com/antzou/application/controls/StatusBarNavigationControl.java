package com.antzou.application.controls;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

public class StatusBarNavigationControl {
    
    private Label statusLabel;
    
    @Inject
    private IEventBroker eventBroker; // 事件总线（Event Broker）的方式，这是Eclipse E4中最优雅的松耦合通信方式
    
    @PostConstruct
    public void createControl(Composite parent) {
    	parent.setLayout(new GridLayout(3, false));
    	
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        
        statusLabel = new Label(composite, SWT.LEFT);
        statusLabel.setText("就绪"); // 默认文本
        statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // 订阅导航事件 - 使用 EventHandler 接口
        eventBroker.subscribe("NAVIGATION/STATUS", new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                if (!statusLabel.isDisposed()) {
                    String message = (String) event.getProperty(IEventBroker.DATA);
                    statusLabel.getDisplay().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            if (!statusLabel.isDisposed()) {
                                statusLabel.setText(message);
                            }
                        }
                    });
                }
            }
        });
    }
    
    /**
     * 设置状态文本
     */
    public void setStatus(String status) {
        if (statusLabel != null && !statusLabel.isDisposed()) {
            statusLabel.setText(status);
        }
    }
}