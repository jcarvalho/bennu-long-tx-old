package pt.ist.bennu.longtx.presentationTier.component;

import java.util.Map;

import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@EmbeddedComponent(path = { "LongTransactionManager" })
public class LongTransactionManager extends CustomComponent implements EmbeddedComponentContainer {

    private static final long serialVersionUID = 2667099682256032726L;

    private final Layout layout = new VerticalLayout();

    @Override
    public void attach() {
        super.attach();
        layout.setSizeFull();
        setCompositionRoot(layout);
        createForm();
    }

    private void createForm() {

    }

    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
        return true;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {

    }

}
