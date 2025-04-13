package io

import org.springframework.data.neo4j.repository.Neo4jRepository

public interface GraphRepository : Neo4jRepository<VertexNeo4j, Long>