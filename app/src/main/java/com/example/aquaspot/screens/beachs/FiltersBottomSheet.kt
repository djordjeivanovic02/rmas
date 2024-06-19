package com.example.aquaspot.screens.beachs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaspot.data.Resource
import com.example.aquaspot.model.Beach
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.screens.components.CustomRateButton
import com.example.aquaspot.ui.theme.buttonDisabledColor
import com.example.aquaspot.ui.theme.lightGreyColor
import com.example.aquaspot.ui.theme.lightMainColor2
import com.example.aquaspot.ui.theme.mainColor
import com.example.aquaspot.ui.theme.redTextColor
import com.example.aquaspot.viewmodels.AuthViewModel
import com.example.aquaspot.viewmodels.BeachViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun FiltersBottomSheet(
    beachViewModel: BeachViewModel,
    viewModel: AuthViewModel,
    beaches: MutableList<Beach>,
    sheetState: ModalBottomSheetState,
    isFiltered: MutableState<Boolean>,
    filteredBeach: MutableList<Beach>
){
    viewModel.getAllUserData()
    val allUsersResource = viewModel.allUsers.collectAsState()

    val allUsersNames = remember {
        mutableListOf<String>()
    }
    var initialCheckedState = remember {
        mutableListOf<Boolean>()
    }
    val selectedOptions = remember {
        mutableStateOf(initialCheckedState)
    }

    val rangeValues = remember { mutableFloatStateOf(1000f) }

    val selectedCrowd = remember {
        mutableListOf<Int>()
    }

    allUsersResource.value.let {
        when(it){
            is Resource.Failure -> {}
            is Resource.Success -> {
                allUsersNames.clear()
                allUsersNames.addAll(it.result.map { user -> user.fullName})
                initialCheckedState = List(allUsersNames.size) { false }.toMutableList()
            }
            Resource.loading -> {}
            null -> {}
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Autor",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownWithCheckboxes(
            options = allUsersNames,
            initiallyChecked = selectedOptions.value,
            onSelectionChanged = { selectedOptions.value = it.toMutableList() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Gužva",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomCrowdSelector(selectedCrowd)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Distanca",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = rangeValues.floatValue.toBigDecimal().setScale(1, RoundingMode.UP).toString() + "m",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        RangeSliderExample(rangeValues = rangeValues)
        Spacer(modifier = Modifier.height(30.dp))
        CustomFilterButton {
            if(rangeValues.floatValue != 1000f){

            }
            if(selectedCrowd.isNotEmpty()) {
//                beaches.filter { it.crowd in selectedCrowd }
                Log.d("Filtered", selectedCrowd.toString())
                isFiltered.value = false
                isFiltered.value = true
                filteredBeach.clear()
                beaches.forEach{beach ->
                    if(beach.crowd in selectedCrowd) filteredBeach.add(beach)
                }
            }

            coroutineScope.launch {
                sheetState.hide()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        CustomResetFilters {
            isFiltered.value = true
            isFiltered.value = false

            coroutineScope.launch {
                sheetState.hide()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun DropdownWithCheckboxes(
    options: MutableList<String>,
    initiallyChecked: List<Boolean>,
    onSelectionChanged: (List<Boolean>) -> Unit
) {
    var expanded = remember { mutableStateOf(false) }
    var checkedStates = remember { mutableStateOf(initiallyChecked) }

    Column{
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded.value = !expanded.value })
                .background(lightGreyColor, RoundedCornerShape(4.dp))
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text("Izaberi autore", style = MaterialTheme.typography.body1)
            Icon(
                if (expanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown icon"
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            if(options.isNotEmpty())
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(onClick = {
                        checkedStates.value = checkedStates.value.toMutableList().apply {
                            this[index] = !this[index]
                        }
                        onSelectionChanged(checkedStates.value)
                    },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checkedStates.value[index],
                                onCheckedChange = {
                                    checkedStates.value = checkedStates.value.toMutableList().apply {
                                        this[index] = it
                                    }
                                    onSelectionChanged(checkedStates.value)
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(option)
                        }
                    }
                }
        }
    }
}

@Composable
fun CustomCrowdSelector(
    selected: MutableList<Int>
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CrowdOption("Slaba", 0, selected)
        CrowdOption("Umerena", 1, selected)
        CrowdOption("Velika", 2, selected)
    }
}

@Composable
fun CrowdOption(
    text: String,
    index: Int,
    selected: MutableList<Int>
) {
    val isSelected = remember { mutableStateOf(selected.contains(index)) }

    Box(
        modifier = Modifier
            .background(
                if (isSelected.value) lightGreyColor else Color.White,
                RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected.value) mainColor else lightGreyColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                if (isSelected.value) {
                    selected.remove(index)
                } else {
                    selected.add(index)
                }
                isSelected.value = !isSelected.value
            }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.People, contentDescription = "")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text)
        }
    }
}

@Composable
fun RangeSliderExample(
    rangeValues: MutableState<Float>
) {
    Slider(
        value = rangeValues.value,
        onValueChange = { rangeValues.value = it },
        valueRange = 0f..1000f,
        steps = 50,
        colors = SliderDefaults.colors(
            thumbColor = mainColor,
            activeTrackColor = lightMainColor2,
            inactiveTrackColor = lightGreyColor
        )
    )
}

@Composable
fun CustomFilterButton(
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Filtriraj",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun CustomResetFilters(
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = redTextColor,
            contentColor = Color.White,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Resetuj Filtere",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}