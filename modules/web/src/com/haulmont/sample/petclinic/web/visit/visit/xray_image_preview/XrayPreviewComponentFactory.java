package com.haulmont.sample.petclinic.web.visit.visit.xray_image_preview;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.MessageBundle;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

public class XrayPreviewComponentFactory {

    private final UiComponents uiComponents;
    private final MessageBundle messageBundle;

    public XrayPreviewComponentFactory(UiComponents uiComponents, MessageBundle messageBundle) {
        this.uiComponents = uiComponents;
        this.messageBundle = messageBundle;
    }


    public Component create(FileDescriptor file) {
        GroupBoxLayout groupBoxLayout = uiComponents.create(GroupBoxLayout.class);

        groupBoxLayout.setShowAsPanel(true);
        groupBoxLayout.setWidthFull();
        groupBoxLayout.setHeightFull();
        groupBoxLayout.setStyleName("well");
        groupBoxLayout.setCaption(messageBundle.formatMessage("previewFile", file.getName()));

        if (isPdf(file)) {
            groupBoxLayout.add(xrayPdfComponent(file));
        }
        else if (isImage(file)){
            groupBoxLayout.add(xrayImageComponent(file));
        }

        return groupBoxLayout;
    }

    private boolean isPdf(FileDescriptor file) {
        return file.getExtension().contains("pdf");
    }

    private boolean isImage(FileDescriptor imageFile) {
        return imageFile.getExtension().contains("png")
                || imageFile.getExtension().contains("jpg")
                || imageFile.getExtension().contains("jpeg");
    }

    private Component xrayPdfComponent(FileDescriptor imageFile) {
        BrowserFrame browserFrame = uiComponents.create(BrowserFrame.class);
        browserFrame.setAlignment(Component.Alignment.MIDDLE_CENTER);
        browserFrame.setWidthFull();
        browserFrame.setHeightFull();
        browserFrame.setSource(FileDescriptorResource.class)
                .setFileDescriptor(imageFile)
                .setMimeType(MediaType.APPLICATION_PDF_VALUE);
        return browserFrame;
    }

    private Component xrayImageComponent(FileDescriptor imageFile) {
        Image image = uiComponents.create(Image.class);
        image.setScaleMode(Image.ScaleMode.SCALE_DOWN);
        image.setAlignment(Component.Alignment.MIDDLE_CENTER);
        image.setWidthFull();
        image.setHeightFull();

        image.setSource(FileDescriptorResource.class)
                .setFileDescriptor(imageFile);

        return image;
    }

}
