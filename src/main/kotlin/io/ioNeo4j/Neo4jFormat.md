# Neo4j Format
To load your graph from neo4j, provide it in this format:


**1. Vertices**

To create vertex you can use this cypher query:
```
CREATE (v:Vertex {
  label: "ExampleVertex",
  x: "1.0",
  y: "2.0",
  isDirected: false,
  isWeighted: true
})
RETURN v
```
where
- **"label"** is the vertex name
- **"x"** and **"y"** is the vertex coordinates. *You can not specify it or specify its *"value"* field as null, then they will be **randomized**.*

**3. Edges**

To create edge you can use this cypher query:
```
MATCH (v1:Vertex {label: "ExampleVertex1"})
MATCH (v2:Vertex {label: "ExampleVertex2"})
CREATE (v1)-[e:Edge {
  label: "ExampleEdge",
  weight: "3.5"
}]->(v2)
RETURN e

```
where
- **"label"** is the edge name (type of the relations)
- **"weight"** is the edge wieght. *You can not specify it (or specify as null), if your graph is unweighted or edge weight is 1.0. Then it will be considered as **1.0***