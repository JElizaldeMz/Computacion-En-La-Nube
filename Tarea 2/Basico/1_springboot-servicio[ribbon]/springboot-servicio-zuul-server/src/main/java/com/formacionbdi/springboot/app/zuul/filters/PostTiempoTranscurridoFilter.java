package com.formacionbdi.springboot.app.zuul.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

	private static final Logger logger = LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);

	@Override
	public String filterType() {
		return "post";
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
		Long tiempoInicio = (Long) ctx.get("tiempoInicio");

		if (tiempoInicio != null) {
			long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
			logger.info("========== FILTRO POST ==========");
			logger.info("Tiempo transcurrido en la peticion: {} ms", tiempoTranscurrido);

			if (tiempoTranscurrido > 1000) {
				logger.warn("ALERTA: La peticion tardo mas de 1 segundo: {} ms", tiempoTranscurrido);
			}
		}
		return null;
	}
}
