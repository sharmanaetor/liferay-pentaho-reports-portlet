package com.mycompany.reports;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class MyReport {
	
	private List<PentahoReport> reportList = new ArrayList<PentahoReport>();
	private PentahoReport selectedReport;
	
	public MyReport() {
		
		ReportListClass report1 = new ReportListClass();
		report1.setReportName("Sample1.prpt");
		report1.setReportPath("Sample1.prpt");		
						
		ReportListClass report2 = new ReportListClass();
		report2.setReportName("Sample2.prpt");
		report2.setReportPath("Sample2.prpt");
		
		ReportListClass report3 = new ReportListClass();
		report3.setReportName("Sample3.prpt");
		report3.setReportPath("Sample3.prpt");
		
		reportList.add(new PentahoReport(report1));
		reportList.add(new PentahoReport(report2));
		reportList.add(new PentahoReport(report3));		
	}
	
	/* Utility Function */
	public PentahoReport getReport(String fileName) {

		PentahoReport report = null;

		List<PentahoReport> reports = getReportList();

		for (PentahoReport currentReport : reports) {

			if (currentReport.getReport().getReportPath().equals(fileName)) {
				report = currentReport;
				break;
			}
		}

		return report;
	}
	
	/* Getters and setters */
	public List<PentahoReport> getReportList() {
		return reportList;
	}
	public void setReportList(List<PentahoReport> reportList) {
		this.reportList = reportList;
	}
	public PentahoReport getSelectedReport() {
		return selectedReport;
	}
	public void setSelectedReport(PentahoReport selectedReport) {
		this.selectedReport = selectedReport;
	}

}
