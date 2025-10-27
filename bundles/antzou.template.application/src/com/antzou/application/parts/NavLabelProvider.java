package com.antzou.application.parts;

import org.eclipse.jface.viewers.LabelProvider;

public class NavLabelProvider extends LabelProvider {
    @Override
    public String getText(Object element) {
        return element.toString();
    }
}