import dasniko.testcontainers.keycloak.KeycloakContainer

class TestKeycloakContainer : KeycloakContainer() {
    init {
        withRealmImportFile("demo-realm-realm.json")
        portBindings = listOf("8081:8080")
        start()
    }
}
