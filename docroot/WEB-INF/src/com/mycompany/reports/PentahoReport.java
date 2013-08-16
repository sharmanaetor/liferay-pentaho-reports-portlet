package com.mycompany.reports;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mycompany.reports.resource.ReportExcelExportResource;
import com.mycompany.reports.resource.ReportHTMLExportResource;
import com.mycompany.reports.resource.ReportPDFExportResource;

public class PentahoReport {

	private ReportListClass report;
	private Calendar reportStartDate = Calendar.getInstance();
	private Calendar reportEndDate = Calendar.getInstance();
	private String reportPdfResourceURL;
	private String reportHtmlResourceURL;
	private String reportExcelResourceURL;

	public PentahoReport() {
		reportStartDate.set(Calendar.DATE, 1);
		reportEndDate.set(Calendar.DATE, reportEndDate.getActualMaximum(Calendar.DATE));
	}

	public PentahoReport(ReportListClass report) {
		
		reportStartDate.set(Calendar.DATE, 1);
		reportEndDate.set(Calendar.DATE, reportEndDate.getActualMaximum(Calendar.DATE));
		
		this.report = report;
	}
	

	/* Getters and setters */
	public String getReportPdfResourceURL() {
		String reportPath = report.getReportPath();
		String formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").format(reportStartDate.getTime());
		String formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").format(reportEndDate.getTime());
		
		this.reportPdfResourceURL = new ReportPDFExportResource(reportPath, formattedStartDate , formattedEndDate).getRequestPath();
		
		return reportPdfResourceURL;
	}

	public void setReportPdfResourceURL(String reportPdfResourceURL) {
		this.reportPdfResourceURL = reportPdfResourceURL;
	}

	public String getReportHtmlResourceURL() {
		String reportPath = report.getReportPath();
		String formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").format(reportStartDate.getTime());
		String formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").format(reportEndDate.getTime());
		
		this.reportHtmlResourceURL = new ReportHTMLExportResource(reportPath, formattedStartDate , formattedEndDate).getRequestPath();
		
		return reportHtmlResourceURL;
	}

	public void setReportHtmlResourceURL(String reportHtmlResourceURL) {
		this.reportHtmlResourceURL = reportHtmlResourceURL;
	}

	public String getReportExcelResourceURL() {
		String reportPath = report.getReportPath();
		String formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").format(reportStartDate.getTime());
		String formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").format(reportEndDate.getTime());
		
		this.reportExcelResourceURL = new ReportExcelExportResource(reportPath, formattedStartDate , formattedEndDate).getRequestPath();
		
		return reportExcelResourceURL;
	}

	public void setReportExcelResourceURL(String reportExcelResourceURL) {
		this.reportExcelResourceURL = reportExcelResourceURL;
	}

	public ReportListClass getReport() {
		return report;
	}

	public void setReport(ReportListClass report) {
		this.report = report;
	}

	public Calendar getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(Calendar reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public Calendar getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(Calendar reportEndDate) {
		this.reportEndDate = reportEndDate;
	}
}
