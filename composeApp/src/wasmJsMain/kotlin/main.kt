import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.apagar.shared.App
import org.apagar.shared.di.initKoin
import org.apagar.shared.di.initLogger
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initLogger()
    initKoin()

    ComposeViewport(document.body!!) {
        App()
    }
}
