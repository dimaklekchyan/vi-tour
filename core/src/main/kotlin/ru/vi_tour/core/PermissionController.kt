package ru.vi_tour.core

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class PermissionController(
    private val context: Context,
    private val launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    private val onGranted: (grantedPermissions: List<String>) -> Unit,
    private val onDenied: (deniedPermissions: List<String>) -> Unit,
    private val onNewKey: (key: String) -> Unit
) {

    fun launchPermission(vararg permission: String, key: String = DEFAULT_KEY, ) {
        checkPermissions(
            key = key,
            permissions = permission.asList(),
            launchAutomatically = true
        )
    }

    fun checkPermission(vararg permission: String, key: String = DEFAULT_KEY, ) {
        checkPermissions(
            key = key,
            permissions = permission.asList(),
            launchAutomatically = false
        )
    }

    fun openAppSettings() {
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        )
    }

    private fun checkPermissions(
        key: String = DEFAULT_KEY,
        permissions: List<String>,
        launchAutomatically: Boolean
    ) {
        onNewKey(key)
        val granted = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        val denied = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }

        if(granted.isNotEmpty()) {
            onGranted(granted)
        }
        if(denied.isNotEmpty()) {
            onDenied(denied)
            if (launchAutomatically) launcher.launch(denied.toTypedArray())
        }
    }

    companion object {
        private const val DEFAULT_KEY = "permissions_key"
    }
}

@Composable
fun rememberPermissionController(
    onGranted: (key: String, grantedPermissions: List<String>) -> Unit,
    onDenied: (key: String, deniedPermissions: List<String>) -> Unit,
    onShouldShowRationale: (key: String, permissions: List<String>) -> Unit
): PermissionController {
    val context = LocalContext.current

    var key by remember { mutableStateOf<String>("") }
    var grantedFromController by remember { mutableStateOf<List<String>>(emptyList()) }
    var grantedFromLauncher by remember { mutableStateOf<List<String>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        grantedFromLauncher = map.filter { it.value }.map { it.key }
        val rationale = map.filter { !it.value }.map { it.key }

        if(grantedFromLauncher.isNotEmpty()) onGranted(key, grantedFromLauncher + grantedFromController)
        if(rationale.isNotEmpty()) onShouldShowRationale(key, rationale)
    }

    return remember {
        PermissionController(
            context = context,
            launcher = launcher,
            onGranted = {
                grantedFromController = it
                onGranted(key, grantedFromController + grantedFromLauncher)
            },
            onDenied = { onDenied(key, it) },
            onNewKey = { key = it }
        )
    }
}