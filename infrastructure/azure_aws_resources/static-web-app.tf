resource "azurerm_static_site" "ss" {
  name                = "${var.name-az}Ss"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku_size            = "Free"
  sku_tier            = "Free"
}
