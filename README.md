liferay-pentaho-reports-portlet
===============================

An integration of Pentaho with Liferay and Primefaces

A few things to notice here:

1. An entry in faces-config.xml is required.
2. The <application> section in faces-config.xml needs to come before the <lifecycle> section.
3. The .prpt files included here will not work since they are not connected to a datasource. Replace them with your own and refer to them in MyReport.java instead.
4. All my reports are set to take a startDate and an endDate parameter. You can remove that as you may not needed but this shows how parameters may be passed to reports generated using the Pentaho Report Designer.
