/**
 * 
 */
package com.albert;

import java.util.Collections;
import java.util.Set;

import javax.print.PrintService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.albert.model.ConfigEntity;
import com.albert.service.CommonService;
import com.albert.service.impl.PrintServiceImpl;
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
	private Set<PrintService> printServices;
	private CommonService commonService ;
	private ConfigEntity config;
	private final Log log = LogFactory.getLog(getClass());
	
	private synchronized void initConfig() {
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
		commonService.print((JasperPrint)re.getObj());
	}
	
	
	
	
	private volatile static AppContext context;
    private AppContext(){
    		initConfig();
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
	public Set<PrintService> getPrintServices() {
		return printServices;
	}
	public void setPrintServices(Set<PrintService> printServices) {
		this.printServices = printServices;
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
}
