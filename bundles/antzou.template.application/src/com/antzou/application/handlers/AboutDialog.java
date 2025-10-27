package com.antzou.application.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ProductProperties;
import org.osgi.framework.Bundle;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class AboutDialog {

    private LocalResourceManager resourceManager;
    private static List<Image> images = new ArrayList<>();
    private StyledText textArea;
    private Shell shell;
    
    @Inject
    public AboutDialog() {
        resourceManager = new LocalResourceManager(JFaceResources.getResources());
    }

//    @PostConstruct
    public void createComposite(Shell parentShell) {
        // 创建对话框外壳
        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("关于");
        shell.setSize(700, 500);
        shell.setLayout(new GridLayout(1, false));
        
        // 创建顶部区域
        createTopArea(shell);
        
        // 创建分隔线
        createSeparator(shell);
        
        // 创建功能按钮区域
        createFeatureButtons(shell);
        
        // 创建文本区域
        createTextArea(shell);
        
        // 创建底部按钮
        createBottomButtons(shell);
        
        shell.open();
    }

    private void createTopArea(Composite parent) {
        Composite topArea = new Composite(parent, SWT.NONE);
        topArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        topArea.setLayout(new GridLayout(2, false));
        
        // 获取产品信息
        IProduct product = Platform.getProduct();
        if (product == null) {
            Label errorLabel = new Label(topArea, SWT.NONE);
            errorLabel.setText("无法获取产品信息");
            return;
        }
        
        // 获取产品图标
        Image productImage = null;
        ImageDescriptor imageDescriptor = ProductProperties.getAboutImage(product);
        if (imageDescriptor != null) {
            productImage = resourceManager.createImage(imageDescriptor);
            images.add(productImage);
        }
        
        // 添加产品图片
        if (productImage != null) {
            Label imageLabel = new Label(topArea, SWT.NONE);
            imageLabel.setImage(productImage);
            imageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        }
        
        // 添加产品文本信息
        String productName = product.getName();
        String versionInfo = ProductProperties.getAboutText(product);
        
        if (versionInfo == null || versionInfo.isEmpty()) {
            // 如果没有提供about文本，使用bundle版本
            Bundle bundle = Platform.getBundle(product.getDefiningBundle().getSymbolicName());
            if (bundle != null) {
                versionInfo = "版本 " + bundle.getVersion().toString();
            } else {
                versionInfo = "版本信息不可用";
            }
        }
        
        Composite textArea = new Composite(topArea, SWT.NONE);
        textArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        textArea.setLayout(new GridLayout(1, false));
        
        Label nameLabel = new Label(textArea, SWT.NONE);
        nameLabel.setText(productName);
        nameLabel.setFont(JFaceResources.getHeaderFont());
        
        StyledText versionText = new StyledText(textArea, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        versionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        versionText.setText(versionInfo);
        versionText.setBackground(textArea.getBackground());
    }

    private void createSeparator(Composite parent) {
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
    }

    private void createFeatureButtons(Composite parent) {
        Composite buttonArea = new Composite(parent, SWT.NONE);
        buttonArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        
        GridLayout layout = new GridLayout(5, true);
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        buttonArea.setLayout(layout);
        
        // 获取功能列表
        List<FeatureInfo> features = getFeatures();
        
        // 添加功能按钮
        for (FeatureInfo feature : features) {
            Button btn = new Button(buttonArea, SWT.PUSH);
            btn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
            btn.setImage(feature.getImage());
            btn.setToolTipText(feature.getName() + "\n" + feature.getVersion());
            btn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    openFeatureDetails(feature);
                }
            });
        }
    }

    private void createTextArea(Composite parent) {
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent, 
            SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        textArea = new StyledText(scrolledComposite, 
            SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        textArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        textArea.setText(getLicenseText());
        
        scrolledComposite.setContent(textArea);
        scrolledComposite.setMinSize(textArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private void createBottomButtons(Composite parent) {
        Composite buttonArea = new Composite(parent, SWT.NONE);
        buttonArea.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
        buttonArea.setLayout(new GridLayout(2, true));
        
        Button detailsBtn = new Button(buttonArea, SWT.PUSH);
        detailsBtn.setText("安装详细信息");
        detailsBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        detailsBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                openInstallationDetails();
            }
        });
        
        Button closeBtn = new Button(buttonArea, SWT.PUSH);
        closeBtn.setText("关闭");
        closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        closeBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        
        // 设置默认按钮
        shell.setDefaultButton(closeBtn);
    }

    private List<FeatureInfo> getFeatures() {
        List<FeatureInfo> features = new ArrayList<>();
        
        // 获取实际安装的功能
        IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();
        if (providers != null) {
            for (IBundleGroupProvider provider : providers) {
                for (IBundleGroup group : provider.getBundleGroups()) {
                    features.add(new FeatureInfo(group, resourceManager));
                }
            }
        }
        
        // 如果没有获取到任何功能，添加一些默认项
        if (features.isEmpty()) {
            features.add(new FeatureInfo("Java Development Tools", "4.30.0", resourceManager));
            features.add(new FeatureInfo("Eclipse Platform", "4.30.0", resourceManager));
            // 添加更多默认功能...
        }
        
        return features;
    }

    private String getLicenseText() {
        // 尝试从产品配置中获取许可证信息
        IProduct product = Platform.getProduct();
        if (product != null) {
            String licenseKey = "license.text"; // 假设的许可证属性键
//            String licenseText = ProductProperties.getProperty(product, licenseKey);
//            if (licenseText != null && !licenseText.isEmpty()) {
//                return licenseText;
//            }
            
            return  "";
        }
        
        // 如果没有找到许可证信息，尝试从bundle中加载
        try {
            Bundle bundle = Platform.getBundle("com.example.e4.rcp"); // 替换为您的bundle ID
            if (bundle != null) {
                URL licenseURL = bundle.getEntry("LICENSE.txt");
                if (licenseURL != null) {
                    InputStream is = licenseURL.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    reader.close();
                    return sb.toString();
                }
            }
        } catch (IOException e) {
            // 忽略异常，使用默认文本
        }
        
        // 默认许可证文本
        return "Eclipse Public License - v 2.0\n\n" +
               "THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE " +
               "PUBLIC LICENSE (\"AGREEMENT\"). ANY USE, REPRODUCTION OR DISTRIBUTION " +
               "OF THE PROGRAM CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.\n\n" +
               "This is a simplified version of the Eclipse Public License. " +
               "Please refer to the full license text for complete details.";
    }

    private void openFeatureDetails(FeatureInfo feature) {
        Shell featureShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        featureShell.setText(feature.getName() + " 详细信息");
        featureShell.setSize(500, 400);
        featureShell.setLayout(new GridLayout(1, false));
        
        Composite content = new Composite(featureShell, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setLayout(new GridLayout(2, false));
        
        Label iconLabel = new Label(content, SWT.NONE);
        iconLabel.setImage(feature.getImage());
        iconLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        
        Composite infoArea = new Composite(content, SWT.NONE);
        infoArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        infoArea.setLayout(new GridLayout(1, false));
        
        Label nameLabel = new Label(infoArea, SWT.NONE);
        nameLabel.setText(feature.getName());
        nameLabel.setFont(JFaceResources.getHeaderFont());
        
        Label versionLabel = new Label(infoArea, SWT.NONE);
        versionLabel.setText("版本: " + feature.getVersion());
        
        Label providerLabel = new Label(infoArea, SWT.NONE);
        providerLabel.setText("提供商: " + feature.getProvider());
        
        if (feature.getDescription() != null && !feature.getDescription().isEmpty()) {
            Label descLabel = new Label(infoArea, SWT.WRAP);
            descLabel.setText(feature.getDescription());
            descLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        }
        
        Button closeBtn = new Button(featureShell, SWT.PUSH);
        closeBtn.setText("关闭");
        closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
        closeBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                featureShell.close();
            }
        });
        
        featureShell.open();
    }

    private void openInstallationDetails() {
        Shell detailsShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        detailsShell.setText("安装详细信息");
        detailsShell.setSize(600, 500);
        detailsShell.setLayout(new GridLayout(1, false));
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(detailsShell, 
            SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        Composite content = new Composite(scrolledComposite, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setLayout(new GridLayout(1, false));
        
        // 添加安装详细信息
        addInstallationInfo(content);
        
        scrolledComposite.setContent(content);
        scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        Button closeBtn = new Button(detailsShell, SWT.PUSH);
        closeBtn.setText("关闭");
        closeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
        closeBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                detailsShell.close();
            }
        });
        
        detailsShell.open();
    }

    private void addInstallationInfo(Composite parent) {
        Label osLabel = new Label(parent, SWT.NONE);
        osLabel.setText("操作系统: " + System.getProperty("os.name") + " " + 
                        System.getProperty("os.version") + ", " + 
                        System.getProperty("os.arch"));
        
        Label javaLabel = new Label(parent, SWT.NONE);
        javaLabel.setText("Java 运行时: " + System.getProperty("java.version") + " (" + 
                         System.getProperty("java.vendor") + ")");
        
        Label userLabel = new Label(parent, SWT.NONE);
        userLabel.setText("用户目录: " + System.getProperty("user.home"));
        
        Label workspaceLabel = new Label(parent, SWT.NONE);
        workspaceLabel.setText("工作空间: " + System.getProperty("user.dir"));
        
        // 添加分隔线
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        
        // 添加功能列表
        Label featuresLabel = new Label(parent, SWT.NONE);
        featuresLabel.setText("已安装功能:");
        featuresLabel.setFont(JFaceResources.getBannerFont());
        
        List<FeatureInfo> features = getFeatures();
        for (FeatureInfo feature : features) {
            Composite featureComp = new Composite(parent, SWT.NONE);
            featureComp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
            featureComp.setLayout(new GridLayout(2, false));
            
            Label iconLabel = new Label(featureComp, SWT.NONE);
            iconLabel.setImage(feature.getImage());
            iconLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
            
            Composite infoComp = new Composite(featureComp, SWT.NONE);
            infoComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            infoComp.setLayout(new GridLayout(1, false));
            
            Label nameLabel = new Label(infoComp, SWT.NONE);
            nameLabel.setText(feature.getName());
            
            Label versionLabel = new Label(infoComp, SWT.NONE);
            versionLabel.setText("版本: " + feature.getVersion() + ", 提供商: " + feature.getProvider());
        }
    }

    @Focus
    public void onFocus() {
        if (textArea != null && !textArea.isDisposed()) {
            textArea.setFocus();
        }
    }

    @PreDestroy
    public void dispose() {
        // 释放所有图像资源
        for (Image image : images) {
            if (image != null && !image.isDisposed()) {
                image.dispose();
            }
        }
        resourceManager.dispose();
    }

    // 功能信息内部类
    private static class FeatureInfo {
        private String name;
        private String version;
        private String provider;
        private String description;
        private Image image;
        
        public FeatureInfo(String name, String version, LocalResourceManager rm) {
            this(name, version, "Eclipse Foundation", "功能描述信息", rm);
        }
        
        public FeatureInfo(String name, String version, String provider, String description, 
                          LocalResourceManager rm) {
            this.name = name;
            this.version = version;
            this.provider = provider;
            this.description = description;
            this.image = createDefaultImage(rm);
        }
        
        public FeatureInfo(IBundleGroup group, LocalResourceManager rm) {
            this.name = group.getName();
            this.version = group.getVersion();
            this.provider = group.getProviderName();
            this.description = group.getDescription();
            
            // 尝试获取实际图标
            String iconPath = group.getIdentifier() + ".png"; // 假设图标命名规则
            ImageDescriptor descriptor = null;
            
            // 尝试从bundle中加载图标
            Bundle bundle = Platform.getBundle(group.getIdentifier());
            if (bundle != null) {
                URL iconURL = bundle.getEntry("icons/" + iconPath);
                if (iconURL != null) {
                    descriptor = ImageDescriptor.createFromURL(iconURL);
                }
            }
            
            // 如果没有找到实际图标，创建默认图标
            if (descriptor != null) {
                this.image = rm.createImage(descriptor);
                images.add(this.image);
            } else {
                this.image = createDefaultImage(rm);
            }
        }
        
        private Image createDefaultImage(LocalResourceManager rm) {
            // 在实际应用中，这里应该从功能获取实际图像
            // 这里创建一个简单的占位图像
            Image img = new Image(Display.getDefault(), 64, 64);
            images.add(img);
            return img;
        }
        
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getProvider() { return provider; }
        public String getDescription() { return description; }
        public Image getImage() { return image; }
    }
}