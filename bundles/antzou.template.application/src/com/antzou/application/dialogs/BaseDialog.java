package com.antzou.application.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.antzou.application.common.IconLoader;


public class BaseDialog extends Dialog {
    
    protected BaseDialog(Shell parentShell) {
        super(parentShell);
        setDefaultImage(IconLoader.loadIcon("icons/app32.png"));
    }
    
    /**
     * 便捷方法
     */
    protected Image getAppIcon(String url) {
        return IconLoader.loadIcon(url);
    }
}