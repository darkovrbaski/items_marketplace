resource "azurerm_cdn_profile" "cdn" {
  name                = "${var.name-az}Cdn"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  sku                 = "Standard_Verizon"
}

resource "azurerm_cdn_endpoint" "cdn-ep" {
  name                = "${var.name-az}CdnEp"
  profile_name        = azurerm_cdn_profile.cdn.name
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  origin_host_header  = azurerm_static_site.ss.default_host_name
  is_https_allowed    = false

  origin {
    name      = "${var.name-az}Ss"
    host_name = azurerm_static_site.ss.default_host_name
  }
}
