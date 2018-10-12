package com.fiberhome.mapps.mssdk;

public class ConfigParam {
	/**
	 * 参数key
	 */
	private String key;

	/**
	 * 属性名
	 */
	private String name;

	/**
	 * 参数编辑类型：text,radio,checkbox，文本、单选、多选
	 */
	private String type;

	/**
	 * 值列宽度
	 */
	private int size;

	/**
	 * 参数说明
	 */
	private String remark;

	/**
	 * 
	 */
	private String regex;

	/**
	 * 
	 */
	private String options;

	/**
	 * 参数分组名称
	 */
	private String group;
	
	/**
	 * 缺省值
	 */
	private String def;
	
	/**
	 * 排序序号
	 */
	private String sort;
	
	/**
	 * 返回数据时是否以默认值装载
	 */
	private String isDef;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getIsDef() {
		return isDef;
	}

	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}

}
