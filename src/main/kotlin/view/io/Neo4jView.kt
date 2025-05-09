package view.io

import GraphScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import kotlin.invoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import view.components.CoolColors
import view.components.PurpleButton
import viewModel.graph.GraphViewModel
import viewModel.io.Neo4jViewModel

@Composable
fun Neo4jView(
    flagOfWrite: Boolean,
    graphViewModel: GraphViewModel?,
    viewModel: Neo4jViewModel,
    navigator: Navigator,
    dismissRequest: () -> Unit,
    isError: () -> Unit,
    errorMessage: () -> Unit,
    onLoading: () -> Unit,
    offLoading: () -> Unit,
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth().height(500.dp).padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().background(CoolColors.DarkGray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Connect to Neo4j",
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    fontSize = 40.sp,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    color = CoolColors.DarkPurple,
                )

                OutlinedTextField(
                    viewModel.username.value,
                    { viewModel.username.value = it },
                    textStyle = TextStyle(fontSize = 32.sp, color = CoolColors.DarkPurple),
                    modifier = Modifier.width(400.dp),
                    label = { Text("username", fontSize = 28.sp, color = CoolColors.DarkPurple) },
                )
                OutlinedTextField(
                    viewModel.password.value,
                    { viewModel.password.value = it },
                    textStyle = TextStyle(fontSize = 32.sp, color = CoolColors.DarkPurple),
                    modifier = Modifier.width(400.dp),
                    label = { Text("password", fontSize = 28.sp, color = CoolColors.DarkPurple) },
                    visualTransformation =
                        if (viewModel.passwordVisible.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (viewModel.passwordVisible.value) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                        val description =
                            if (viewModel.passwordVisible.value) "Hide password"
                            else "Show password"

                        IconButton(
                            onClick = {
                                viewModel.passwordVisible.value = !viewModel.passwordVisible.value
                            }
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription = description,
                                tint = CoolColors.DarkPurple,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    },
                )
                Column(
                    modifier = Modifier.padding(top = 52.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PurpleButton(
                            onClick = dismissRequest,
                            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
                            text = "Back",
                            fontSize = 32.sp,
                            textPadding = 10.dp,
                        )
                        PurpleButton(
                            onClick = {
                                onLoading.invoke()
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (!flagOfWrite) {
                                        val model = viewModel.read()
                                        if (model != null) {
                                            navigator.push(GraphScreen(model.first, model.second))
                                        } else {
                                            isError.invoke()
                                            errorMessage.invoke()
                                        }
                                    } else {
                                        if (viewModel.write(graphViewModel)) {
                                            dismissRequest.invoke()
                                        } else {
                                            isError.invoke()
                                            errorMessage.invoke()
                                        }
                                    }
                                    offLoading.invoke()
                                }
                            },
                            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
                            text = "Confirm",
                            fontSize = 32.sp,
                            textPadding = 10.dp,
                        )
                    }
                }
            }
        }
    }
}
