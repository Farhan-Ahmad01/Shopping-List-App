package com.example.myshoppinglist.ui.theme

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.round

data class ShoppingItem(val id: Int,
                        var name: String,
                        var quantity: Int,
                        var isEditing: Boolean = false) {

}


@Composable
fun shoppingListApp() {

    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDilog by remember{ mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {showDilog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){
                item ->
                if(item.isEditing){
                    shoppingItemEditor(Item = item, onEditComplete ={
                        editedName, editedQuantity ->
                        sItems = sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                } else {
                    shoppingListItem(item = item,
                        onEditClick = { sItems = sItems.map { it.copy(isEditing = it.id == item.id) } },
                        onDeleteClick = { sItems = sItems-item
                        }
                        )
                }
            }
        }
    }

    if(showDilog) {
        AlertDialog(onDismissRequest = { showDilog = false },
            confirmButton = {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                          horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                 if(itemName.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = sItems.size+1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt()
                                )
                                sItems = sItems + newItem
                                itemName = ""
                                itemQuantity = ""
                                showDilog = false
                                 }
                            }) {
                                Text(text = "Add")
                            }
                            Button(onClick ={
                                showDilog = false
                                itemName = ""
                                itemQuantity = ""
                            }) {
                                Text(text = "Cancel")
                            }
                        }
            },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                        )
                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }
            )
    }
}

@Composable
fun shoppingItemEditor(Item: ShoppingItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(Item.name) }
    var editedQuantity by remember { mutableStateOf(Item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(Item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(width = 2.dp, color = Color.Cyan, shape = RoundedCornerShape(20))
        ,horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )

                BasicTextField(

                    value = editedQuantity,
                    onValueChange = { editedQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

        }

        Button(
            modifier = Modifier.padding(14.dp),
            onClick = {isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }
        ) {
            Text(text = "Save")
        }
    }
}





@Composable
fun shoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Cyan),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.name, modifier = Modifier.padding(8.dp))
            Text(text = "Quantity: ${item.quantity}", modifier = Modifier.padding(8.dp))
        }

        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick)
            {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun shoppingListAppPreview() {
    shoppingListApp()
}