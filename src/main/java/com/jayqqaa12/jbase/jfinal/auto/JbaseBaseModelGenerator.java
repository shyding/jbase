package com.jayqqaa12.jbase.jfinal.auto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class JbaseBaseModelGenerator extends BaseModelGenerator {

	protected String importTemplate = "import com.jayqqaa12.jbase.jfinal.ext.model.Model;%n"
			+ "import com.jfinal.plugin.activerecord.IBean;%n%n";

	protected String setterTemplate = "\tpublic M %s(Object %s) {%n" + "\t\t return set(\"%s\", %s);%n" + "\t}%n%n";

	protected String classDefineTemplate = 
			"/**%n" + " * Generated by Jbase, do not modify this file.%n" + " */%n"
			+ "@SuppressWarnings(\"serial\")%n"
			+ "public abstract class %s<M extends %s<M>> extends Model<M> implements IBean {%n%n";

	protected String attrTemplate = "\tpublic static final String %s = \"%s\"; %n";
	
//	protected String setTableNameTempate="\tpublic  %s(){%n"
//			+ " \t\t this.TABLENAME= \"%s\"; "
//			+ "\t}%n%n";

	public JbaseBaseModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
		super(baseModelPackageName, baseModelOutputDir);
	}


	
	protected void genBaseModel(TableMeta tableMeta) {
		StringBuilder ret = new StringBuilder();
		genPackage(tableMeta, ret);
		genImport(ret);
		genClassDefine(tableMeta, ret);

		genTableName(tableMeta,ret);
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genAttrName(columnMeta, ret);

		}
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genSetMethodName(columnMeta, ret);
			genGetMethodName(columnMeta, ret);
		}
		ret.append(String.format("}%n"));
		tableMeta.baseModelContent = ret.toString();
	}

	protected void genTableName(TableMeta tableMeta, StringBuilder ret) {
		
//		ret.append(String.format(setTableNameTempate, tableMeta.baseModelName,tableMeta.name));
		
	}



	protected void genAttrName(ColumnMeta columnMeta, StringBuilder ret) {

		String attr = String.format(attrTemplate, columnMeta.name.toUpperCase(), columnMeta.name);

		ret.append(attr);
	}

	@Override
	protected void genImport(StringBuilder ret) {
		ret.append(String.format(this.importTemplate));
	}

	@Override
	protected void genSetMethodName(ColumnMeta columnMeta, StringBuilder ret) {
		String setterMethodName = "set" + StrKit.firstCharToUpperCase(StrKit.toCamelCase(columnMeta.name));
		String attrName = StrKit.toCamelCase(columnMeta.name);

		String setter = String.format(setterTemplate, setterMethodName, attrName, columnMeta.name, attrName);
		ret.append(setter);
	}

	protected void genPackage(TableMeta tableMeta, StringBuilder ret) {

		String pre = tableMeta.name.toLowerCase().replace("_", "").replace(tableMeta.modelName.toLowerCase(), "");

		String pk = baseModelPackageName;
		if (StrKit.notBlank(pre)) {
			pk = baseModelPackageName + "." + pre;
		}
		ret.append(String.format(packageTemplate, pk));
	}

	protected void wirtToFile(TableMeta tableMeta) throws IOException {

		String pre = tableMeta.name.toLowerCase().replace("_", "").replace(tableMeta.modelName.toLowerCase(), "");

		String outDir = baseModelOutputDir;
		if (StrKit.notBlank(pre)) {
			outDir = baseModelOutputDir + File.separator + pre;
		}

		File dir = new File(outDir);
		if (!dir.exists()) dir.mkdirs();

		String target = outDir + File.separator + tableMeta.baseModelName + ".java";
		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.baseModelContent);
		} finally {
			fw.close();
		}
	}
}