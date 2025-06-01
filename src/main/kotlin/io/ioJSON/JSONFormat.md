# JSON format
To load your graph from a JSON file, provide it in this format:

**1. Type**
- If your graph is directed, add key *"direction"* with boolean value.
- If your graph is weighted, add key *"weight"* with boolean value.
```
"direction":true,"weight":true
```
*You can not specify these keys, then they will be considered as **false***

**2. Vertices array**

After type you should specify vertices of your graph, performed as the array with key **"vertices"**, consisting of objects of the type:
```
"0":{"label":"Addam-Marbrand","x":{"value":670.0},"y":{"value":590.0}}
```
where 
- the **key** is the vertex ID
- **"label"** is the vertex name
- **"x"** and **"y"** is the vertex coordinates. *You can not specify it or specify its *"value"* field as null, then they will be **randomized**.*

**3. Edges array**

At the end you should specify edges of your graph, performed as the array with key **"edges"**, containing objects in the format:
```
"0":{"label":"Daughter","from":0,"to":1,"weight":9.0}
```
where
- the **key** is the edge ID
- **"label"** is the edge name (type of the relations)
- **"from"** is the ID of first vertex
- **"to"** is the ID of first vertex
- **"weight"** is the edge wieght. *You can not specify it (or specify as null), if your graph is unweighted or edge weight is 1.0. Then it will be considered as **1.0***
