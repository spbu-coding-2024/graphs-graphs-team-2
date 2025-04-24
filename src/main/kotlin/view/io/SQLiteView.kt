package view.io

import GraphScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.DrawerDefaults.backgroundColor
import androidx.compose.material.DrawerDefaults.shape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import view.components.CoolColors
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
            backgroundColor = CoolColors.Gray
        ) {
            var nameForConfirm by remember { mutableStateOf("") }
            var showDialog by remember { mutableStateOf(false) }
            val graphs = remember { viewmodel.graphList.toMutableStateList() }
            var searchQuery by remember { mutableStateOf("") }
            var expandedMenuId by remember { mutableStateOf(-1) }

            val filteredNames = remember(searchQuery, graphs) {
                if (searchQuery.isBlank()) graphs
                else graphs.filter { it.contains(searchQuery, ignoreCase = true) }
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
                        Box(contentAlignment = Alignment.Center) {
                            Button(
                                onClick = {
                                    onDismissRequest()
                                    val model = viewmodel.loadGraph(name)
                                    navigator.push(GraphScreen(model!!.first, model.second))
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                                    Text(text = name)
                                    IconButton(

                                        onClick = {
                                            showDialog = true
                                            nameForConfirm = name
                                        },

                                    ){
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                        )
                                    }
                                }
                            }
                            
                        }
                    }
                }
            }
            if(showDialog) {
                confirmationDialog ({ showDialog = false },
                    {viewmodel.deleteGraph(nameForConfirm)
                        graphs.remove(nameForConfirm)
                        showDialog=false},
                    nameForConfirm)
            }
        }
    }
}

@Composable
fun confirmationDialog(onDismiss: () -> Unit,onYesClick: () -> Unit,name:String) {
    Dialog(onDismissRequest = onDismiss) {
        Card (modifier = Modifier
            .width(300.dp)
            .height(200.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            backgroundColor = CoolColors.Gray ){
            Column (modifier = Modifier.fillMaxSize()) {
                Text("Are you sure you want to delete this graph?",
                        modifier = Modifier.padding(10.dp),
                    fontSize = 30.sp,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    color = CoolColors.White)
                Row(modifier = Modifier.fillMaxWidth().height(150.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = onYesClick,modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text("Yes")
                    }
                    Button(onClick = onDismiss,modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text("No")
                    }
                }
            }
        }
    }
}