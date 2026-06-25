package br.com.correios.ppm.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.login.presentation.AutenticacaoUiState
import br.com.correios.ppm.login.presentation.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DefaultTextFieldCorreios(
    text: StringResource,
    viewModel: LoginViewModel,
    autenticacaoUiState: AutenticacaoUiState,
    modifier: Modifier = Modifier
) {
    // guarda o texto entre recomposições e rotações
    var username by rememberSaveable { mutableStateOf("") }

    // carrega o último usuário UMA vez, sem travar recomposição
    LaunchedEffect(Unit) {
        val last = viewModel.preencherViewComUltimoUsuarioLogado().orEmpty()
        username = last
        // avise o VM (não mutar estado da UI diretamente)
        viewModel.onUsernameChanged(last)
    }

    TextField(
        value = username,
        onValueChange = {
            username = it
            viewModel.onUsernameChanged(it) // propague para o VM
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
    var password by rememberSaveable {
        mutableStateOf(autenticacaoUiState.aut?.senha.orEmpty())
    }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = {
            password = it                    // segura o texto na UI
            autenticacaoUiState.aut?.senha = it // (opcional) propaga pro seu estado atual
        },
        label = { Text("Senha", color = MaterialTheme.colorScheme.onBackground) },
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
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
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
fun DefaultButton(
    description: String,
    viewModel: LoginViewModel){

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Colete os eventos da ViewModel e mostre o snackbar
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                }

                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {

                viewModel.onLoginClick()

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



