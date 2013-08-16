package com.mycompany.reports.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.output.table.xls.ExcelReportUtil;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.theme.ThemeDisplay;

public class ReportExcelExportResource extends Resource {
	// Public Constants
	public static final String CONTENT_TYPE = "application/vnd.ms-excel";
	public static final String RESOURCE_NAME = "exportExcel";

	// Private Constants
	private static final String PARAM_REPORT_FILE = "reportFile";
	private static final String PARAM_REPORT_START_DATE = "startDate";
	private static final String PARAM_REPORT_END_DATE = "endDate";

	// Private Data Members
	private String reportFileName;
	private String reportStartDate;
	private String reportEndDate;
	private String requestPath;

	public ReportExcelExportResource() {
		setLibraryName(ReportExcelResourceHandler.LIBRARY_NAME);
		setResourceName(RESOURCE_NAME);
		setContentType(CONTENT_TYPE);

		ClassicEngineBoot.getInstance().start();
	}

	public ReportExcelExportResource(String reportFileName) {
		this();
		this.reportFileName = reportFileName;
	}

	public ReportExcelExportResource(String reportFileName, String reportStartDate, String reportEndDate) {
		this();
		this.reportFileName = reportFileName;
		this.reportStartDate = reportStartDate;
		this.reportEndDate = reportEndDate;
	}

	protected String getReportFileName() {
		if (reportFileName == null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			reportFileName = facesContext.getExternalContext().getRequestParameterMap().get(PARAM_REPORT_FILE);
		}
		return reportFileName;
	}

	protected String getReportStartDate() {
		if (reportStartDate == null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			reportStartDate = facesContext.getExternalContext().getRequestParameterMap().get(PARAM_REPORT_START_DATE);
		}
		return reportStartDate;
	}

	protected String getReportEndDate() {
		if (reportEndDate == null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			reportEndDate = facesContext.getExternalContext().getRequestParameterMap().get(PARAM_REPORT_END_DATE);
		}
		return reportEndDate;
	}

	private void setReportTimezone() {

		ThemeDisplay themeDisplay = LiferayFacesContext.getInstance().getThemeDisplay();
		TimeZone.setDefault(TimeZone.getTimeZone(themeDisplay.getUser().getTimeZoneId()));

	}

	@Override
	public InputStream getInputStream() throws IOException {

		ResourceManager manager = new ResourceManager();
		manager.registerDefaults();

		String reportPath = "file:" + FacesContext.getCurrentInstance().getExternalContext().getRealPath(getReportFileName());
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		try {
			org.pentaho.reporting.libraries.resourceloader.Resource res = manager.createDirectly(new URL(reportPath), MasterReport.class);
			MasterReport report = (MasterReport) res.getResource();

			setReportTimezone();

			if (getReportStartDate() != null && getReportEndDate() != null) {
				report.getParameterValues().put(PARAM_REPORT_START_DATE, getReportStartDate());
				report.getParameterValues().put(PARAM_REPORT_END_DATE, getReportEndDate());
			}

			// For Excel
			ExcelReportUtil.createXLS(report, byteArray);

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		return new ByteArrayInputStream(byteArray.toByteArray());
	}

	@Override
	public String getRequestPath() {
		if (requestPath == null) {
			StringBuilder buf = new StringBuilder();
			buf.append(ResourceHandler.RESOURCE_IDENTIFIER);
			buf.append("/");
			buf.append(getResourceName());
			buf.append("?ln=");
			buf.append(getLibraryName());
			buf.append("&");
			buf.append(PARAM_REPORT_FILE);
			buf.append("=");
			buf.append(getReportFileName());
			buf.append("&");
			buf.append(PARAM_REPORT_START_DATE);
			buf.append("=");
			buf.append(getReportStartDate());
			buf.append("&");
			buf.append(PARAM_REPORT_END_DATE);
			buf.append("=");
			buf.append(getReportEndDate());

			requestPath = buf.toString();
		}

		return requestPath;
	}

	@Override
	public Map<String, String> getResponseHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean userAgentNeedsUpdate(FacesContext arg0) {

		return false;
	}
}
