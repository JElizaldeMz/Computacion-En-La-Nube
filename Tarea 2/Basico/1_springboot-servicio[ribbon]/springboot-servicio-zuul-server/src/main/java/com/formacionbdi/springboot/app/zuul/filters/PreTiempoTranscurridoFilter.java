package com.formacionbdi.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PreTiempoTranscurridoFilter extends ZuulFilter {

	private static final Logger logger = LoggerFactory.getLogger(PreTiempoTranscurridoFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		logger.info("========== FILTRO PRE ==========");
		logger.info("Entrando a la peticion: {} {}", request.getMethod(), request.getRequestURL().toString());

		ctx.set("tiempoInicio", System.currentTimeMillis());
		return null;
	}
}
