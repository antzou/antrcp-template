package com.antzou.application.dialogs;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.antzou.application.theme.ThemeInfo;
import com.antzou.application.theme.ThemeManager;

public class PreferenceDialog extends BaseDialog {
    
    private ThemeManager themeManager;
    private Combo themeCombo;
    private String selectedTheme;
    
    public PreferenceDialog(Shell parentShell, ThemeManager themeManager) {
        super(parentShell);
        this.themeManager = themeManager;
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(2, false));
        
        createThemeSection(container);
        
        return area;
    }
    
    private void createThemeSection(Composite parent) {
        Label themeLabel = new Label(parent, SWT.NONE);
        themeLabel.setText("主题:");
        
        themeCombo = new Combo(parent, SWT.READ_ONLY);
        themeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        List<ThemeInfo> themes = themeManager.getAvailableThemes();
        for (ThemeInfo theme : themes) {
            themeCombo.add(theme.getName());
            themeCombo.setData(theme.getName(), theme.getId());
        }
        
        String currentTheme = themeManager.getCurrentTheme();
        for (ThemeInfo theme : themes) {
            if (theme.getId().equals(currentTheme)) {
                themeCombo.setText(theme.getName());
                selectedTheme = currentTheme;
                break;
            }
        }
        
        themeCombo.addListener(SWT.Selection, event -> {
            String themeName = themeCombo.getText();
            String themeId = (String) themeCombo.getData(themeName);
            if (themeId != null && !themeId.equals(selectedTheme)) {
                selectedTheme = themeId;
            }
        });
        
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData sepData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        sepData.heightHint = 10;
        separator.setLayoutData(sepData);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        Button applyButton = createButton(parent, SWT.PUSH, "应用", false);
        applyButton.addListener(SWT.Selection, event -> {
            applyTheme();
        });
        
        Button closeButton = createButton(parent, SWT.PUSH, "关闭", true);
        closeButton.addListener(SWT.Selection, event -> {
            close();
        });
        
        closeButton.setFocus();
    }
    
    /**
     * 应用主题设置
     */
    private void applyTheme() {
    	if (selectedTheme == null || "".equals(selectedTheme) || selectedTheme.equals(themeManager.getCurrentTheme())) {
    		MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK);
            messageBox.setText("警告");
            messageBox.setMessage("请切换主题");
            messageBox.open();
            return;
    	}
    	
        themeManager.setTheme(selectedTheme);
        MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK);
        messageBox.setText("成功");
        messageBox.setMessage("主题切换成功");
        messageBox.open();
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("首选项");
    }
}