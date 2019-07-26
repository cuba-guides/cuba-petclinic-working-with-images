package com.haulmont.sample.petclinic.web.visit.visit.xray_image_preview;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.components.FileDescriptorResource;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("petclinic_XrayImagePreview")
@UiDescriptor("xray-image-preview.xml")
@LoadDataBeforeShow
@EditedEntityContainer("imageDc")
public class XrayImagePreview extends StandardEditor<FileDescriptor> {

    @Inject
    protected Image image;

    @Inject
    protected InstanceContainer<FileDescriptor> imageDc;

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {

        image.setSource(FileDescriptorResource.class)
                .setFileDescriptor(imageDc.getItem());
    }
    
    
}