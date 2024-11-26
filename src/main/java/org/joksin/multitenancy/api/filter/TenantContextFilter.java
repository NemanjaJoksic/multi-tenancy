package org.joksin.multitenancy.api.filter;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import lombok.AllArgsConstructor;
import org.joksin.multitenancy.core.TenantContext;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Filter("/api/**")
@AllArgsConstructor
public class TenantContextFilter implements HttpServerFilter {

  private final TenantContext tenantContext;

  @Override
  public Publisher<MutableHttpResponse<?>> doFilter(
      HttpRequest<?> request, ServerFilterChain chain) {

    var tenant = request.getHeaders().get("Tenant");

    if (tenant != null) {
      tenantContext.initialize(tenant);
    }

    return Mono.from(chain.proceed(request)).doFinally((x) -> tenantContext.clear());
  }
}
