package com.albert.utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRExporterContext;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.PrintServiceExporterConfiguration;
import net.sf.jasperreports.export.PrintServiceReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration;
/**
 * jasper bug(覆盖原来JRPrintServiceExporter类,自定义纸张不正常)
 * @author albert
 * @version 1.0
 */
public class MyJRPrintServiceExporter extends JRAbstractExporter<PrintServiceReportConfiguration, PrintServiceExporterConfiguration, ExporterOutput, JRExporterContext>
  implements Printable
{
  protected static final String PRINT_SERVICE_EXPORTER_PROPERTIES_PREFIX = "net.sf.jasperreports.export.print.service.";
  protected JRGraphics2DExporter exporter;
  protected SimpleGraphics2DReportConfiguration grxConfiguration;
  protected int reportIndex;
  private PrintService printService;
  private Boolean[] printStatus;

  public MyJRPrintServiceExporter()
  {
    this(DefaultJasperReportsContext.getInstance());
  }

  public MyJRPrintServiceExporter(JasperReportsContext jasperReportsContext)
  {
    super(jasperReportsContext);

    this.exporterContext = new ExporterContext();
  }

  protected Class<PrintServiceExporterConfiguration> getConfigurationInterface()
  {
    return PrintServiceExporterConfiguration.class;
  }

  protected Class<PrintServiceReportConfiguration> getItemConfigurationInterface()
  {
    return PrintServiceReportConfiguration.class;
  }

  protected void ensureOutput()
  {
  }

  public void exportReport()
    throws JRException
  {
    ensureJasperReportsContext();
    ensureInput();

    initExport();

    ensureOutput();
    try
    {
      PrintServiceExporterConfiguration configuration = (PrintServiceExporterConfiguration)getCurrentConfiguration();

      PrintServiceAttributeSet printServiceAttributeSet = configuration.getPrintServiceAttributeSet();
      if (printServiceAttributeSet == null)
      {
        printServiceAttributeSet = new HashPrintServiceAttributeSet();
      }

      boolean displayPageDialog = false;
      boolean displayPageDialogOnlyOnce = false;
      boolean displayPrintDialog = false;
      boolean displayPrintDialogOnlyOnce = false;

      Boolean pageDialog = configuration.isDisplayPageDialog();
      if (pageDialog != null)
      {
        displayPageDialog = pageDialog.booleanValue();
      }

      Boolean pageDialogOnlyOnce = configuration.isDisplayPageDialogOnlyOnce();
      if ((displayPageDialog) && (pageDialogOnlyOnce != null))
      {
        displayPageDialogOnlyOnce = pageDialogOnlyOnce.booleanValue();
      }

      Boolean printDialog = configuration.isDisplayPrintDialog();
      if (printDialog != null)
      {
        displayPrintDialog = printDialog.booleanValue();
      }

      Boolean printDialogOnlyOnce = configuration.isDisplayPrintDialogOnlyOnce();
      if ((displayPrintDialog) && (printDialogOnlyOnce != null))
      {
        displayPrintDialogOnlyOnce = printDialogOnlyOnce.booleanValue();
      }
      PrinterJob printerJob = PrinterJob.getPrinterJob();

      JRPrinterAWT.initPrinterJobFields(printerJob);

      printerJob.setPrintable(this);

      this.printStatus = null;

      this.printService = configuration.getPrintService();
      if (this.printService == null) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);
        if (services.length > 0)
        {
          this.printService = services[0];
        }
      }

      if (this.printService == null)
      {
        throw new JRException("No suitable print service found.");
      }

      try
      {
        printerJob.setPrintService(this.printService);
      }
      catch (PrinterException e)
      {
        throw new JRException(e);
      }

      List items = this.exporterInput.getItems();

      PrintRequestAttributeSet printRequestAttributeSet = null;
      if ((displayPrintDialogOnlyOnce) || (displayPageDialogOnlyOnce))
      {
        printRequestAttributeSet = new HashPrintRequestAttributeSet();
        setDefaultPrintRequestAttributeSet(printRequestAttributeSet);
        setOrientation(((ExporterInputItem)items.get(0)).getJasperPrint(), printRequestAttributeSet);
        if (displayPageDialogOnlyOnce)
        {
          if (printerJob.pageDialog(printRequestAttributeSet) == null)
          {
            return;
          }

          displayPageDialog = false;
        }

        if (displayPrintDialogOnlyOnce)
        {
          if (!printerJob.printDialog(printRequestAttributeSet))
          {
            this.printStatus = new Boolean[] { Boolean.FALSE };
            return;
          }

          displayPrintDialog = false;
        }

      }

      List status = new ArrayList();

      for (this.reportIndex = 0; this.reportIndex < items.size(); this.reportIndex += 1)
      {
        ExporterInputItem item = (ExporterInputItem)items.get(this.reportIndex);

        setCurrentExporterInputItem(item);

        PrintServiceReportConfiguration lcItemConfiguration = (PrintServiceReportConfiguration)getCurrentItemConfiguration();

        this.exporter = new JRGraphics2DExporter(this.jasperReportsContext);
        this.exporter.setExporterInput(new SimpleExporterInput(this.jasperPrint));
        this.grxConfiguration = new SimpleGraphics2DReportConfiguration();
        this.grxConfiguration.setProgressMonitor(lcItemConfiguration.getProgressMonitor());
        this.grxConfiguration.setOffsetX(lcItemConfiguration.getOffsetX());
        this.grxConfiguration.setOffsetY(lcItemConfiguration.getOffsetY());
        this.grxConfiguration.setZoomRatio(lcItemConfiguration.getZoomRatio());

        this.grxConfiguration.setExporterFilter(this.filter);
        this.grxConfiguration.setMinimizePrinterJobSize(lcItemConfiguration.isMinimizePrinterJobSize());

        if ((displayPrintDialog) || (displayPageDialog) || ((!displayPrintDialogOnlyOnce) && (!displayPageDialogOnlyOnce)))
        {
          printRequestAttributeSet = new HashPrintRequestAttributeSet();
          setDefaultPrintRequestAttributeSet(printRequestAttributeSet);
          setOrientation(this.jasperPrint, printRequestAttributeSet);
        }

        try
        {
          JRAbstractExporter.PageRange pageRange = getPageRange();
          int startPageIndex = (pageRange == null) || (pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex().intValue();
          int endPageIndex = (pageRange == null) || (pageRange.getEndPageIndex() == null) ? this.jasperPrint.getPages().size() - 1 : pageRange.getEndPageIndex().intValue();
          if(endPageIndex<0) endPageIndex=0;
          if (items.size() == 1)
          {
            printRequestAttributeSet.add(new PageRanges(startPageIndex + 1, endPageIndex + 1));
          }

          printerJob.setJobName("JasperReports - " + this.jasperPrint.getName());

          if (displayPageDialog)
          {
            printerJob.pageDialog(printRequestAttributeSet);
          }
          if (displayPrintDialog)
          {
            if (printerJob.printDialog(printRequestAttributeSet))
            {
              status.add(Boolean.TRUE);
              printerJob.print(printRequestAttributeSet);
            }
            else
            {
              status.add(Boolean.FALSE);
            }
          }
          else
          {
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = pageFormat.getPaper();

            switch (this.jasperPrint.getOrientationValue().ordinal())
            {
            case 1:
              pageFormat.setOrientation(0);
              paper.setSize(this.jasperPrint.getPageHeight(), this.jasperPrint.getPageWidth());
              paper.setImageableArea(0.0D, 0.0D, this.jasperPrint.getPageHeight(), this.jasperPrint.getPageWidth());

              break;
            case 2:
            default:
              pageFormat.setOrientation(1);
              paper.setSize(this.jasperPrint.getPageWidth(), this.jasperPrint.getPageHeight());
              paper.setImageableArea(0.0D, 0.0D, this.jasperPrint.getPageWidth(), this.jasperPrint.getPageHeight());
            }

            pageFormat.setPaper(paper);

            Book pBook = new Book();
            pBook.append(this, pageFormat,jasperPrint.getPages().size());
            printerJob.setPageable(pBook);

            printerJob.print(printRequestAttributeSet);
          }
        }
        catch (PrinterException e)
        {
          throw new JRException(e);
        }
      }

      this.printStatus = ((Boolean[])status.toArray(new Boolean[status.size()]));
      this.printService = printerJob.getPrintService();
    }
    finally
    {
      resetExportContext();
    }
  }

  protected void initExport()
  {
    super.initExport();
  }

  protected void initReport()
  {
    super.initReport();
  }

  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
    throws PrinterException
  {
    if (Thread.interrupted())
    {
      throw new PrinterException("Current thread interrupted.");
    }

    if ((pageIndex < 0) || (pageIndex >= this.jasperPrint.getPages().size()))
    {
      return 1;
    }

    SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
    output.setGraphics2D((Graphics2D)graphics);
    this.exporter.setExporterOutput(output);

    this.grxConfiguration.setPageIndex(Integer.valueOf(pageIndex));
    this.exporter.setConfiguration(this.grxConfiguration);
    try
    {
      this.exporter.exportReport();
    }
    catch (JRException e)
    {
      throw new PrinterException(e.getMessage());
    }

    return 0;
  }

  private void setOrientation(JasperPrint jPrint, PrintRequestAttributeSet printRequestAttributeSet)
  {
    if (!printRequestAttributeSet.containsKey(MediaPrintableArea.class))
    {
      int printableWidth;
      int printableHeight;
      switch (jPrint.getOrientationValue().ordinal())
      {
      case 1:
        printableWidth = jPrint.getPageHeight();
        printableHeight = jPrint.getPageWidth();
        break;
      default:
        printableWidth = jPrint.getPageWidth();
        printableHeight = jPrint.getPageHeight();
      }

      printRequestAttributeSet.add(new MediaPrintableArea(0.0F, 0.0F, printableWidth / 72.0F, printableHeight / 72.0F, 25400));
    }

    if (!printRequestAttributeSet.containsKey(OrientationRequested.class))
    {
      OrientationRequested orientation;
      switch (jPrint.getOrientationValue().ordinal())
      {
      case 1:
        orientation = OrientationRequested.LANDSCAPE;
        break;
      default:
        orientation = OrientationRequested.PORTRAIT;
      }

      printRequestAttributeSet.add(orientation);
    }
  }

  private void setDefaultPrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet)
  {
    PrintRequestAttributeSet configPrintRequestAttributeSet = ((PrintServiceExporterConfiguration)getCurrentConfiguration()).getPrintRequestAttributeSet();
    if (configPrintRequestAttributeSet != null)
    {
      printRequestAttributeSet.addAll(configPrintRequestAttributeSet);
    }
  }

  public static boolean checkAvailablePrinters()
  {
    PrintService[] ss = PrinterJob.lookupPrintServices();
    for (int i = 0; i < ss.length; i++) {
      Attribute[] att = ss[i].getAttributes().toArray();
      for (int j = 0; j < att.length; j++) {
        if (att[j].equals(PrinterIsAcceptingJobs.ACCEPTING_JOBS)) {
          return true;
        }
      }
    }
    return false;
  }

  public Boolean[] getPrintStatus()
  {
    return this.printStatus;
  }

  public PrintService getPrintService()
  {
    return this.printService;
  }

  public String getExporterKey()
  {
    return null;
  }

  public String getExporterPropertiesPrefix()
  {
    return "net.sf.jasperreports.export.print.service.";
  }

  protected class ExporterContext extends JRAbstractExporter<PrintServiceReportConfiguration, PrintServiceExporterConfiguration, ExporterOutput, JRExporterContext>.BaseExporterContext
  {
    protected ExporterContext()
    {
      super();
    }
  }
}