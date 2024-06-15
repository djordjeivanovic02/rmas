package com.example.aquaspot.screens.beachs

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaspot.R
import com.example.aquaspot.data.Resource
import com.example.aquaspot.screens.components.CustomCrowd
import com.example.aquaspot.screens.components.CustomGalleryForAddNewBeach
import com.example.aquaspot.screens.components.CustomImageForNewBeach
import com.example.aquaspot.screens.components.customRichTextInput
import com.example.aquaspot.screens.components.headingText
import com.example.aquaspot.screens.components.inputTextIndicator
import com.example.aquaspot.screens.components.loginRegisterCustomButton
import com.example.aquaspot.ui.theme.greyTextColor
import com.example.aquaspot.viewmodels.BeachViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun AddNewBeachBottomSheet(
    beachViewModel: BeachViewModel?,
    location: MutableState<LatLng?>
) {
    val beachFlow = beachViewModel?.beachFlow?.collectAsState()
    val inputDescription = remember {
        mutableStateOf("")
    }
    val isDescriptionError = remember {
        mutableStateOf(false)
    }
    val descriptionError = remember {
        mutableStateOf("Ovo polje je obavezno")
    }
    val selectedOption = remember {
        mutableStateOf(0)
    }
    val buttonIsEnabled = remember {
        mutableStateOf(true)
    }
    val buttonIsLoading = remember {
        mutableStateOf(false)
    }

    val selectedImage = remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
    val selectedGallery = remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 20.dp)
    ) {
        item{headingText(textValue = stringResource(id = R.string.add_new_beach_heading))}
        item{Spacer(modifier = Modifier.height(20.dp))}
        item{ CustomImageForNewBeach(selectedImageUri = selectedImage)}
        item{Spacer(modifier = Modifier.height(20.dp))}
        item{inputTextIndicator(textValue = "Opis")}
        item{Spacer(modifier = Modifier.height(5.dp))}
        item{customRichTextInput(inputValue = inputDescription, inputText = "Unesite opis", isError = isDescriptionError, errorText = descriptionError)}
        item{Spacer(modifier = Modifier.height(20.dp))}
        item{inputTextIndicator(textValue = "Gužva")}
        item{Spacer(modifier = Modifier.height(5.dp))}
        item{CustomCrowd(selectedOption)}
        item{Spacer(modifier = Modifier.height(20.dp))}
        item{inputTextIndicator(textValue = "Galerija")}
        item{Spacer(modifier = Modifier.height(5.dp))}
        item{CustomGalleryForAddNewBeach(selectedImages = selectedGallery)}
        item{Spacer(modifier = Modifier.height(20.dp))}
        item{loginRegisterCustomButton(buttonText = "Dodaj plazu", isEnabled = buttonIsEnabled, isLoading = buttonIsLoading) {
            buttonIsLoading.value = true
            beachViewModel?.saveBeachData(
                description = inputDescription.value,
                crowd = selectedOption.value,
                mainImage = selectedImage.value!!,
                galleryImages = selectedGallery.value,
                location = location
            )
        }}
        item{Spacer(modifier = Modifier.height(5.dp))}
//        Spacer(modifier = Modifier.height(100.dp))
    }
    beachFlow?.value.let {
        when (it){
            is Resource.Failure -> {
                buttonIsLoading.value = false
                val context = LocalContext.current
                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
            }
            is Resource.loading -> {

            }
            is Resource.Success -> {
                buttonIsLoading.value = false
                val context = LocalContext.current
                Toast.makeText(context, "Uspesno dodato", Toast.LENGTH_LONG).show()
            }
            null -> {}
        }
    }
}
