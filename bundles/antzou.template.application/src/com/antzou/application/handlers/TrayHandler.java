package com.antzou.application.handlers;

import jakarta.inject.Named;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class TrayHandler {
    
    private static TrayItem trayItem;
    
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell) {
        if (trayItem != null && !trayItem.isDisposed()) {
            return; // 已经存在托盘图标
        }
        
        final Display display = shell.getDisplay();
        Tray tray = display.getSystemTray();
        
        if (tray == null) {
            System.out.println("系统不支持托盘");
            return;
        }
        
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setToolTipText("antrcp-Template");
        trayItem.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
        
        // 创建托盘菜单
        createTrayMenu(shell, trayItem);
        
        // 添加点击事件
        trayItem.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (shell.getMinimized()) {
                    shell.setMinimized(false);
                }
                shell.setVisible(true);
                shell.forceActive();
            }
        });
        
        // 添加窗口最小化事件监听
        shell.addListener(SWT.Iconify, new Listener() {
            @Override
            public void handleEvent(Event event) {
                shell.setVisible(false);
            }
        });
    }
    
    private void createTrayMenu(final Shell shell, TrayItem trayItem) {
        final Menu menu = new Menu(shell, SWT.POP_UP);
        
        // 显示/隐藏菜单项
        MenuItem showItem = new MenuItem(menu, SWT.PUSH);
        showItem.setText("显示/隐藏");
        showItem.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                shell.setVisible(!shell.getVisible());
                if (shell.getVisible()) {
                    shell.setMinimized(false);
                    shell.forceActive();
                }
            }
        });
        
        new MenuItem(menu, SWT.SEPARATOR);
        
        // 退出菜单项
        MenuItem exitItem = new MenuItem(menu, SWT.PUSH);
        exitItem.setText("退出");
        exitItem.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                trayItem.dispose();
                shell.close();
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
    
    @CanExecute
    public boolean canExecute() {
        return trayItem == null || trayItem.isDisposed();
    }
    
    // 清理托盘图标
    public static void disposeTray() {
        if (trayItem != null && !trayItem.isDisposed()) {
            trayItem.dispose();
            trayItem = null;
        }
    }
}