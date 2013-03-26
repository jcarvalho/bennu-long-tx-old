package pt.ist.bennu.longtx.domain;

import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.longtx.TransactionalContext;

public class LongTransactionUtils {

    @Atomic
    public static TransactionalContext createNewTransactionForUser(String name) {
        TransactionalContext context = new TransactionalContext(name);
        context.setUser(Authenticate.getCurrentUser());
        return context;
    }

}
