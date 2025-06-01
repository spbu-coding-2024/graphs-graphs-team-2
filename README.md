# Graph Visualizer
![tests](https://github.com/spbu-coding-2024/graphs-graphs-team-2/actions/workflows/build.yml/badge.svg)
## About
This application allows you to visualize algorithms on a graph from a database [SQLiTe](https://github.com/spbu-coding-2024/graphs-graphs-team-2/blob/main/src/main/kotlin/io/SQLiteExposed/SQLiteFormat.md)/[Neo4j](https://github.com/spbu-coding-2024/graphs-graphs-team-2/blob/main/src/main/kotlin/io/ioNeo4j/Neo4jFormat.md)) or a file ([JSON format](https://github.com/spbu-coding-2024/graphs-graphs-team-2/blob/main/src/main/kotlin/io/ioJSON/JSONFormat.md)).
## Features
- Load (store) graph from (to) database / JSON-file
#### Algorithms:
- Placement (Force-Atlas 2)
- Find key vertices (Harmonic Centrality)
- Find communities (Louvain)
- Shortest path between 2 vertices (Dijkstra / Ford-Bellman)
- Find loop for vertex

    ***For undirected graphs:***
- Find bridges
- Minimal spanning tree (***weighted***)

    ***For directed graphs:***
- Find strongly connected components (Prim)
## Quick start

Install graph visualizer with github

```bash
  git clone https://github.com/spbu-coding-2024/graphs-graphs-team-2.git
  cd graphs-graphs-team-2/
```
    
Run app
```bash
  ./gradlew run
```
## Contributors

- [@talubik](https://github.com/talubik)
- [@g4rry1](https://github.com/g4rry1)
- [@semrosin](https://www.github.com/semrosin)


## License

[GNU GPL-3.0](https://choosealicense.com/licenses/mit/)

This app uses the *GSON* and *Spring data Neo4j* libraries, distributed with [Apache License 2.0](https://choosealicense.com/licenses/apache-2.0/)
