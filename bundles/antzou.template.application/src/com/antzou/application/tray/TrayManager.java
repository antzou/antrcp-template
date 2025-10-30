package com.antzou.application.tray;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.antzou.application.common.IconLoader;

public class TrayManager {
    
    private TrayItem trayItem;
    private Shell shell;
    
    @Inject
    private IEventBroker eventBroker;
    
    /**
     * 创建系统托盘
     */
    public void createTray(Shell shell) {
        this.shell = shell;
        
        if (trayItem != null && !trayItem.isDisposed()) {
            return; // 已经存在托盘图标
        }
        
        final Display display = shell.getDisplay();
        Tray tray = display.getSystemTray();
        
        if (tray == null) {
            return;
        }
        
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setToolTipText("antrcp-Template");
        
        try {
            trayItem.setImage(IconLoader.loadIcon("icons/app32.png"));
        } catch (Exception e) {
            trayItem.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
        }
        
        // 创建托盘菜单
        createTrayMenu(shell, trayItem);
        
        // 添加双击事件 - 显示/隐藏窗口
        trayItem.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                toggleWindowVisibility();
            }
        });
        
        // 添加右键菜单事件
        trayItem.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event event) {
                // 菜单在 createTrayMenu 中已经设置
            }
        });
    }
    
    /**
     * 创建托盘右键菜单
     */
    private void createTrayMenu(final Shell shell, TrayItem trayItem) {
        final Menu menu = new Menu(shell, SWT.POP_UP);
        
        // 显示/隐藏菜单项
        MenuItem showHideItem = new MenuItem(menu, SWT.PUSH);
        showHideItem.setText("显示/隐藏");
        showHideItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                toggleWindowVisibility();
            }
        });
        
        new MenuItem(menu, SWT.SEPARATOR);
        
        // 最大化菜单项
        MenuItem maximizeItem = new MenuItem(menu, SWT.PUSH);
        maximizeItem.setText("最大化");
        maximizeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (shell != null && !shell.isDisposed()) {
                    shell.setMinimized(false);
                    shell.setMaximized(true);
                    shell.setVisible(true);
                    shell.forceActive();
                }
            }
        });
        
        // 最小化菜单项
        MenuItem minimizeItem = new MenuItem(menu, SWT.PUSH);
        minimizeItem.setText("最小化到托盘");
        minimizeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (shell != null && !shell.isDisposed()) {
                    shell.setVisible(false);
                }
            }
        });
        
        new MenuItem(menu, SWT.SEPARATOR);
        
        // 退出菜单项
        MenuItem exitItem = new MenuItem(menu, SWT.PUSH);
        exitItem.setText("退出");
        exitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                disposeTray();
                if (shell != null && !shell.isDisposed()) {
                    shell.close();
                }
            }
        });
        
        // 设置托盘菜单
        trayItem.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event event) {
                menu.setVisible(true);
            }
        });
    }
    
    /**
     * 切换窗口显示/隐藏
     */
    private void toggleWindowVisibility() {
        if (shell != null && !shell.isDisposed()) {
            boolean visible = shell.getVisible();
            shell.setVisible(!visible);
            
            if (!visible) {
                // 如果之前是隐藏的，现在显示并激活
                shell.setMinimized(false);
                shell.forceActive();
                
                // 发送事件通知其他部件窗口已显示
                if (eventBroker != null) {
                    eventBroker.post("WINDOW_VISIBILITY_CHANGED", true);
                }
            } else {
                // 发送事件通知其他部件窗口已隐藏
                if (eventBroker != null) {
                    eventBroker.post("WINDOW_VISIBILITY_CHANGED", false);
                }
            }
        }
    }
    
    /**
     * 清理托盘资源
     */
    public void disposeTray() {
        if (trayItem != null && !trayItem.isDisposed()) {
            trayItem.dispose();
            trayItem = null;
        }
    }
    
    @PreDestroy
    public void preDestroy() {
        disposeTray();
    }
    
    /**
     * 获取托盘状态
     */
    public boolean isTrayCreated() {
        return trayItem != null && !trayItem.isDisposed();
    }
}