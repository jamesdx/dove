-- Route configuration table
CREATE TABLE IF NOT EXISTS route_config (
    id BIGSERIAL PRIMARY KEY,
    route_id VARCHAR(100) NOT NULL UNIQUE,
    service_name VARCHAR(100),
    path VARCHAR(200),
    host VARCHAR(200),
    headers JSONB,
    query_params JSONB,
    methods VARCHAR(100),
    uri VARCHAR(200) NOT NULL,
    predicates TEXT,
    filters TEXT,
    "order" INTEGER DEFAULT 0,
    metadata JSONB,
    enabled BOOLEAN DEFAULT true,
    weight INTEGER DEFAULT 100,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL
);

-- Route predicate table
CREATE TABLE IF NOT EXISTS route_predicate (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    args JSONB,
    "order" INTEGER DEFAULT 0,
    enabled BOOLEAN DEFAULT true,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    FOREIGN KEY (route_id) REFERENCES route_config(id)
);

-- Route filter table
CREATE TABLE IF NOT EXISTS route_filter (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    args JSONB,
    "order" INTEGER DEFAULT 0,
    enabled BOOLEAN DEFAULT true,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    FOREIGN KEY (route_id) REFERENCES route_config(id)
);

-- Audit log table
CREATE TABLE IF NOT EXISTS route_audit_log (
    id BIGSERIAL PRIMARY KEY,
    operation VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    user_id VARCHAR(100),
    user_ip VARCHAR(50),
    old_value JSONB,
    new_value JSONB,
    created_time TIMESTAMP NOT NULL
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_route_config_service_name ON route_config(service_name);
CREATE INDEX IF NOT EXISTS idx_route_config_path ON route_config(path);
CREATE INDEX IF NOT EXISTS idx_route_config_host ON route_config(host);
CREATE INDEX IF NOT EXISTS idx_route_predicate_route_id ON route_predicate(route_id);
CREATE INDEX IF NOT EXISTS idx_route_filter_route_id ON route_filter(route_id);
CREATE INDEX IF NOT EXISTS idx_route_audit_log_entity ON route_audit_log(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_route_audit_log_user ON route_audit_log(user_id);