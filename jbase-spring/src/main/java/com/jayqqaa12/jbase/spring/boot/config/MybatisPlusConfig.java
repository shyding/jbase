package com.jayqqaa12.jbase.spring.boot.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.jayqqaa12.jbase.spring.boot.base.Profiles;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@DependsOn("flywayInitializer")
@Configuration
@ConditionalOnClass(MybatisConfiguration.class)
public class MybatisPlusConfig {


	/**
	 * config-plus SQL执行效率插件【生产环境可以关闭】
	 */
	@Bean
	@Profile(Profiles.NOT_PRODUCTION)
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor();
	}



	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor(){
		return  new OptimisticLockerInterceptor();
	}



	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//		paginationInterceptor.setLocalPage(Boolean.TRUE);
		return paginationInterceptor;
	}
}
