import com.example.demo.DemoApplication
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import dasniko.testcontainers.keycloak.KeycloakContainer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest(classes = [DemoApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DemoTest {
    companion object {
        // Start a Keycloak instance with an import file for the demo realm
        @JvmStatic
        private val keycloakContainer = KeycloakContainer().apply {
            withRealmImportFile("realm.json")
            portBindings = listOf("8081:8080")
            start()
        }

        // Register the Keycloak issuer URL as a dynamic property for the Spring Boot application
        @JvmStatic
        @DynamicPropertySource
        private fun registerResourceServerIssuerProperty(registry: DynamicPropertyRegistry) {
            registry.add("spring.security.oauth2.client.provider.demo-provider.issuer-uri") {
                keycloakContainer.authServerUrl + "/realms/demo-realm"
            }
        }

        // Playwright setup
        private var playwright: Playwright? = null
        private var browser: Browser? = null
        private var context: BrowserContext? = null
        private var page: Page? = null

        @JvmStatic
        @BeforeAll
        fun launchBrowser() {
            playwright = Playwright.create()
            browser = playwright?.chromium()?.launch()
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
        page?.navigate("http://localhost:8080/public")
        assertThat(page?.locator("body")).hasText("Hello from a public endpoint!")
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
        assertThat(page?.locator("body")).hasText("Hello from a private endpoint!")
    }
}
