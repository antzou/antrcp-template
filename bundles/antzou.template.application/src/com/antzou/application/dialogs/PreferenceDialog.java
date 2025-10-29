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
        System.out.println("创建主题选择区域");
        
        // 主题选择标签
        Label themeLabel = new Label(parent, SWT.NONE);
        themeLabel.setText("主题:");
        
        // 主题选择下拉框
        themeCombo = new Combo(parent, SWT.READ_ONLY);
        themeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        // 填充主题列表
        List<ThemeInfo> themes = themeManager.getAvailableThemes();
        System.out.println("填充主题列表，数量: " + themes.size());
        themeCombo.add("");
        themeCombo.setData("", "");
        for (ThemeInfo theme : themes) {
            themeCombo.add(theme.getName());
            themeCombo.setData(theme.getName(), theme.getId());
        }
        
        // 设置当前选中主题
        String currentTheme = themeManager.getCurrentTheme();
        System.out.println("当前主题: " + currentTheme);
        
        for (ThemeInfo theme : themes) {
            if (theme.getId().equals(currentTheme)) {
                themeCombo.setText(theme.getName());
                selectedTheme = currentTheme;
                break;
            }
        }
        
        // 监听主题选择变化
        themeCombo.addListener(SWT.Selection, event -> {
            String themeName = themeCombo.getText();
            String themeId = (String) themeCombo.getData(themeName);
            if (themeId != null && !themeId.equals(selectedTheme)) {
                selectedTheme = themeId;
            }
        });
        
        // 添加分隔线
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData sepData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        sepData.heightHint = 10;
        separator.setLayoutData(sepData);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // 创建应用按钮（自定义，不是标准的OK按钮）
        Button applyButton = createButton(parent, SWT.PUSH, "应用", false);
        applyButton.addListener(SWT.Selection, event -> {
            applyTheme();
        });
        
        // 创建关闭按钮
        Button closeButton = createButton(parent, SWT.PUSH, "关闭", true);
        closeButton.addListener(SWT.Selection, event -> {
            close();
        });
        
        // 设置焦点到关闭按钮
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
    	
        System.out.println("准备切换主题: " + selectedTheme);
        themeManager.setTheme(selectedTheme);
        // 可以在这里添加成功提示
        
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