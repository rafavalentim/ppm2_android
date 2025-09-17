package br.com.correios.ppm.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.login.presentation.AutenticacaoUiState
import br.com.correios.ppm.login.presentation.LoginViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DefaultTextFieldCorreios(
    text: StringResource,
    viewModel: LoginViewModel,
    autenticacaoUiState: AutenticacaoUiState,
    modifier: Modifier = Modifier
) {

    var cont by mutableStateOf(0)

    var username: String? by remember { mutableStateOf("") }

    if(cont == 0){
        username = viewModel.preencherViewComUltimoUsuarioLogado()
        autenticacaoUiState.aut?.usuario = username
        cont++
    }

    TextField(
        value = username.toString(),
        onValueChange = {
            username = it
            autenticacaoUiState.aut?.usuario = it
        },
        modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(4.dp)
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background
        ),
        placeholder = {
            Text(
                stringResource(text),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}


@Composable
fun PasswordTextFieldCorreios(
    autenticacaoUiState: AutenticacaoUiState,
    modifier: Modifier = Modifier.padding(start = 0.dp, top = 16.dp, end = 0.dp, bottom = 4.dp)
) {

    if(autenticacaoUiState.aut?.senha == null){
        autenticacaoUiState.aut?.senha = ""
    }

    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = autenticacaoUiState.aut?.senha.toString(),
        onValueChange = { autenticacaoUiState.aut?.senha = it },
        label = {
            Text("Senha",
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        modifier = modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(4.dp)
            .fillMaxWidth()
            .height(56.dp),

        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(
                onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                    tint = MaterialTheme.colorScheme.primary //mudar a cor do ícone.
                )
            }
        },
        singleLine = true
    )
}


@Composable
fun SwitchWithIconExample(viewModel: LoginViewModel) {
    var checked by remember { mutableStateOf(true) }

    Row {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Lembrar login",
                modifier = Modifier.align(Alignment.CenterStart),
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    viewModel.setRememberLogin(checked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun DefaultButton(description: String, viewModel: LoginViewModel){
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                //viewModel.onLoginClick() //IMPLEMENTAR DEPOIS.
            },
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 10.dp,
                disabledElevation = 4.dp
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = description)
        }
    }
}

@Composable
fun TextVersioApp(description: String){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ){
        Text(
            text = description,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomCenter))

    }
}



