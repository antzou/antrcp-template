package com.antzou.application.dialogs;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class BaseDialog extends Dialog {
    
    protected ResourceManager resourceManager;
    
    protected BaseDialog(Shell parentShell) {
        super(parentShell);
        // 创建资源管理器，自动管理资源生命周期
        resourceManager = new LocalResourceManager(JFaceResources.getResources(), parentShell);
    
        setDefaultImage(getAppIcon("icons/app32.png"));
    }

    @Override
    public boolean close() {
        // 资源管理器会自动释放所有管理的资源
        if (resourceManager != null) {
            resourceManager.dispose();
        }
        return super.close();
    }
    
    @SuppressWarnings("deprecation")
	protected Image getAppIcon(String url) {
        try {
            Bundle bundle = FrameworkUtil.getBundle(getClass());
            ImageDescriptor descriptor = ImageDescriptor.createFromURL(
                FileLocator.find(bundle, new Path(url))
            );
            // 使用资源管理器创建图像，会自动管理生命周期
            return resourceManager.createImage(descriptor);
        } catch (Exception e) {
            return null;
        }
    }
}