package com.antzou.application.parts;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class NavContentProvider implements ITreeContentProvider {
    @Override
    public Object[] getElements(Object input) {
        return (Object[]) input;
    }

    @Override
    public Object[] getChildren(Object parent) {
        return new Object[0]; // 无子项
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return false;
    }
}