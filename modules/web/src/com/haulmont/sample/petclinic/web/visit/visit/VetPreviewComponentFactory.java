package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.MessageBundle;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.sample.petclinic.entity.vet.Vet;
import com.haulmont.sample.petclinic.entity.visit.Visit;

import java.util.function.Consumer;

public class VetPreviewComponentFactory {

    private final UiComponents uiComponents;
    private final ScreenBuilders screenBuilders;
    private final MessageBundle messageBundle;
    private final FrameOwner frameOwner;
    private InstanceContainer<Visit> visitDc;

    public VetPreviewComponentFactory(UiComponents uiComponents, ScreenBuilders screenBuilders, MessageBundle messageBundle, FrameOwner frameOwner) {
        this.uiComponents = uiComponents;
        this.screenBuilders = screenBuilders;
        this.messageBundle = messageBundle;
        this.frameOwner = frameOwner;
    }


    public Component create(InstanceContainer<Visit> visitDc, Consumer<Vet> vetSelectionHandler){
        this.visitDc = visitDc;

        return verticalLayout(
                vetImage(),
                horizontalLayout(
                        treatingVetName(),
                        editVetButton(vetSelectionHandler)
                )
        );
    }


    private HBoxLayout horizontalLayout(Component... childComponents) {
        HBoxLayout layout = uiComponents.create(HBoxLayout.class);
        layout.setAlignment(Component.Alignment.MIDDLE_CENTER);
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.add(childComponents);
        return layout;
    }

    private VBoxLayout verticalLayout(Component... childComponents) {
        VBoxLayout layout = uiComponents.create(VBoxLayout.class);
        layout.setAlignment(Component.Alignment.BOTTOM_CENTER);
        layout.add(childComponents);
        layout.setWidthFull();
        return layout;
    }

    private Label treatingVetName() {
        Label treatingVetlabel = uiComponents.create(Label.class);
        treatingVetlabel.setValueSource(new ContainerValueSource<Visit, Vet>(visitDc, "treatingVet"));
        treatingVetlabel.setAlignment(Component.Alignment.MIDDLE_CENTER);
        treatingVetlabel.setWidthFull();
        treatingVetlabel.setStyleName("h1");
        return treatingVetlabel;
    }

    private Button editVetButton(Consumer<Vet> vetSelectionHandler) {

        LinkButton button = uiComponents.create(LinkButton.class);
        button.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        button.setIconFromSet(CubaIcon.EDIT_ACTION);
        button.setStyleName("borderless huge");
        button.setAction(new BaseAction("changeVet").withHandler(event -> openVetLookup(event, vetSelectionHandler)));

        return button;
    }

    private void openVetLookup(Action.ActionPerformedEvent event, Consumer<Vet> vetSelectionHandler) {
        screenBuilders.lookup(Vet.class, frameOwner)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(vets -> vetSelectionHandler.accept(vets.iterator().next()))
                .show();
    }

    private Image vetImage() {

        Image image = uiComponents.create(Image.class);
        image.setScaleMode(Image.ScaleMode.CONTAIN);
        image.setHeight("80");
        image.setWidth("80");
        image.setStyleName("avatar-icon-large");
        image.setAlignment(Component.Alignment.MIDDLE_CENTER);
        image.setValueSource(new ContainerValueSource<>(visitDc, "treatingVet.image"));

        return image;
    }

    private Component xrayImage(FileDescriptor file) {
        GroupBoxLayout groupBoxLayout = uiComponents.create(GroupBoxLayout.class);

        groupBoxLayout.setShowAsPanel(true);
        groupBoxLayout.setWidthFull();
        groupBoxLayout.setHeightFull();
        groupBoxLayout.setStyleName("well");
        groupBoxLayout.setCaption(messageBundle.formatMessage("previewFile", file.getName()));

        if (isPdf(file)) {
            Component xrayImageComponent = xrayPdfComponent(file);
            groupBoxLayout.add(xrayImageComponent);

        }
        else if (isImage(file)){
            Component xrayImageComponent = xrayImageComponent(file);
            groupBoxLayout.add(xrayImageComponent);
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
                .setMimeType("application/pdf");
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
