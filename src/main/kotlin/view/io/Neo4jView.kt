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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.IconButton
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.ioNeo4j.ReadNeo4j
import model.Graph
import model.abstractGraph.AbstractVertex
import view.DataSystems
import view.components.CoolColors
import cafe.adriel.voyager.navigator.Navigator


@Composable
fun Neo4jView(
    Username: MutableState<String?>, Password: MutableState<String?>, onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize().background(CoolColors.DarkGray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Connect to Neo4j",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 40.sp,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    color = CoolColors.DarkPurple
                )
                val username = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                var passwordVisible = remember { mutableStateOf(false) }

                OutlinedTextField(
                    username.value,
                    { username.value = it },
                    textStyle = TextStyle(fontSize = 30.sp, color = CoolColors.DarkPurple),
                    modifier = Modifier.padding(15.dp).width(400.dp),
                    label = { Text("username", fontSize = 30.sp, color = CoolColors.DarkPurple) },
                )
                OutlinedTextField(
                    password.value,
                    { password.value = it },
                    textStyle = TextStyle(fontSize = 30.sp, color = CoolColors.DarkPurple),
                    modifier = Modifier.padding(15.dp).width(400.dp),
                    label = { Text("password", fontSize = 30.sp, color = CoolColors.DarkPurple) },
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible.value)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible.value) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                            Icon(
                                imageVector = image, contentDescription = description,
                                tint = CoolColors.DarkPurple, modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(30.dp),
                    ) {
                        Text("Back", fontSize = 32.sp, color = CoolColors.DarkPurple)
                    }
                    TextButton(
                        onClick = {
                            Username.value = username.value
                            Password.value = password.value
                        },
                        modifier = Modifier.padding(30.dp),
                    ) {
                        Text("Confirm", fontSize = 32.sp, color = CoolColors.DarkPurple)
                    }
                }
            }
        }
    }
}