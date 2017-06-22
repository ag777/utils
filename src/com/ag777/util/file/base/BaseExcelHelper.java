package com.ag777.util.file.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ag777
 * @Description excel验证辅助基类,子类定义验证方法
 * Time: created at 2017/6/19. last modify at 2017/6/19.
 * Mark: 
 */
public abstract class BaseExcelHelper {
	
	protected abstract List<List<Map<String, String>>> readExcel(String path, List<List<String>> titles, boolean isIgnoreFirstRow) throws Exception;
	
	protected abstract String validate(Map<String, String> item);	//验证列表项
	
	protected abstract List<List<String>> titles();

	/**
	 * 验证并获取数据列表
	 * @param path
	 * @param index
	 * @return
	 * @throws Exception e.getMessage() 即错误提示信息
	 */
	public List<Map<String, Object>> getDataList(String path, int index) throws Exception {
		
		try {
			List<List<Map<String, String>>> resultList = readExcel(path, titles(), true);
			if(resultList.isEmpty()) {	//解析失败
				throw new Exception("文件内容不正确");
			}else {	//解析成功
				List<Map<String, String>> list = resultList.get(index);
				List<Map<String, Object>> data = handleList(list, titles().get(index));
				
				return data;
			}
			
		} catch (Exception e) {
			throw e;
		}
//		finally {
//			File f = new File(path);
//			if(f.exists() && f.isFile()) {
//				f.delete();
//			}
//		}
		
	}

	/**
	 * 处理从excel中解析出的数据,List<Map<String, String>> list代表一页的数据,Map<String, String>为一行的
	 * @param list
	 */
	private List<Map<String, Object>> handleList(List<Map<String, String>> list, List<String> titles) throws Exception{
		List<Map<String, Object>> result = new ArrayList<>();
		
		for (Map<String, String> item : list) {
			if(isMapEmpty(item)) {
				continue;
			}
			
			String errMsg = validate(item);
			if(errMsg != null) {	//验证数据不通过
				throw new Exception(errMsg);
			}else {		//数据验证通过
				Map<String,Object> resultItem = new HashMap<>();
				for (String title : titles) {
					if(item.containsKey(title)) {
						resultItem.put(title, item.get(title));
					}
				}
				result.add(resultItem);
			}
		}
		
		return result;
	}

	/*内部工具方法*/
	
	protected static boolean isNullOrEmpty(String str) {
		if(str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}
	private static boolean isMapEmpty(Map<String, String> map) {
		boolean flag = true;
		Iterator<String> itor = map.keySet().iterator();
		while(itor.hasNext()) {
			String key = itor.next();
			if(!isNullOrEmpty(map.get(key))) {
				flag=false;
				break;
			}
		}
		return flag;
	}

}
