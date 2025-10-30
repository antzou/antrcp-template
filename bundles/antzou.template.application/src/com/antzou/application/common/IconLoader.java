package com.antzou.application.common;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * 图标加载工具类
 * 提供统一的图标加载和资源管理功能
 */
public class IconLoader {
    
    private static IconLoader instance;
    private ResourceManager resourceManager;
    
    private IconLoader() {
        // 使用JFace资源管理器，自动管理资源生命周期
        this.resourceManager = new LocalResourceManager(JFaceResources.getResources());
    }
    
    /**
     * 获取单例实例
     */
    public static synchronized IconLoader getInstance() {
        if (instance == null) {
            instance = new IconLoader();
        }
        return instance;
    }
    
    /**
     * 静态方法：应用程序退出时调用
     */
    public static void disposeInstance() {
        if (instance != null) {
            instance.dispose();
        }
    }
    
    /**
     * 加载应用图标
     * @param url 图标路径（相对于bundle根目录）
     * @return 加载的Image对象，如果加载失败返回null
     */
    @SuppressWarnings("deprecation")
    public Image getAppIcon(String url) {
        try {
            Bundle bundle = FrameworkUtil.getBundle(getClass());
            ImageDescriptor descriptor = ImageDescriptor.createFromURL(
                FileLocator.find(bundle, new Path(url))
            );
            // 使用资源管理器创建图像，会自动管理生命周期
            return resourceManager.createImage(descriptor);
        } catch (Exception e) {
            System.err.println("加载图标失败: " + url + ", 错误: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 加载应用图标，支持备用图标
     * @param primaryUrl 首选图标路径
     * @param fallbackUrl 备用图标路径
     * @return 加载的Image对象，如果都加载失败返回null
     */
    public Image getAppIconWithFallback(String primaryUrl, String fallbackUrl) {
        Image image = getAppIcon(primaryUrl);
        if (image == null && fallbackUrl != null) {
            image = getAppIcon(fallbackUrl);
        }
        return image;
    }
    
    /**
     * 加载指定尺寸的图标
     * @param url 图标路径
     * @param width 期望宽度
     * @param height 期望高度
     * @return 缩放后的Image对象
     */
    public Image getAppIcon(String url, int width, int height) {
        Image original = getAppIcon(url);
        if (original == null) {
            return null;
        }
        
        // 如果尺寸匹配，直接返回
        if (original.getBounds().width == width && original.getBounds().height == height) {
            return original;
        }
        
        // 创建缩放后的图像
        return new Image(original.getDevice(), original.getImageData().scaledTo(width, height));
    }
    
    /**
     * 预加载常用图标到缓存
     */
    public void preloadIcons(String... iconUrls) {
        for (String url : iconUrls) {
            try {
                getAppIcon(url);
            } catch (Exception e) {
                System.err.println("预加载图标失败: " + url);
            }
        }
    }
    
    /**
     * 检查图标是否存在
     * @param url 图标路径
     * @return 是否存在
     */
    public boolean iconExists(String url) {
        try {
            Bundle bundle = FrameworkUtil.getBundle(getClass());
            return FileLocator.find(bundle, new Path(url)) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取资源管理器
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }
    
    /**
     * 释放所有图标资源
     */
    public void dispose() {
        if (resourceManager != null) {
            resourceManager.dispose();
            resourceManager = null;
        }
        instance = null;
    }
    
    /**
     * 静态方法：便捷加载图标
     */
    public static Image loadIcon(String url) {
        return getInstance().getAppIcon(url);
    }
    
    /**
     * 静态方法：便捷加载指定尺寸图标
     */
    public static Image loadIcon(String url, int width, int height) {
        return getInstance().getAppIcon(url, width, height);
    }
}