package com.example.aquaspot.screens.components

import android.net.Uri
import android.util.Log
import android.widget.ImageButton
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.example.aquaspot.R
import com.example.aquaspot.ui.theme.buttonDisabledColor
import com.example.aquaspot.ui.theme.greyTextColor
import com.example.aquaspot.ui.theme.lightMailColor
import com.example.aquaspot.ui.theme.lightRedColor
import com.example.aquaspot.ui.theme.mainColor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.protobuf.Empty
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun loginImage(){
Box(modifier = Modifier
    .fillMaxWidth()
    .padding(20.dp),

    contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.loginimage),
            contentDescription = "Login Image",
            modifier = Modifier
                .width(210.dp)
                .height(210.dp)
        )
    }
}
@Composable
fun registerImage(
    selectedImageUri: MutableState<Uri?>,
    isError: MutableState<Boolean>

) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri.value == Uri.EMPTY) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .border(
                        if (isError.value) BorderStroke(2.dp, Color.Red) else BorderStroke(
                            0.dp,
                            Color.Transparent
                        )
                    )
                    .clip(RoundedCornerShape(70.dp)) // 50% border radius
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        } else {
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(70.dp)) // 50% border radius
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun headingText(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
        ),
        text = textValue
    )
}

@Composable
fun greyText(textValue: String){
    Text(style = TextStyle(
        color = greyTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}

@Composable
fun inputTextIndicator(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}

@Composable
fun customTextInput(
    isEmail: Boolean,
    isNumber: Boolean = false,
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = if(isEmail && !isNumber) KeyboardOptions(keyboardType = KeyboardType.Email) else if(!isEmail && isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}


@Composable
fun customRichTextInput(
    inputValue: MutableState<String>,
    inputText: String,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}


@Composable
fun customPasswordInput(
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
){
    var showPassword = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            trailingIcon = {
                IconButton(onClick = {
                    showPassword.value = !showPassword.value
                }) {
                    Icon(
                        imageVector = if(!showPassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            visualTransformation = if(!showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }else{
        Text(text = " ")
    }
}

@Composable
fun loginRegisterCustomButton(
    buttonText: String,
    isEnabled: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .height(50.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        enabled = isEnabled.value
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = buttonText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}


@Composable
fun customClickableText(
    firstText: String,
    secondText: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = firstText,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
        Text(
            text = secondText,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(start = 4.dp),
            style = TextStyle(
                fontSize = 12.sp,
                color = mainColor
            )
        )
    }
}


@Composable
fun customAuthError(
    errorText: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(lightRedColor)
        .height(50.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = errorText,
            style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun customImagePicker() {
    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = "Izaberi sliku")
        }
        Spacer(modifier = Modifier.height(10.dp))
        selectedImageUri.value?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun mapNavigationBar(
    searchValue: MutableState<String>,
    profileImage: String,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .shadow(
                    6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
        ){
            OutlinedTextField(
                value = searchValue.value,
                onValueChange = { newValue ->
                    searchValue.value = newValue
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_text),
                        style = TextStyle(
                            color = greyTextColor
                        )
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Search,
                        contentDescription = "",
                        tint = mainColor)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)
                .shadow(
                    6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center

        ){
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {

                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun mapFooter(
    openAddNewBeach: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)  // Set background color if necessary
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    spotColor = Color.Transparent
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Home,  // Replace with appropriate icon
                        contentDescription = "",
                        tint = greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Map,  // Replace with appropriate icon
                        contentDescription = "",
                        tint = greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(modifier = Modifier.size(70.dp))  // Spacer for search icon position
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.FormatListNumbered,
                        contentDescription = "",
                        tint = greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "",
                        tint = greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-30).dp)  // Negative offset to overlap the footer
                .size(90.dp)  // Adjust size as needed
        ) {
            IconButton(onClick = openAddNewBeach,
                modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.searchcomponent),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CustomCrowd(
    selectedOption: MutableState<Int>
){
    val customModifier = Modifier
        .fillMaxSize()
        .shadow(
            elevation = 20.dp,
            shape = RoundedCornerShape(10.dp),
            spotColor = Color.Transparent
        )
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(10.dp),
        )
        .background(
            Color.White,
            shape = RoundedCornerShape(10.dp),
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 0)
                    customModifier.background(Color.Green)
                else
                    customModifier.clickable {
                        selectedOption.value = 0
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Slabo")
            }
        }
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 1)
                    customModifier.background(Color.Yellow)
                else
                    customModifier.clickable {
                        selectedOption.value = 1
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Srednje")
            }
        }
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 2)
                    customModifier.background(Color.Red)
                else
                    customModifier.clickable {
                        selectedOption.value = 2
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Mnogo")
            }
        }
    }
}

@Composable
fun CustomImageForNewBeach(
    selectedImageUri: MutableState<Uri?>
){
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(vertical = 2.dp)
        .shadow(
            6.dp,
            shape = RoundedCornerShape(20.dp)
        )
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(20.dp)
        )
        .background(
            greyTextColor,
            shape = RoundedCornerShape(20.dp)
        ),
        contentAlignment = Alignment.Center
    ){
        if (selectedImageUri.value == Uri.EMPTY) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.uploadimage),
                    contentDescription = ""
                )
                Text(text = "Dodaj naslovnu sliku")
            }
        }else{
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun CustomGalleryForAddNewBeach(
    selectedImages: MutableState<List<Uri>>
) {
    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris != null) {
            selectedImages.value += uris
        }
    }

    LazyRow {
        if (selectedImages.value.size < 5) {
            item {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                        .height(100.dp)
                        .border(
                            1.dp,
                            greyTextColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .background(
                            greyTextColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { pickImagesLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.AddAPhoto, contentDescription = "")
                }
            }
        }
        items(selectedImages.value.size) { index ->
            val uri = selectedImages.value[index]
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .border(
                        1.dp,
                        Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .clickable { selectedImages.value -= uri },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}
