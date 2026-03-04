package ci.nsu.moble.main.navigation

sealed class NavRoutes(val route: String, val title: String) {
    object Start : NavRoutes("start", "Старт")
    object First : NavRoutes("first", "Первый")
    object Second : NavRoutes("second", "Второй")

    companion object {
        val bottomNavItems = listOf(Start, First, Second)

        fun fromRoute(route: String?): NavRoutes {
            return when (route) {
                Start.route -> Start
                First.route -> First
                Second.route -> Second
                else -> Start
            }
        }
    }
}