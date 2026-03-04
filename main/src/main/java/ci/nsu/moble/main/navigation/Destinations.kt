package ci.nsu.moble.main.navigation

sealed class Destination(val route: String, val label: String) {
    object Start : Destination("start", "Старт")
    object First : Destination("first", "Первый")
    object Second : Destination("second", "Второй")

    companion object {
        val bottomItems = listOf(Start, First, Second)

        fun getDestinationByRoute(route: String?): Destination {
            return when (route) {
                Start.route -> Start
                First.route -> First
                Second.route -> Second
                else -> Start
            }
        }
    }
}