package com.ag777.util.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.ag777.util.file.base.BaseExcelHelper;
import com.ag777.util.file.bean.ExcelValidateBean;

/**
 * @author wanggz
 * @Description excel验证辅助类
 * Time: created at 2017/6/19. last modify at 2017/6/19.
 * Mark: 
 */
public abstract class ExcelHelper extends BaseExcelHelper{
	
	private List<List<String>> titles;
	private Map<String, ExcelValidateBean> validateMap;
	
	public ExcelHelper(List<List<ExcelValidateBean>> validateList) {
		titles = new ArrayList<>();
		validateMap = new LinkedHashMap<String, ExcelValidateBean>();
		
		for (List<ExcelValidateBean> list2 : validateList) {
			List<String> items = new ArrayList<String>();
			for (ExcelValidateBean orgExcelValidateBean : list2) {
				String title = orgExcelValidateBean.getTitle();
				items.add(title);
				validateMap.put(title, orgExcelValidateBean);
			}
			titles.add(items);
		}
		
	}

	@Override
	protected List<List<String>> titles() {
		return titles;
	}

	@Override
	protected String validate(Map<String, String> item) {
		Iterator<String> itor = validateMap.keySet().iterator();
		while(itor.hasNext()) {
			String key = itor.next();
			String errMsg = validateItem(key, item);
			if(errMsg != null) {
				return errMsg;
			}
		}
		
		return null;
	}
	

	/*内部工具方法*/
	private String validateItem(String key,Map<String, String> map) {
		String val = map.get(key);
		
		ExcelValidateBean validateBean = validateMap.get(key);
		String err = validateBean.getErrMsg();
		
		if(validateBean.getErrKey()!=null && err.contains("【】")) {
			err = err.replace("【】", "【"+map.get(validateBean.getErrKey())+"】");
			err += "的"+validateBean.getName();
		}else {
			err += validateBean.getName();
		}
		if(validateBean.isRequire() && isNullOrEmpty(val)) {
			return err+"未填写";
		}
		if(!isNullOrEmpty(val) && validateBean.getReg() != null && !val.matches(validateBean.getReg())) {	//正则
			return err+"格式不正确";
		}
		if(!isNullOrEmpty(val) && validateBean.getMaxLength() != null && val.length() > validateBean.getMaxLength()) {
			return err+"长度超过"+validateBean.getMaxLength();
		}
		return null;
	}

}
