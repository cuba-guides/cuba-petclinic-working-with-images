package com.haulmont.sample.petclinic.web.vet.vet;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.sample.petclinic.entity.vet.Vet;

import javax.inject.Inject;

@UiController("petclinic_Vet.browse")
@UiDescriptor("vet-browse.xml")
@LookupComponent("vetsTable")
@LoadDataBeforeShow
public class VetBrowse extends StandardLookup<Vet> {

    @Inject
    protected GroupTable<Vet> vetsTable;

    @Inject
    protected UiComponents uiComponents;

    @Subscribe
    protected void onInit(InitEvent event) {
        vetsTable.addGeneratedColumn(
                "image",
                this::renderAvatarImageComponent
        );
    }

    private Component renderAvatarImageComponent(Vet vet) {
        FileDescriptor imageFile = vet.getImage();

        if (imageFile == null) {
            return null;
        }

        Image image = smallAvatarImage();
        image.setSource(FileDescriptorResource.class)
                .setFileDescriptor(imageFile);

        return image;
    }

    private Image smallAvatarImage() {
        Image image = uiComponents.create(Image.class);
        image.setScaleMode(Image.ScaleMode.CONTAIN);
        image.setHeight("40");
        image.setWidth("40");
        image.setStyleName("avatar-icon-small");
        return image;
    }

}