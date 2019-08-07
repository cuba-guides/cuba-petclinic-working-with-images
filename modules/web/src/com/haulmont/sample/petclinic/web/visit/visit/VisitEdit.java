package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.web.visit.visit.xray_image_preview.XrayImagePreview;
import com.haulmont.sample.petclinic.web.visit.visit.xray_image_preview.XrayPreviewComponentFactory;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Set;


@UiController("petclinic_Visit.edit")
@UiDescriptor("visit-edit.xml")
@EditedEntityContainer("visitDc")
@LoadDataBeforeShow
public class VisitEdit extends StandardEditor<Visit> {

    @Inject
    protected Form treatingVetForm;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected InstanceContainer<Visit> visitDc;

    @Inject
    protected ScreenBuilders screenBuilders;

    @Inject
    protected CollectionPropertyContainer<FileDescriptor> xRayImagesDc;

    @Inject
    protected Logger logger;

    @Inject
    protected HBoxLayout xrayImageWrapperLayout;

    @Inject
    protected FileUploadField upload;

    @Inject
    protected DataContext dataContext;

    @Inject
    protected FileUploadingAPI fileUploadingAPI;

    @Inject
    protected Notifications notifications;

    @Inject
    protected MessageBundle messageBundle;

    @Inject
    protected Table<FileDescriptor> xRayImagesTable;

    @Inject
    protected ExportDisplay exportDisplay;


    @Subscribe
    protected void renderTreatingVetLayout(AfterShowEvent event) {

        VetPreviewComponentFactory vetPreviewComponentFactory = new VetPreviewComponentFactory(
                uiComponents,
                screenBuilders,
                this
        );

        Component vetPreview = vetPreviewComponentFactory.create(
                visitDc,
                vet -> getEditedEntity().setTreatingVet(vet)
        );

        treatingVetForm.add(vetPreview);
    }

    @Subscribe("xRayImagesTable")
    protected void onXRayImagesTableSelection(Table.SelectionEvent<FileDescriptor> event) {
        xrayImageWrapperLayout.removeAll();
        Set<FileDescriptor> selectedXrayImages = event.getSelected();

        if (!selectedXrayImages.isEmpty()) {
            xrayImageWrapperLayout.add(
                    xrayImage(
                            selectedXrayImages.iterator().next()
                    )
            );
        }
    }

    @Subscribe("xRayImagesTable.edit")
    protected void onXRayImagesTableEdit(Action.ActionPerformedEvent event) {
        screenBuilders.editor(FileDescriptor.class, this)
                .editEntity(xRayImagesTable.getSingleSelected())
                .withScreenClass(XrayImagePreview.class)
                .withOpenMode(OpenMode.DIALOG)
                .show();
    }

    @Subscribe("xRayImagesTable.download")
    protected void onXRayImagesTableDownload(Action.ActionPerformedEvent event) {
        downloadFile(xRayImagesTable.getSingleSelected());
    }

    private void downloadFile(FileDescriptor file) {
        exportDisplay.show(file, ExportFormat.OCTET_STREAM);
    }


    @Subscribe("upload")
    protected void onUploadFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        FileDescriptor imageFile = upload.getFileDescriptor();
        logger.error("" + imageFile);


        try {
            fileUploadingAPI.putFileIntoStorage(upload.getFileId(), imageFile);
            dataContext.merge(imageFile);
            xRayImagesDc.getMutableItems().add(imageFile);
            dataContext.commit();

            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption(messageBundle.getMessage("xrayImageStoredSuccessfully"))
                    .show();
        } catch (FileStorageException e) {
            String failedMessage = messageBundle.getMessage("xrayImageStorageFailed");
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption(failedMessage)
                    .show();

            logger.error(failedMessage, e);
        }
    }

    private Component xrayImage(FileDescriptor file) {
        XrayPreviewComponentFactory xrayPreviewComponentFactory = new XrayPreviewComponentFactory(
                uiComponents,
                messageBundle
        );

        return xrayPreviewComponentFactory.create(file);
    }
}