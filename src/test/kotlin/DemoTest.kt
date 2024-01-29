import com.example.demo.DemoApplication
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import dasniko.testcontainers.keycloak.KeycloakContainer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [DemoApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration(exclude = [org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration::class])
@Testcontainers
class DemoTest {
    companion object {
//        var playwright: Playwright? = null
//        var browser: Browser? = null
//        var context: BrowserContext? = null
//        var page: Page? = null

        @JvmStatic
        @Container
        val keycloakContainer = KeycloakContainer().apply {
            withRealmImportFile("realm.json")
            portBindings = listOf("8081:8080")
        }

//        @JvmStatic
//        @BeforeAll
//        fun launchBrowser() {
//            playwright = Playwright.create()
//            browser = playwright?.chromium()?.launch()
//        }

//        @JvmStatic
//        @BeforeAll
//        fun startKeycloakServer() {
//            keycloakContainer.start()
//        }

//        @JvmStatic
//        @AfterAll
//        fun closeBrowser() {
//            playwright?.close()
//        }
    }

//    @BeforeEach
//    fun createContextAndPage() {
//        context = browser?.newContext()
//        page = context?.newPage()
//    }
//
//    @AfterEach
//    fun closeContext() {
//        context?.close()
//    }

    @Test
    fun contextLoads() {
    }

//    @Test
//    fun publicEndpointTest() {
//        println("Test 1")
//        page?.navigate("http://localhost:8080/public")
//        println("Test 2")
//        assertThat(page?.textContent("body")).isEqualTo("Hello from a public endpoint!")
//        println("Test 3")
//    }
//
//    @Test
//    fun privateEndpointTest() {
//        // Unable to access private endpoint without logging in and redirects to login page
//        page?.navigate("http://localhost:8080/private")
//
//        // Login with example user 'John Doe' by filling the login form and submitting it
//        page?.fill("#username", "john.doe")
//        page?.fill("#password", "password")
//        page?.locator("input[type=submit]")?.click()
//        // Wait for redirect to previously requested private endpoint
//        page?.waitForURL("http://localhost:8080/private?continue")
//
//        // Check for expected content on private endpoint
//        assertThat(page?.textContent("body")).isEqualTo("Hello from a private endpoint!")
//    }
}
