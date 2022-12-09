package com.ag777.util.db.interf;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库返回转换器
 * @author ag777 <837915770@vip.qq.com>
 * @Date 2022/12/9 9:34
 */
@FunctionalInterface
public interface ColConverter<T> {
    T apply(ResultSet rs) throws SQLException;
}
