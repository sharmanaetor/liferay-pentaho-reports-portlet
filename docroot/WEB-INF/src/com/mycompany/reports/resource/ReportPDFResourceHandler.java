package com.mycompany.reports.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

public class ReportPDFResourceHandler extends ResourceHandlerWrapper {

	// Public Constants
	public static final String LIBRARY_NAME = "pentahoPDFReports";

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(ReportPDFResourceHandler.class);

	// Private Data Members
	private ResourceHandler wrappedResourceHandler;

	public ReportPDFResourceHandler(ResourceHandler wrappedResourceHandler) {
		this.wrappedResourceHandler = wrappedResourceHandler;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {
		if (LIBRARY_NAME.equals(libraryName)) {
			if (ReportPDFExportResource.RESOURCE_NAME.equals(resourceName)) {
				return new ReportPDFExportResource();
			} else {
				return wrappedResourceHandler.createResource(resourceName, libraryName);
			}

		} else {
			return wrappedResourceHandler.createResource(resourceName, libraryName);
		}
	}

	@Override
	public void handleResourceRequest(FacesContext facesContext) throws IOException {

		ExternalContext externalContext = facesContext.getExternalContext();
		String libraryName = externalContext.getRequestParameterMap().get("ln");
		String resourceName = externalContext.getRequestParameterMap().get("javax.faces.resource");

		if (LIBRARY_NAME.equals(libraryName) && ReportPDFExportResource.RESOURCE_NAME.equals(resourceName)) {

			Resource resource = createResource(resourceName, libraryName);

			ReadableByteChannel readableByteChannel = null;
			WritableByteChannel writableByteChannel = null;
			InputStream inputStream = null;
			int bufferSize = 1024;
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

			try {

				// Open an input stream in order to read the resource's
				// contents/data.
				inputStream = resource.getInputStream();

				if (inputStream != null) {

					// Set the response buffer size.
					int responseBufferSize = byteBuffer.capacity();
					externalContext.setResponseBufferSize(responseBufferSize);

					String responseContentType = resource.getContentType();

					if (responseContentType != null) {
						externalContext.setResponseContentType(responseContentType);
					}

					// Copy the bytes in the resource's input stream to the
					// response's output stream.
					int responseContentLength = 0;
					readableByteChannel = Channels.newChannel(inputStream);
					writableByteChannel = Channels.newChannel(externalContext.getResponseOutputStream());

					int bytesRead = readableByteChannel.read(byteBuffer);
					int bytesWritten = 0;

					while (bytesRead != -1) {
						byteBuffer.rewind();
						byteBuffer.limit(bytesRead);

						do {
							bytesWritten += writableByteChannel.write(byteBuffer);
						} while (bytesWritten < responseContentLength);

						byteBuffer.clear();
						responseContentLength += bytesRead;
						bytesRead = readableByteChannel.read(byteBuffer);

						if (logger.isTraceEnabled()) {

							// Surround with isTraceEnabled check in order to
							// avoid unnecessary conversion
							// of int to String.
							logger.trace("Handling - MORE bytesRead=[{0}]", Integer.toString(bytesRead));
						}
					}

					// Now that we know how big the file is, set the response
					// content length.
					externalContext.setResponseContentLength(responseContentLength);
					externalContext.setResponseStatus(HttpServletResponse.SC_OK);

					logger.debug("HANDLED (SC_OK) resourceName=[{0}], libraryName[{1}], responseContentType=[{2}], responseContentLength=[{3}]", new Object[] {
							resourceName, libraryName, responseContentType, responseContentLength });
				}
			} catch (IOException e) {
				externalContext.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
				logger.error("NOT HANDLED (SC_NOT_FOUND) resourceName=[{0}], libraryName[{1}], errorMessage=[{2}]",
						new Object[] { resourceName, libraryName, e.getMessage() }, e);
			} finally {

				if (writableByteChannel != null) {
					writableByteChannel.close();
				}

				if (readableByteChannel != null) {
					readableByteChannel.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}
			}
		} else {
			super.handleResourceRequest(facesContext);
		}
	}

	@Override
	public boolean libraryExists(String libraryName) {

		if (LIBRARY_NAME.equals(libraryName)) {
			return true;
		} else {
			return super.libraryExists(libraryName);
		}
	}

	@Override
	public ResourceHandler getWrapped() {

		return wrappedResourceHandler;
	}

}
