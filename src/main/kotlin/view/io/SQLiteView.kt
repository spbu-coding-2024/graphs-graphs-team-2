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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import kotlin.collections.filter
import kotlin.text.contains
import kotlin.text.isBlank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import view.components.CoolColors
import view.components.PurpleButton
import viewModel.io.SQLiteSearchScreenViewModel

@Composable
fun SQLiteSearchView(
    viewmodel: SQLiteSearchScreenViewModel,
    onDismissRequest: () -> Unit,
    navigator: Navigator,
    onErrorRequest: () -> Unit,
    onLoading: () -> Unit,
    offLoading: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    var graphs: MutableList<String> by remember { mutableStateOf(mutableListOf<String>()) }
    scope.launch {
        isLoading = true
        graphs = viewmodel.graphList.toMutableStateList()
        isLoading = false
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth().height(500.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            backgroundColor = CoolColors.Gray,
        ) {
            if (!isLoading) {
                var nameForConfirm by remember { mutableStateOf("") }
                var showDialog by remember { mutableStateOf(false) }

                var searchQuery by remember { mutableStateOf("") }

                val filteredNames =
                    remember(searchQuery, graphs) {
                        if (searchQuery.isBlank()) graphs
                        else graphs.filter { it.contains(searchQuery, ignoreCase = true) }
                    }
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("SearchGraphs") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(filteredNames) { name ->
                            Box(contentAlignment = Alignment.Center) {
                                Button(
                                    onClick = {
                                        onLoading.invoke()
                                        CoroutineScope(Dispatchers.IO).launch {
                                            onDismissRequest()
                                            val model = viewmodel.loadGraph(name)
                                            if (model == null || model.first.vertices.isEmpty()) {
                                                onErrorRequest()
                                            } else {
                                                navigator.push(
                                                    GraphScreen(model.first, model.second)
                                                )
                                            }
                                            offLoading.invoke()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        Text(text = name)
                                        IconButton(
                                            onClick = {
                                                showDialog = true
                                                nameForConfirm = name
                                            }
                                        ) {
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
                if (showDialog) {
                    confirmationDialog(
                        { showDialog = false },
                        {
                            viewmodel.deleteGraph(nameForConfirm)
                            graphs.remove(nameForConfirm)
                            showDialog = false
                        },
                        nameForConfirm,
                    )
                }
            }
            if (isLoading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun confirmationDialog(onDismiss: () -> Unit, onYesClick: () -> Unit, name: String) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.width(300.dp).height(200.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            backgroundColor = CoolColors.Gray,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Are you sure you want to delete this graph?",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 30.sp,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    color = CoolColors.White,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = onYesClick, modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text("Yes")
                    }
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text("No")
                    }
                }
            }
        }
    }
}

@Composable
fun SQLiteNameInputView(name: MutableState<String?>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth().height(400.dp).padding(20.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            backgroundColor = CoolColors.Gray,
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                Text(
                    text = "Please enter a name for the graph",
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    fontSize = 40.sp,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    color = CoolColors.DarkPurple,
                )
                Spacer(modifier = Modifier.height(16.dp))
                val input = remember { mutableStateOf("") }
                OutlinedTextField(
                    input.value,
                    { input.value = it },
                    textStyle = TextStyle(fontSize = 32.sp, color = CoolColors.DarkPurple),
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Name for the graph", fontSize = 28.sp, color = CoolColors.DarkPurple)
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    PurpleButton(
                        onClick = onDismiss,
                        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)).weight(1f),
                        text = "Cancel",
                        fontSize = 32.sp,
                        textPadding = 10.dp,
                    )
                    PurpleButton(
                        onClick = { name.value = input.value },
                        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)).weight(1f),
                        text = "OK",
                        fontSize = 32.sp,
                        textPadding = 10.dp,
                    )
                }
            }
        }
    }
}
