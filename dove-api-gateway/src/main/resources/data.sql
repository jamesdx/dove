-- Insert default route configuration
INSERT INTO route_config (
    route_id,
    service_name,
    path,
    uri,
    predicates,
    filters,
    "order",
    metadata,
    enabled,
    weight,
    created_time,
    updated_time
) VALUES (
    'default_route',
    'default-service',
    '/**',
    'lb://default-service',
    '[]',
    '[]',
    0,
    '{"version": "1.0", "description": "Default route"}',
    true,
    100,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (route_id) DO NOTHING;

-- Insert test route configuration
INSERT INTO route_config (
    route_id,
    service_name,
    path,
    uri,
    predicates,
    filters,
    "order",
    metadata,
    enabled,
    weight,
    created_time,
    updated_time
) VALUES (
    'test_route',
    'test-service',
    '/test/**',
    'lb://test-service',
    '[]',
    '[]',
    1,
    '{"version": "1.0", "description": "Test route"}',
    true,
    100,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (route_id) DO NOTHING;