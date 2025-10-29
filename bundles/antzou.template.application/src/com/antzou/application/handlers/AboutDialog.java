package com.antzou.application.handlers;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class AboutDialog extends Dialog {
    
    private ResourceManager resourceManager;
    
    protected AboutDialog(Shell parentShell) {
        super(parentShell);
        // 创建资源管理器，自动管理资源生命周期
        resourceManager = new LocalResourceManager(JFaceResources.getResources(), parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        // 应用图标 - 使用资源管理器创建
        Image appIcon = getAppIcon();
        if (appIcon != null) {
            Label iconLabel = new Label(container, SWT.NONE);
            iconLabel.setImage(appIcon);
        }

        // 应用信息
        Composite infoComposite = new Composite(container, SWT.NONE);
        infoComposite.setLayout(new GridLayout(1, false));

        Label appName = new Label(infoComposite, SWT.NONE);
        appName.setText("antrcp-Template");
        appName.setFont(parent.getFont());

        Label version = new Label(infoComposite, SWT.NONE);
        version.setText("Version 1.0.0");

        Label copyright = new Label(infoComposite, SWT.NONE);
        copyright.setText("Copyright © 2024 antzou. All rights reserved.");

        // 添加其他信息...
        Link website = new Link(infoComposite, SWT.NONE);
        website.setText("Visit <a>https://gitee.com/antzou/antrcp-template</a>");
        website.addListener(SWT.Selection, e -> {
            openBrowser(getShell(), "https://gitee.com/antzou/antrcp-template");
        });

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("关于 antrcp-Template");
    }
    
    @Override
    public boolean close() {
        // 资源管理器会自动释放所有管理的资源
        if (resourceManager != null) {
            resourceManager.dispose();
        }
        return super.close();
    }
    
    private boolean openBrowser(Shell shell, String url) {
        try {
            boolean result = Program.launch(url);
            if (!result) {
                MessageDialog.openWarning(shell, "提示", "打开网页失败: " + url);
            }
            return result;
        } catch (Exception ex) {
            MessageDialog.openError(shell, "错误", "打开网页失败: " + ex.getMessage());
            return false;
        }
    }

    @SuppressWarnings("deprecation")
	private Image getAppIcon() {
        try {
            Bundle bundle = FrameworkUtil.getBundle(getClass());
            ImageDescriptor descriptor = ImageDescriptor.createFromURL(
                FileLocator.find(bundle, new Path("icons/app250.png"))
            );
            // 使用资源管理器创建图像，会自动管理生命周期
            return resourceManager.createImage(descriptor);
        } catch (Exception e) {
            return null;
        }
    }
}