import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun QRCode(data: String, size: Int, pixelSize: Int) {
    val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
    val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, pixelSize, pixelSize, hints)
    println("dimen: ${bits.width} ${bits.height}")
    Canvas(modifier = Modifier/*.border(width = 4.dp, color = Color.Black, shape = RectangleShape)*/.size(size.dp)) {
        val sqrSize = this.size.width / pixelSize.toFloat()
        for (x in 0 until bits.width) {
            for (y in 0 until bits.height) {
                drawRect(color = if (bits[x, y]) Color.Black else Color.White,
                    topLeft = Offset((x-1).toFloat() * sqrSize, (y-1).toFloat() * sqrSize), size = Size(sqrSize.toFloat(), sqrSize.toFloat()
                ))
            }
        }
    }
}