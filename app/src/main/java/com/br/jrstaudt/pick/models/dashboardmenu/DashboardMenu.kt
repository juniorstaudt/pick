package com.br.jrstaudt.pick.models.dashboardmenu

data class DashboardMenu(
    val title: String,
    val subTitle: String,
    val items: List<DashboardItem>
)
data class DashboardItem(
    val feature: String,
    val image: String,
    val label: String,
    val action: DashboardAction
)
data class DashboardAction(
    val deeplink: String,
    val params: HashMap<String, Any>
)