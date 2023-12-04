package com.example.mad

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.example.mad.model.Equation
import com.example.mad.ui.theme.MADTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TruthTableApp()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TruthTableApp() {

    val equation = remember { mutableStateOf(Equation("-", "-", "?")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Truth Table") }
            )
        },
        content = {
            EquationScreen(LocalContext.current, equation)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Next") },
                onClick = {
                    equation.value = Equation(
                        generateRandomArgument(),
                        generateRandomArgument(),
                        generateRandomArgument()
                    )
                }
            )
        }
    )
}


@Composable
fun EquationScreen(context: Context, equation: MutableState<Equation>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ScreenTitle("Conjunction Table (AND)")
        EquationHeaders()
        EquationValues(equation.value)
        AnswerButtons(equation.value, onAnswerSelected = {
            equation.value = Equation("✅", "✅", "✅")
        }, context = context)
    }
}

@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun EquationHeaders() {
    Row {
        Text(
            text = "Arg 1",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Arg 2",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Answer",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EquationValues(equation: Equation) {
    Row {
        Text(
            text = equation.firstArg,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = equation.secondArg,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = equation.answer,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AnswerButtons(equation: Equation, onAnswerSelected: () -> Unit, context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                if (checkAnswers(equation)) {
                    onAnswerSelected()
                    informUser(context = context, msgId = R.string.correct)
                }else
                    informUser(context = context, msgId = R.string.incorrect)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("True")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {
                if (!checkAnswers(equation)) {
                    onAnswerSelected()
                } else
                    informUser(context = context, msgId = R.string.incorrect)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("False")
        }
    }
}

private fun checkAnswers(equation: Equation): Boolean {
    return if (equation.firstArg == "T" && equation.secondArg == "T") {
        equation.answer == "T"
    } else {
        equation.answer == "F"
    }
}


fun generateRandomArgument(): String {
    return if (Random.nextBoolean()) "F" else "T"
}

private fun informUser(context: Context, msgId: Int) {
    Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MADTheme {
        TruthTableApp()
    }
}