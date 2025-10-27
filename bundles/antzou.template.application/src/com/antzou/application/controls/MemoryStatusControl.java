package com.antzou.application.controls;

import jakarta.inject.Inject; // 改为Jakarta EE包名
import jakarta.annotation.PostConstruct; // 改为Jakarta注解

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

public class MemoryStatusControl {
    
    private Label memoryLabel;
    private ProgressBar memoryBar;
	private Color a;
	private Color b;
    
    @Inject
    public MemoryStatusControl() {
        // Constructor for DI
    }
    
    @PostConstruct
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 3;
        layout.marginHeight = 1;
        composite.setLayout(layout);
        
        memoryBar = new ProgressBar(composite, SWT.SMOOTH);
        memoryBar.setMinimum(0);
        memoryBar.setMaximum(100);
        memoryBar.setLayoutData(new GridData(80, SWT.DEFAULT));
        
        memoryLabel = new Label(composite, SWT.NONE);
        memoryLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        memoryLabel.setToolTipText("Click to run garbage collection");
        
        // Update memory display periodically
        parent.getDisplay().timerExec(2000, new Runnable() {
            @Override
            public void run() {
                if (!composite.isDisposed()) {
                    updateMemoryDisplay();
                    parent.getDisplay().timerExec(2000, this);
                }
            }
        });
        
        // Add click handler to run GC
        composite.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                System.gc();
                updateMemoryDisplay();
            }
        });
        
        a = parent.getDisplay().getSystemColor(SWT.COLOR_RED);
        b = parent.getDisplay().getSystemColor(SWT.COLOR_BLUE);
    }
    
    private void updateMemoryDisplay() {
        if (memoryLabel.isDisposed() || memoryBar.isDisposed()) {
            return;
        }
        
        Runtime runtime = Runtime.getRuntime();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = total - free;
        int percent = (int) ((used * 100) / total);
        
        if (percent > 90) {
            memoryBar.setForeground(a);
        } else {
            memoryBar.setForeground(b);
        }
        
        memoryBar.setSelection(percent);
        memoryLabel.setText(String.format("%,dM / %,dM", 
            used / (1024 * 1024), 
            total / (1024 * 1024)));
    }
}