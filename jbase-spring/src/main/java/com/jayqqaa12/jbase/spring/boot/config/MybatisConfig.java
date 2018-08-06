package com.jayqqaa12.jbase.spring.boot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by 12 on 2018/1/26.
 */
@Configuration
public class MybatisConfig {

    @Autowired
    SqlSessionFactory sqlSessionFactory;


    /**
     * 注册enum 类型 都使用ordinal 表示
     */
    @PostConstruct
    public void specialTypeHandlerRegistry() {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        //FIXME 修改后看看是否生效
        typeHandlerRegistry.register(EnumOrdinalTypeHandler.class);
        
    }
}
