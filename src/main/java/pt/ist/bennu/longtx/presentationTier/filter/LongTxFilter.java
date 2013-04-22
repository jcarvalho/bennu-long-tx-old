package pt.ist.bennu.longtx.presentationTier.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.fenixframework.longtx.TransactionalContext;

@WebFilter("*")
public class LongTxFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LongTxFilter.class);

    public static final String LONG_TX_CONTEXT_KEY = "__LONG_TX_CONTEXT_KEY__";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        TransactionalContext context = (TransactionalContext) req.getSession().getAttribute(LONG_TX_CONTEXT_KEY);
        if (context != null) {
            try {
                // LongTransaction.bindContextToThread(context);
                logger.debug("Setting TransactionalContext {} for user {}", context, Authenticate.getCurrentUser());
                chain.doFilter(request, response);
            } finally {
                // LongTransaction.unbindContextFromThread();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

}
