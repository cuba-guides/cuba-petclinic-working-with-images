package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.vet.Vet;
import com.haulmont.sample.petclinic.entity.visit.Visit;

import javax.inject.Inject;


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

    @Subscribe
    protected void renderTreatingVetLayout(AfterShowEvent event) {

        treatingVetForm.add(
                verticalLayout(
                                vetImage(),
                        horizontalLayout(
                                treatingVetName(),
                                editVetButton()
                        )
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

    private Button editVetButton() {

        LinkButton button = uiComponents.create(LinkButton.class);
        button.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        button.setIconFromSet(CubaIcon.EDIT_ACTION);
        button.setStyleName("borderless huge");
        button.setAction(new BaseAction("changeVet").withHandler(this::openVetLookup));

        return button;
    }

    private void openVetLookup(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(Vet.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(vets -> getEditedEntity().setTreatingVet(vets.iterator().next()))
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


}