package com.pdt.core.mybatis.bean;

public class Function {
	private String sqltype;  //sql的类型,计划在xml读取有四种情况
    private String funcName;  // 方法名
    private String sql;       //执行的sql语句
    private String resultType;  // 返回类型
	private String setGenericReturnType;

	public String getSqltype() {
		return sqltype;
	}

	public void setSqltype(String sqltype) {
		this.sqltype = sqltype;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getSetGenericReturnType() {
		return setGenericReturnType;
	}

	public void setSetGenericReturnType(String setGenericReturnType) {
		this.setGenericReturnType = setGenericReturnType;
	}
}
