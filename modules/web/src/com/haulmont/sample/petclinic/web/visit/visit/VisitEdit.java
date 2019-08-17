package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
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
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.web.visit.visit.xray_image_preview.XrayImagePreview;
import com.haulmont.sample.petclinic.web.visit.visit.xray_image_preview.XrayPreviewComponentFactory;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
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

    @Inject
    private DataManager dataManager;

    @Inject
    private FileStorageService fileStorageService;

    private List<FileDescriptor> newImageDescriptors = new ArrayList<>();

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
    protected void onXRayImagesTableSelection(
            Table.SelectionEvent<FileDescriptor> event
    ) {
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

    private Component xrayImage(FileDescriptor file) {
        XrayPreviewComponentFactory factory = new XrayPreviewComponentFactory(
                uiComponents,
                messageBundle
        );

        return factory.create(file);
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
        FileDescriptor imageDescriptor = upload.getFileDescriptor();

        try {
            fileUploadingAPI.putFileIntoStorage(upload.getFileId(), imageDescriptor);
            // save file descriptor and remember it in case the user clicks "Cancel" and we need to remove it
            FileDescriptor savedImageDescriptor = dataManager.commit(imageDescriptor);
            newImageDescriptors.add(savedImageDescriptor);
            // add file descriptor to data container to show in the table
            xRayImagesDc.getMutableItems().add(savedImageDescriptor);

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

    @Override
    public OperationResult closeWithDiscard() {
        return super.closeWithDiscard().then(() -> {
            for (FileDescriptor fileDescriptor : newImageDescriptors) {
                try {
                    fileStorageService.removeFile(fileDescriptor);
                    if (xRayImagesDc.containsItem(fileDescriptor)) { // could be removed by user right after adding
                        dataManager.remove(fileDescriptor);
                    }
                } catch (FileStorageException e) {
                    logger.warn("Unable to remove file " + fileDescriptor);
                }
            }
        });
    }
}