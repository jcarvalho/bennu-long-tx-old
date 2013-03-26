package pt.ist.bennu.longtx.presentationTier.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.domain.groups.UserGroup;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/longTx")
public class CreateLongTxAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "LONG_TX_RESOURCES", key = "add.node.longtx.interface", groupKey = "label.module.longtx")
    public final ActionForward createAnnouncmentNodes(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
        final Node node = getDomainObject(request, "parentOfNodesToManageId");

        VaadinNode.createVaadinNode(virtualHost, node, "LONG_TX_RESOURCES", "label.link.longtx.interface",
                "LongTransactionManager", UserGroup.getInstance());

        return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
