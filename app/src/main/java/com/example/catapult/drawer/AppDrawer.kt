package com.example.catapult.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


// prikaz otvorenog drawera
@Composable
fun BreedListDrawer(
    onProfileClick: () -> Unit,
    onBreedsClick: () -> Unit,
    onQuizClick: () -> Unit,
) {
    BoxWithConstraints {
        ModalDrawerSheet( // prikazuje se kao modalni sheet, modalni sheet je deo ekrana koji se pojavljuje iznad ostatka ekrana
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = "Menu",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    AppDrawerActionItem(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        onClick = onProfileClick,
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Home,
                        text = "Breeds",
                        onClick = onBreedsClick,
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.ThumbUp,
                        text = "Quiz",
                        onClick = onQuizClick,
                    )
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() } // ako je onClick null, nece biti moguce kliknuti na ListItem
        ),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(text = text)
        }
    )
}
