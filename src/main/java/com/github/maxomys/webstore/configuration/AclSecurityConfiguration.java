package com.github.maxomys.webstore.configuration;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
public class AclSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(DataSource dataSource, AclCache aclCache) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService(dataSource, aclCache));
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        return expressionHandler;
    }

    @Bean
    public JdbcMutableAclService aclService(DataSource dataSource, AclCache aclCache) {
        return new JdbcMutableAclService(dataSource, lookupStrategy(dataSource, aclCache), aclCache);
    }

    @Bean
    public LookupStrategy lookupStrategy(DataSource dataSource, AclCache aclCache) {
        return new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    AclCache aclCache(CacheManager cacheManager) {
        Cache cache = cacheManager.getCache("acl_cache");
        return new SpringCacheBasedAclCache(cache, permissionGrantingStrategy(), aclAuthorizationStrategy());

    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

}
