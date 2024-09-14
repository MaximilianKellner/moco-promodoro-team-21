package com.example.promodoro_team_21.frontend

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import com.example.promodoro_team_21.R

@Composable
fun ErklaerungsDialog(onDismiss: () -> Unit, ablehnungsCount: Int,) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(id = R.string.erklaerungsDialog_title))
        },
        text = {

            //Wenn der Nutzer mindestens 2 mal abgelehnt hat
            if (ablehnungsCount >= 2) {

                Text(text = stringResource(id = R.string.desc_app_info))
            }else{
                //Wenn der Nutzer weniger als 2 mal abgelehnt hat.

                Text(text = stringResource(id = R.string.desc_normal))
            }
        },
        confirmButton = {
            if (ablehnungsCount >= 2) {
                AppInfoButton(context)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                if (ablehnungsCount >= 2) {
                    Text(stringResource(id =R.string.abbrechen_button_label))
                } else {
                    Text(stringResource(id =R.string.ok_button_label))
                }
            }
        }
    )
}

//Button, welcher die App info öffnet.
@Composable
fun AppInfoButton(context : Context){
    TextButton(
        onClick = {
            //Intent zum öffnen der App info wird erstellt und gestartet
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    ) {
        Text(stringResource(id = R.string.app_info_button_label))
    }
}