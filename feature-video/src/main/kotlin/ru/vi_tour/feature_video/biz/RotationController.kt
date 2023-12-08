package ru.vi_tour.feature_video.biz

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import ru.vi_tour.core.LifecycleEffect

class RotationController(
    private val context: Context
): SensorEventListener {

    private var sensorManager: SensorManager? = null

    var pitch by mutableFloatStateOf(0f)
        private set
    var roll by mutableFloatStateOf(0f)
        private set

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            updateOrientation(event.values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    internal fun onCreate() {
        sensorManager = getSystemService(context, SensorManager::class.java)
    }

    internal fun onResume() {
        sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also { accelerometer ->
            sensorManager?.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    internal fun onPause() {
        sensorManager?.unregisterListener(this)
    }

    private fun updateOrientation(rotationVector: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)

        val displayRotation: Int? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.rotation
        } else {
            (context as Activity).windowManager.defaultDisplay?.rotation
        }

        val (worldAxisForDeviceAxisX, worldAxisForDeviceAxisY) = when (displayRotation) {
            Surface.ROTATION_0 -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Z)
            Surface.ROTATION_90 -> Pair(SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_X)
            Surface.ROTATION_180 -> Pair(SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Z)
            Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_X)
            else -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Z)
        }

        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix, worldAxisForDeviceAxisX,
            worldAxisForDeviceAxisY, adjustedRotationMatrix
        )

        // Transform rotation matrix into azimuth/pitch/roll
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)

        // Convert radians to degrees
        pitch = orientation[1] * -57
        roll = orientation[2] * -57
    }
}

@Composable
internal fun rememberRotationController(): RotationController {
    val context = LocalContext.current

    val controller = remember {
        RotationController(context = context)
    }

    LifecycleEffect(
        onCreate = remember(controller) {{
            controller.onCreate()
        }},
        onResume = remember(controller) {{
            controller.onResume()
        }},
        onPause = remember(controller) {{
            controller.onPause()
        }}
    )

    return controller
}