package com.example.jettipapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.util.calculateTotalPerPerson
import com.example.jettipapp.util.calculateTotalTip
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {

            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()) { innerPadding ->
            BuildBody(modifier = Modifier.padding(innerPadding))

        }

    }
}

@Composable
fun BuildBody(modifier: Modifier){
    Column (modifier = modifier){
        MainContent()
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun TopHeader(totalPerPerson: Double = 0.0,  modifier: Modifier = Modifier){
    Surface(modifier = modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(15.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp)))
        , color = Color(0xFFE9D7F7)
//        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
    ) {
        Column (
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
                )
            Text(text = "$${total}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
                )
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier){
    val splitByState = remember {
        mutableStateOf(1)
    }

    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmaountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    BillForm(
        splitByState = splitByState,
        range = range,
        tipAmaountState = tipAmaountState,
        totalPerPersonState = totalPerPersonState
    ) { billAmt ->
    }
}

@Composable
fun BillForm(modifier: Modifier = Modifier,
             range: IntRange =  1..100,
             splitByState: MutableState<Int>,
             tipAmaountState: MutableState<Double>,
             totalPerPersonState: MutableState<Double>,
             onValChange: (String) -> Unit){
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()

    TopHeader(totalPerPerson = totalPerPersonState.value)
    Surface(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
        , shape = RoundedCornerShape(corner = CornerSize(8.dp))
        , border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column (
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
            ){
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if(!validState) return@KeyboardActions
                    //Todo - onvaluechanged
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if(validState){
                Row (
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start,
                    )
                {
                    Text(text = "Split", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                        ){
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                if(splitByState.value > 1){
                                    splitByState.value--
                                }else{
                                    splitByState.value = 1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill =  totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage
                                )
                            }
                        )
                        Text(text = "${splitByState.value}",modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp))
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if(splitByState.value < range.last){
                                    splitByState.value++
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill =  totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage
                                )
                            }
                        )
                    }
                }
                Row (
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 12.dp)
                ){
                    Text(text = "Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(text = "$ ${tipAmaountState.value}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercentage %")
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 5,
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.floatValue = newVal
                            tipAmaountState.value = calculateTotalTip(
                               totalBill =  totalBillState.value.toDouble(),
                                tipPercentage = tipPercentage)

                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBill =  totalBillState.value.toDouble(),
                                splitBy = splitByState.value,
                                tipPercentage = tipPercentage
                            )
                        },
                    )
                }
            }else{
                Box{}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {

        }
    }
}