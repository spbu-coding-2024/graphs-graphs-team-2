package io.ioNeo4j

import org.springframework.data.neo4j.repository.Neo4jRepository

interface ReadRepositoryNeo4j : Neo4jRepository<VertexNeo4j, Long>

interface WriteRepositoryNeo4j : Neo4jRepository<VertexNeo4j, Long>
