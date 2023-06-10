package com.nsunf.newsvoca.config;

import com.nsunf.newsvoca.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@RequiredArgsConstructor
@Order(1)
@WebFilter("*")
@Component
public class CategoryFilter implements Filter {

    private final CategoryService categoryService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        categoryService.initCategories();

        chain.doFilter(request, response);
    }
}
