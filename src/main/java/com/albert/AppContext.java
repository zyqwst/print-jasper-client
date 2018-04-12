/**
 * 
 */
package com.albert;

import java.awt.print.PrinterJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.print.PrintService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.albert.model.ConfigEntity;
import com.albert.model.JasperForPrinter;
import com.albert.service.CommonService;
import com.albert.service.impl.PrintServiceImpl;
import com.albert.ui.SetWindow;
import com.albert.utils.HttpRequestUtil;
import com.albert.utils.JsonUtil;
import com.sy.domain.ResponseEntity;

import net.sf.jasperreports.engine.JasperPrint;

/** 
* @ClassName: AppContext 
* @Description: 
* @author albert
* @date 2018年4月11日 下午3:57:29 
*  
*/
public class AppContext {
	private Map<String,PrintService> allPrinter;
	private CommonService commonService ;
	private ConfigEntity config;
	private SetWindow window;
	private final Log log = LogFactory.getLog(getClass());
	
	public synchronized void initConfig() {
		try {
			String str = JsonUtil.readJsonFromStream();
			ConfigEntity config = JsonUtil.getGson().fromJson(str, ConfigEntity.class).checkPrinter();
			Collections.sort(config.getJasperPrinters());
			this.setConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("初始化失败",e);
		}
	}
	
	private void initPrinter() {
		allPrinter = new HashMap<String,PrintService>();
		PrintService[] services = PrinterJob.lookupPrintServices();
		for(PrintService p : services){
			allPrinter.put(p.getName(), p);
		}
	}
	/**
	 * 打印
	 * @param httpUrl
	 * @throws Exception
	 */
	public void print(String httpUrl) throws Exception{
		if(httpUrl==null)throw new Exception("打印数据获取url为空");
		Object en = HttpRequestUtil.request(httpUrl);
		ResponseEntity re = (ResponseEntity) en;
		if(re.getStatus()==-1)throw new Exception(re.getMsg());
		
		JasperPrint jasper = (JasperPrint)re.getObj();
		
		JasperForPrinter printer = null;
		
		for(JasperForPrinter print : config.getJasperPrinters()) {
			if(print.getJasper().equals(jasper.getName())) {
				printer = print;
				break;
			}
		}
		if(printer==null) {
			JasperForPrinter j = new JasperForPrinter(jasper.getName(), "未设置", false, true);
			config.getJasperPrinters().add(j);
			saveConfig();
			window.visible(true);
		}else {			
			commonService.print(jasper);
		}
	}
	
	
	private volatile static AppContext context;
    private AppContext(){
		initConfig();
		initPrinter();
		commonService = PrintServiceImpl.instance();
    }
    public static AppContext INSTANCE(){
        if (context == null) {
            synchronized (AppContext.class) {
                if (context == null) {
                		context = new AppContext();
                }
            }
        }
        return context;
    }
    
    public void deleteJasper(JasperForPrinter j) throws Exception {
    		for(JasperForPrinter x : config.getJasperPrinters()) {
    			if(x.getUuid().equals(j.getUuid())){    				
    				config.getJasperPrinters().remove(x);
    				break;
    			}
    		}
    		saveConfig();
    }
    
    public void saveConfig() throws Exception {
    		try {
				JsonUtil.writeObject(config);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("配置文件写入失败");
			}
    }
	public ConfigEntity getConfig() {
		return config;
	}
	public void setConfig(ConfigEntity config) {
		this.config = config;
	}
	public CommonService getCommonService() {
		return commonService;
	}
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
	public static void main(String[] args) {
		ConfigEntity config = AppContext.INSTANCE().getConfig().checkPrinter();
		System.out.println(config);
	}
	public Set<String> getAllPrinter() {
		return allPrinter;
	}
	public void setAllPrinter(Set<String> allPrinter) {
		this.allPrinter = allPrinter;
	}

	public SetWindow getWindow() {
		return this.window==null? new SetWindow(this):this.window;
	}

}
