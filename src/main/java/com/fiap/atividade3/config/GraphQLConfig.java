package com.fiap.atividade3.config;

import org.springframework.context.annotation.Configuration;

/**
 * GraphQL configuration for the hospital management system
 * Using Spring Boot's auto-configuration for GraphQL
 */
@Configuration
public class GraphQLConfig {
    // Spring Boot auto-configures GraphQL endpoints automatically
    // /graphql endpoint is available by default
    // /graphiql endpoint is available when spring.graphql.graphiql.enabled=true
}
