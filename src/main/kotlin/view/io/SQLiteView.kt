package view.io

import GraphScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import viewModel.SearchScreenSQlite.SQLiteSearchScreenViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import kotlin.collections.filter
import kotlin.text.contains
import kotlin.text.isBlank

@Composable
fun SQLiteView(viewmodel: SQLiteSearchScreenViewModel,
               onDismissRequest: () -> Unit, navigator: Navigator
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        ) {
            val graphs = remember { viewmodel.graphList.toMutableStateList() }
            var searchQuery by remember { mutableStateOf("") }
            var expandedMenuId by remember { mutableStateOf(-1) }

            val filteredNames = remember(searchQuery, graphs) {
                if (searchQuery.isBlank()) graphs
                else graphs.filter { it.contains(searchQuery) }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("SearchGraphs") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredNames) { name ->
                        Button(
                            onClick = {
                                onDismissRequest()
                                val model = viewmodel.loadGraph(name)
                                navigator.push(GraphScreen(model!!.first, model.second))
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = name)
                        }
                    }
                }
            }
        }
    }
}