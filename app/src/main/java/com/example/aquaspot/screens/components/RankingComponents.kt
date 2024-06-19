package com.example.aquaspot.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aquaspot.Navigation.Routes
import com.example.aquaspot.R
import com.example.aquaspot.model.CustomUser
import com.example.aquaspot.ui.theme.mainColor
import com.example.aquaspot.ui.theme.redTextColor
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun firstThreePlaces(
    users: List<CustomUser>,
    navController: NavController
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        if(users.count() > 1) {
            SecondAndThird(
                place = 2,
                user = users[1],
                onClick = {
                    val userJson = Gson().toJson(users[1])
                    val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.userProfileScreen + "/$encodedUserJson")
                }
            )
        }
        if(users.isNotEmpty()) {
            FirstPlace(
                user = users[0],
                onClick = {
                    val userJson = Gson().toJson(users[0])
                    val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.userProfileScreen + "/$encodedUserJson")
                }
            )
        }
        if(users.count() > 2) {
            SecondAndThird(
                place = 3,
                user = users[2],
                onClick = {
                    val userJson = Gson().toJson(users[2])
                    val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.userProfileScreen + "/$encodedUserJson")
                }
            )
        }
    }
}

@Composable
fun SecondAndThird(
    place: Int,
    user: CustomUser,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        if(place == 2) {
            Icon(
                imageVector = Icons.Filled.ArrowDropUp,
                contentDescription = "",
                tint = mainColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(0.dp)
            )
            Text(
                text = place.toString(),
                modifier = Modifier.offset(y = (-7).dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        else{
            Text(
                text = place.toString(),
                modifier = Modifier.offset(y = (7).dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "",
                tint = redTextColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(0.dp)
            )
        }
        AsyncImage(
            model = user.profileImage,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(35.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = user.fullName)
        Spacer(modifier = Modifier.height(3.dp))
        greyText(
            textValue =
                if(user.points <= 25) "Početnik"
                else if(user.points <= 60) "Istraživač"
                else "Kontkvistador"
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = mainColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = user.points.toString(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FirstPlace(
    user: CustomUser,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = user.profileImage,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
            Image(
                painter = painterResource(id = R.drawable.crown),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(y = (-60).dp)
                    .size(50.dp)
                    .zIndex(1f) // Higher zIndex to ensure it's above the other image
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = user.fullName)
        Spacer(modifier = Modifier.height(3.dp))
        greyText(
            textValue =
            if(user.points <= 25) "Početnik"
            else if(user.points <= 60) "Istraživač"
            else "Kontkvistador"
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = mainColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = user.points.toString(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun OtherPlaces(
    users: List<CustomUser>,
    navController: NavController
){
    users.forEachIndexed { index, user ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = (index + 4).toString(),
                    modifier = Modifier.offset(y = (7).dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    tint = redTextColor,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(0.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .background(
                        Color.White,
                        RoundedCornerShape(40.dp)
                    )
                    .width(330.dp)
                    .padding(10.dp)
                    .clickable {
                        val userJson = Gson().toJson(user)
                        val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.userProfileScreen + "/$encodedUserJson")
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = user.profileImage,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(30.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = user.fullName,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        greyText(
                            textValue =
                            if(user.points <= 25) "Početnik"
                            else if(user.points <= 60) "Istraživač"
                            else "Kontkvistador"
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "",
                        tint = mainColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = user.points.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}
