package com.rest.marketplace.infrastructure.rest.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream cachedBody = new ByteArrayOutputStream();

	public CachedBodyHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return new ServletOutputStream() {
			@Override
			public void write(int b) {
				cachedBody.write(b);
			}

			@Override
			public boolean isReady() { return true; }

			@Override
			public void setWriteListener(WriteListener listener) {}
		};
	}

	@Override
	public PrintWriter getWriter() {
		return new PrintWriter(cachedBody);
	}

	public String getCachedBody() {
		return cachedBody.toString();
	}

}
