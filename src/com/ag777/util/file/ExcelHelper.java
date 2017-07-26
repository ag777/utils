package com.ag777.util.file;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.ag777.util.file.base.BaseExcelHelper;
import com.ag777.util.file.bean.ExcelValidateBean;

/**
 * @author ag777
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
				String title = orgExcelValidateBean.title();
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
		
		ExcelValidateBean v = validateMap.get(key);
		StringBuilder err;
		
		//初始化错误提示
		if (v.errKey()!=null && v.errMsg().contains("【】")) {
			err = new StringBuilder(v.errMsg().replace("【】", "【"+map.get(v.errKey())+"】"));
			err.append("的").append(v.name());
		} else {
			err = new StringBuilder(v.name());
		}
		//必填
		if (v.require() && isNullOrEmpty(val)) {
			return err.append("未填写").toString();
		}
		if (!isNullOrEmpty(val)) {
			//最小长度
			if (!isNullOrEmpty(val) && v.minLength() != null && v.minLength()>=0 && val.length() < v.minLength()) {
				return err.append("长度小于").append(v.minLength()).toString();
			}
			//最大长度
			if (!isNullOrEmpty(val) && v.maxLength() != null && v.maxLength()>=0 && val.length() > v.maxLength()) {
				return err.append("长度超过").append(v.maxLength()).toString();
			}
			//类型判断
			Class<?> clazz = v.clazz();
			if (clazz != null) {
				try {
					if("java.lang.Float".equals(clazz.getName())){
						float num = Float.parseFloat(val);
						if(v.minNum() != null && num < v.minNum()) {
							return err.append("数字超出范围").toString();
						}
						if(v.maxNum() != null && num > v.maxNum()) {
							return err.append("数字超出范围").toString();
						}
					}else if("java.lang.Double".equals(clazz.getName())){
						double num = Double.parseDouble(val);
						if(v.minNum() != null && num < v.minNum()) {
							return err.append("数字超出范围").toString();
						}
						if(v.maxNum() != null && num > v.maxNum()) {
							return err.append("数字超出范围").toString();
						}
					}else if("java.lang.Long".equals(clazz.getName())) {
						long num = Long.parseLong(val);
						if(v.minNum() != null && num < v.minNum()) {
							return err.append("数字超出范围").toString();
						}
						if(v.maxNum() != null && num > v.maxNum()) {
							return err.append("数字超出范围").toString();
						}
					}else if("java.lang.Integer".equals(clazz.getName())) {
						int num = Integer.parseInt(val);
						if(v.minNum() != null && num < v.minNum()) {
							return err.append("数字超出范围").toString();
						}
						if(v.maxNum() != null && num > v.maxNum()) {
							return err.append("数字超出范围").toString();
						}
					} else if("java.util.Date".equals(clazz.getName())) {
						if(v.dateFormat()==null) {
							v.dateFormat("yyyy-MM-dd");
						}
						SimpleDateFormat sdf = new SimpleDateFormat(v.dateFormat());  
						sdf.parse(val);
					} else if("java.lang.String".equals(clazz.getName())) {
						
					}
				} catch(Exception e){	//类型转换失败
					return err.append("类型转换失败，目标类型[ ").append(clazz.getName()).append(" ]").toString();
				}
				//-类型判断完毕
				//正则匹配
				if(v.reg() != null && !v.reg().matcher(val).matches()) {	//正则
					return err.append("格式不正确").toString();
				}
			
			}
		}
		
		return null;
	}

}
