package pt.ist.bennu.longtx.presentationTier.component;

import java.util.Map;

import module.vaadin.ui.BennuTheme;
import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.bennu.longtx.domain.LongTransactionUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.longtx.TransactionalContext;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@EmbeddedComponent(path = { "LongTransactionManager" })
public class LongTransactionManager extends CustomComponent implements EmbeddedComponentContainer {

    private static final long serialVersionUID = 2667099682256032726L;

    private final Layout layout = new VerticalLayout();

    private final static String BUNDLE_NAME = "resources/LongTxResources";
    private final DomainContainer<TransactionalContext> container;

    public LongTransactionManager() {
        container = new DomainContainer<>(Authenticate.getCurrentUser().getOngoingTransactions(), TransactionalContext.class);
        container.setContainerProperties(new String[] { "name" });
    }

    @Override
    public void attach() {
        super.attach();
        layout.setSizeFull();
        setCompositionRoot(layout);
        createForm();

        createTable();
    }

    @SuppressWarnings("unchecked")
    private class CreateNewTransactionButton extends Button implements ClickListener {

        private static final long serialVersionUID = 1L;

        private CreateNewTransactionButton() {
            super("Create");
            final ClickListener clickListener = this;
            addListener(clickListener);
        }

        @Override
        public void buttonClick(final ClickEvent event) {
            TransactionalContext transaction = LongTransactionUtils.createNewTransactionForUser(nameField.getValue().toString());
            getWindow().showNotification("Created '" + transaction + "'");
            container.addItem(transaction);
        }
    }

    final TextField nameField = new TextField();

    private void createForm() {
        final HorizontalLayout searchPanelLayout = new HorizontalLayout();
        searchPanelLayout.setMargin(true);
        searchPanelLayout.setSpacing(true);

        final Label label = new Label("New Transaction");
        searchPanelLayout.addComponent(label);
        searchPanelLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

        searchPanelLayout.addComponent(nameField);
        searchPanelLayout.setComponentAlignment(nameField, Alignment.MIDDLE_LEFT);

        final CreateNewTransactionButton button = new CreateNewTransactionButton();
        searchPanelLayout.addComponent(button);
        searchPanelLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);

        final VerticalLayout paddingLeft = new VerticalLayout();
        paddingLeft.setWidth(100, UNITS_PIXELS);
        searchPanelLayout.addComponent(paddingLeft);

        final Resource resource = new ThemeResource("../../../images/BlueFenix.png");
        final Embedded embedded = new Embedded(null, resource);
        searchPanelLayout.addComponent(embedded);
        searchPanelLayout.setComponentAlignment(embedded, Alignment.MIDDLE_RIGHT);

        final VerticalLayout paddingRight = new VerticalLayout();
        paddingRight.setWidth(30, UNITS_PIXELS);
        searchPanelLayout.addComponent(paddingRight);

        layout.addComponent(new Panel("<h2>Long Transaction Management</h2>", searchPanelLayout));

        layout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private final Table table = new TransactionalTable(BUNDLE_NAME);

    private void createTable() {

        table.setSizeFull();
        table.setPageLength(0);

        table.setSelectable(Boolean.FALSE);
        table.setImmediate(Boolean.TRUE);
        table.setWriteThrough(Boolean.TRUE);
        table.setReadThrough(Boolean.TRUE);

        table.setContainerDataSource(container);
        table.setVisibleColumns(new Object[] { "name" });

        table.addGeneratedColumn("Activate", new ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final TransactionalContext context = (TransactionalContext) itemId;

                final Button button = new Button("Activate", new Button.ClickListener() {

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        getWindow().showNotification("Activating '" + context + "'");
                    }

                });
                button.addStyleName(BennuTheme.BUTTON_LINK);

                final Button commitButton = new Button("Commit", new Button.ClickListener() {

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        getWindow().showNotification("Committing '" + context + "'");
                    }

                });
                commitButton.addStyleName(BennuTheme.BUTTON_LINK);

                Layout layout = new HorizontalLayout();
                layout.addComponent(button);
                layout.addComponent(commitButton);
                return layout;
            }
        });

        table.addGeneratedColumn("Commit", new ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final TransactionalContext context = (TransactionalContext) itemId;

                final Button button = new Button("Commit", new Button.ClickListener() {

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        getWindow().showNotification("Committing '" + context + "'");
                    }

                });
                button.addStyleName(BennuTheme.BUTTON_LINK);
                return button;
            }
        });

        table.addGeneratedColumn("Abort", new ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final TransactionalContext context = (TransactionalContext) itemId;

                final Button button = new Button("Abort", new Button.ClickListener() {

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        getWindow().showNotification("Aborting '" + context + "'");
                    }

                });
                button.addStyleName(BennuTheme.BUTTON_LINK);
                return button;
            }
        });

        table.addGeneratedColumn("Delete", new ColumnGenerator() {

            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final TransactionalContext context = (TransactionalContext) itemId;

                final Button button = new Button("Delete", new Button.ClickListener() {

                    @Atomic
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        source.getContainerDataSource().removeItem(itemId);
                        context.delete();
                    }

                });
                button.addStyleName(BennuTheme.BUTTON_LINK);
                return button;
            }
        });

        layout.addComponent(table);
    }

    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
        return true;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {

    }

}
