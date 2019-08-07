package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.sample.petclinic.entity.vet.Vet;
import com.haulmont.sample.petclinic.entity.visit.Visit;

import java.util.function.Consumer;

public class VetPreviewComponentFactory {

    private final UiComponents uiComponents;
    private final ScreenBuilders screenBuilders;
    private final FrameOwner frameOwner;

    public VetPreviewComponentFactory(
            UiComponents uiComponents,
            ScreenBuilders screenBuilders,
            FrameOwner frameOwner
    ) {
        this.uiComponents = uiComponents;
        this.screenBuilders = screenBuilders;
        this.frameOwner = frameOwner;
    }

    public Component create(
            InstanceContainer<Visit> visitDc,
            Consumer<Vet> vetSelectionHandler
    ){
        return verticalLayout(
                vetImage(visitDc),
                horizontalLayout(
                        treatingVetName(visitDc),
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

    private Label treatingVetName(InstanceContainer<Visit> visitDc) {

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

    private Image vetImage(InstanceContainer<Visit> visitDc) {

        Image image = uiComponents.create(Image.class);

        image.setScaleMode(Image.ScaleMode.CONTAIN);
        image.setHeight("80");
        image.setWidth("80");
        image.setStyleName("avatar-icon-large");
        image.setAlignment(Component.Alignment.MIDDLE_CENTER);
        image.setValueSource(new ContainerValueSource<>(visitDc, "treatingVet.image"));

        return image;
    }
}
