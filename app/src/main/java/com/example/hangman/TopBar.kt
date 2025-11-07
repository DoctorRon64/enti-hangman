import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hangman.R

class TopBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val switchTheme: ImageButton
    private val switchLang: ImageButton

    init {
        inflate(context, R.layout.topbar, this)
        switchTheme = findViewById(R.id.switchTheme)
        switchLang = findViewById(R.id.switchLang)
    }

    fun setOnThemeClick(handler: (currentMode: Int) -> Unit) {
        switchTheme.setOnClickListener {
            val mode = AppCompatDelegate.getDefaultNightMode()
            handler(mode)
        }
    }

    fun setOnLanguageClick(handler: () -> Unit) {
        switchLang.setOnClickListener { handler() }
    }
}
