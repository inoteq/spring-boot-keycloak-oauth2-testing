import com.example.demo.DemoApplication
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import dasniko.testcontainers.keycloak.KeycloakContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Container

@SpringBootTest(classes = [DemoApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DemoTest {
    companion object {
        var playwright: Playwright? = null
        var browser: Browser? = null
        var context: BrowserContext? = null
        var page: Page? = null

        @JvmStatic
        @Container
        var keycloakContainer: KeycloakContainer? = null

        @JvmStatic
        @BeforeAll
        fun launchBrowser() {
            playwright = Playwright.create()
            browser = playwright?.firefox()?.launch()
        }

        @JvmStatic
        @BeforeAll
        fun startKeycloakServer() {
            keycloakContainer = KeycloakContainer().apply {
                // realm.json file is located in src/test/resources
                withRealmImportFile("realm.json")
                portBindings = listOf("8081:8080")
                start()
            }
        }

        @JvmStatic
        @AfterAll
        fun closeBrowser() {
            playwright?.close()
        }
    }

    @BeforeEach
    fun createContextAndPage() {
        context = browser?.newContext()
        page = context?.newPage()
    }

    @AfterEach
    fun closeContext() {
        context?.close()
    }

    @Test
    fun publicEndpointTest() {
        println("Test 1")
        page?.navigate("http://localhost:8080/public")
        println("Test 2")
        assertThat(page?.textContent("body")).isEqualTo("Hello from a public endpoint!")
        println("Test 3")
    }

    @Test
    fun privateEndpointTest() {
        // Unable to access private endpoint without logging in and redirects to login page
        page?.navigate("http://localhost:8080/private")

        // Login with example user 'John Doe' by filling the login form and submitting it
        page?.fill("#username", "john.doe")
        page?.fill("#password", "password")
        page?.locator("input[type=submit]")?.click()
        // Wait for redirect to previously requested private endpoint
        page?.waitForURL("http://localhost:8080/private?continue")

        // Check for expected content on private endpoint
        assertThat(page?.textContent("body")).isEqualTo("Hello from a private endpoint!")
    }
}
